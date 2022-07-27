/*
# Copyright © 2021 Argela Technologies
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
 */
package tr.com.argela.nfv.onap.api.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import tr.com.argela.nfv.onap.api.client.OnapClient;
import tr.com.argela.nfv.onap.api.client.model.OnapRequest;
import tr.com.argela.nfv.onap.api.client.model.OnapRequestParameters;
import tr.com.argela.nfv.onap.api.util.OnapUtil;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@Service
public class CloudService {

    @Autowired
    OnapClient adaptor;

    Logger log = LoggerFactory.getLogger(CloudService.class);

    public JSONObject getCloudComplexs() throws IOException {
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.CLOUD_COMPLEXS);
        if (log.isInfoEnabled())
            log.info("[Cloud][Complexs][Get] size:" + adaptor.getResponseSize(data, "complex"));
        return data;
    }

    public JSONObject getCloudComplex(String name) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_COMPLEX_NAME.name(), name);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.CLOUD_COMPLEXS);
        if (log.isInfoEnabled())
            log.info("[Cloud][Complex][Get] " + parameters + " , result : " + data);
        return data;
    }

    public void createCloudComplex(String name) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_COMPLEX_NAME.name(), name);
        adaptor.call(OnapRequest.CLOUD_COMPLEX_CREATE, parameters);
        if (log.isInfoEnabled())
            log.info("[Cloud][Complex][Create] " + parameters);
    }

    public JSONObject getCloudRegions() throws IOException {
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.CLOUD_REGIONS);
        if (log.isInfoEnabled())
            log.info("[Cloud][Regions][Get] size:" + adaptor.getResponseSize(data, "cloud-region"));
        return data;
    }

    public JSONObject getCloudRegion(String cloudOwner, String name)
            throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_NAME.name(), name);
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.CLOUD_REGION, parameters);
        if (log.isInfoEnabled())
            log.info("[Cloud][Region][Get] " + parameters + " , response : " + data);
        return data;
    }

    public void createOpenstackRegion(String cloudOwner, String name,
            String regionName, String complexName,
            String osDomain, String osDefaultProject,
            String osKeystoneURL, String osUser,
            String osPassword) throws IOException {
        String aaiResponse = createRegion(OnapRequest.CLOUD_REGION_CREATE.getPayloadFilePath(), name, regionName,
                cloudOwner, complexName, osDomain, osDefaultProject, osKeystoneURL, osUser, osPassword);

        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_NAME.name(), name);
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        adaptor.call(OnapRequest.CLOUD_REGION_CREATE_MSB, parameters);
    }

    private String createRegion(String payload, String name, String regionName, String cloudOwner, String complexName,
            String osDomain, String osDefaultProject,
            String osKeystoneURL, String osUser, String osPassword) throws IOException {
        Map<String, String> parameters = new HashMap<>();

        UUID esrUUID = UUID.randomUUID();

        parameters.put(OnapRequestParameters.CLOUD_ESR_UUID.name(), esrUUID.toString());
        parameters.put(OnapRequestParameters.CLOUD_NAME.name(), name);
        parameters.put(OnapRequestParameters.CLOUD_REGION.name(), regionName);
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        parameters.put(OnapRequestParameters.CLOUD_COMPLEX_NAME.name(), complexName);
        parameters.put(OnapRequestParameters.CLOUD_COMPLEX_NAME.name(), complexName);
        parameters.put(OnapRequestParameters.CLOUD_OS_KEYSTONE_URL.name(), osKeystoneURL);
        parameters.put(OnapRequestParameters.CLOUD_OS_USER.name(), osUser);
        parameters.put(OnapRequestParameters.CLOUD_OS_PASSWORD.name(), osPassword);
        parameters.put(OnapRequestParameters.CLOUD_OS_DOMAIN.name(), osDomain);
        parameters.put(OnapRequestParameters.CLOUD_OS_PROJECT.name(), osDefaultProject);
        String data = (String) adaptor.call(OnapRequest.CLOUD_REGION_CREATE, parameters, null, payload);
        if (log.isInfoEnabled())
            log.info("[Cloud][CloudRegion][Create] " + parameters + " , response : " + data);
        return data;
    }

    public void createK8SRegion(
            String cloudOwner,
            String name,
            String complex,
            String namespace,
            @RequestBody String kubeconfig) throws IOException {

        String body = "{\"cloud-region\":\"" + name + "\",\"cloud-owner\":\"" + cloudOwner
                + "\",\"other-connectivity-list\":{\"connectivity-records\":[{\"ssl-initiator\":\"false\"}]}};type=application/json";
        File kubeConfigFile = OnapUtil.writeStringToTmpFile(kubeconfig, "k8s_config", ".json");
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_NAME.name(), name);
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        parameters.put(OnapRequestParameters.CLOUD_COMPLEX_NAME.name(), complex);

        Map<String, Object> files = new HashMap<>();
        files.put("metadata", body);
        files.put("file", kubeConfigFile);

        String data = (String) adaptor.call(OnapRequest.CLOUD_K8S_MSB_ADD_KUBECONFIG, parameters, files);

        kubeConfigFile.delete();
        String data2 = (String) createRegion("payloads/cloud/region_k8s_create.json", name, name, cloudOwner, complex,
                null, namespace, null, null, null);
        if (log.isInfoEnabled())
            log.info("[Cloud][K8S][Create] " + parameters);
    }

    public void createCloudRegionComplexRelations(
            String cloudOwner,
            String name,
            String complex) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_COMPLEX_NAME.name(), complex);
        parameters.put(OnapRequestParameters.CLOUD_REGION.name(), name);
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        String data = (String) adaptor.call(OnapRequest.CLOUD_REGION_COMPLEX_Relations, parameters);
        if (log.isInfoEnabled())
            log.info("[Cloud][ComplexRegionRelation][Create] " + parameters + " , response:" + data);
    }

    public JSONObject getCloudTenants(String cloudOwner, String cloudRegion)
            throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        parameters.put(OnapRequestParameters.CLOUD_REGION.name(), cloudRegion);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.CLOUD_TENANTS, parameters);
        if (log.isInfoEnabled())
            log.info("[Cloud][Tenants][Get] " + parameters + " , size:" + adaptor.getResponseSize(data, "tenant"));
        return data;
    }

    public JSONObject getCloudTenant(String cloudOwner, String cloudRegion,
            String tenantId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        parameters.put(OnapRequestParameters.CLOUD_REGION.name(), cloudRegion);
        parameters.put(OnapRequestParameters.CLOUD_TENANT_ID.name(), tenantId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.CLOUD_TENANT, parameters);
        if (log.isInfoEnabled())
            log.info("[Cloud][Tenant][Get] " + parameters + ", response : " + data);
        return data;
    }

    public void createCloudTenant(
            String cloudOwner,
            String cloudRegion,
            String tenantId,
            String tenantName) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        parameters.put(OnapRequestParameters.CLOUD_REGION.name(), cloudRegion);
        parameters.put(OnapRequestParameters.CLOUD_TENANT_ID.name(), tenantId);
        parameters.put(OnapRequestParameters.CLOUD_TENANT_NAME.name(), tenantName);
        String data = (String) adaptor.call(OnapRequest.CLOUD_TENANT_CREATE, parameters);
        if (log.isInfoEnabled())
            log.info("[Cloud][Tenant][Create] " + parameters + " , response : " + data);
    }

    public JSONObject getCloudAvailabilityZones(String cloudOwner, String cloudRegion)
            throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        parameters.put(OnapRequestParameters.CLOUD_REGION.name(), cloudRegion);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.CLOUD_AVAILABILITY_ZONES, parameters);
        if (log.isInfoEnabled())
            log.info("[Cloud][AvailabilityZones][Get] " + parameters + " , size:"
                    + adaptor.getResponseSize(data, "availability-zone"));
        return data;
    }

    public JSONObject getCloudAvailabilityZone(String cloudOwner, String cloudRegion,
            String availibilityZone) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        parameters.put(OnapRequestParameters.CLOUD_REGION.name(), cloudRegion);
        parameters.put(OnapRequestParameters.CLOUD_AVAILABILITY_ZONE.name(), availibilityZone);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.CLOUD_AVAILABILITY_ZONE, parameters);
        if (log.isInfoEnabled())
            log.info("[Cloud][AvailabilityZone][Get] " + parameters + " , " + data);
        return data;
    }

    public void createCloudAvailibilityZone(
            String cloudOwner,
            String cloudRegion,
            String azName,
            String azHypervisorType) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        parameters.put(OnapRequestParameters.CLOUD_REGION.name(), cloudRegion);
        parameters.put(OnapRequestParameters.CLOUD_AVAILABILITY_ZONE.name(), azName);
        parameters.put(OnapRequestParameters.CLOUD_HYPERVISOR_TYPE.name(), azHypervisorType);
        String data = (String) adaptor.call(OnapRequest.CLOUD_AVAILABILITY_ZONE_CREATE, parameters);
        if (log.isInfoEnabled())
            log.info("[Cloud][AvailabilityZone][Create] " + parameters + " , response : " + data);
    }

    public JSONObject getVServerDetail(
            @PathVariable(required = true) String cloudOwner,
            @PathVariable(required = true) String regionName,
            @PathVariable(required = true) String tenantId,
            @PathVariable(required = true) String vServerId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        parameters.put(OnapRequestParameters.CLOUD_REGION.name(), regionName);
        parameters.put(OnapRequestParameters.CLOUD_TENANT_ID.name(), tenantId);
        parameters.put(OnapRequestParameters.CLOUD_VSERVER_ID.name(), vServerId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.CLOUD_VSERVER_DETAIL, parameters);
        if (log.isInfoEnabled())
            log.info("[Cloud][VServer][Get] " + parameters);
        return data;
    }

    public JSONObject getFlavorDetail(
            @PathVariable(required = true) String cloudOwner,
            @PathVariable(required = true) String regionName,
            @PathVariable(required = true) String flavorId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        parameters.put(OnapRequestParameters.CLOUD_REGION.name(), regionName);
        parameters.put(OnapRequestParameters.CLOUD_OS_FLAVOR_ID.name(), flavorId);

        JSONObject data = (JSONObject) adaptor.call(OnapRequest.CLOUD_VSERVER_FLAVOR_DETAIL, parameters);
        if (log.isInfoEnabled())
            log.info("[Cloud][Flavor][Get] " + parameters);
        return data;
    }

    public JSONObject getK8SInstanceDetail(
            @PathVariable(required = true) String helmInstId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.CLOUD_HELM_INST_ID.name(), helmInstId);

        JSONObject data = (JSONObject) adaptor.call(OnapRequest.CLOUD_K8S_INST_DETAIL, parameters);
        if (log.isInfoEnabled())
            log.info("[Cloud][K8S][Get] " + parameters);
        return data;
    }
}
