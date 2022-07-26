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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

import tr.com.argela.nfv.onap.api.client.OnapClient;
import tr.com.argela.nfv.onap.api.client.model.OnapRequest;
import tr.com.argela.nfv.onap.api.client.model.OnapRequestParameters;
import tr.com.argela.nfv.onap.api.client.model.VFModuleParameter;
import tr.com.argela.nfv.onap.api.client.model.VFModuleProfile;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */

public class RuntimeService {

    @Autowired
    OnapClient adaptor;

    Logger log = LoggerFactory.getLogger(RuntimeService.class);

    public JSONArray getServiceInstances(String customerId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_CUSTOMER_ID.name(), customerId);
        JSONArray data = (JSONArray) adaptor.call(OnapRequest.RUNTIME_SERVICE_INSTANCES, parameters);
        if (log.isInfoEnabled())
            log.info("[Runtime][ServiceInstances][Get] " + parameters + " ,size:" + data.length());
        return data;
    }

    public JSONObject getServiceInstanceDetails(String serviceInstanceId)
            throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.RUNTIME_SERVICE_INSTANCE_ID.name(), serviceInstanceId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.RUNTIME_SERVICE_INSTANCE_DETAIL, parameters);
        if (log.isInfoEnabled())
            log.info("[Runtime][ServiceInstanceDetail][Get] service-instance-id:" + serviceInstanceId + " , response:"
                    + data.toString());
        return data;
    }

    public JSONObject createServiceInstance(String serviceInstanceName,
            String serviceModelInvariantUUID,
            String serviceModelUUID,
            String serviceName,
            String owningId,
            String owningName,
            String customerId,
            String projectName) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.RUNTIME_SERVICE_INSTANCE_NAME.name(), serviceInstanceName);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_InvariantUUID.name(), serviceModelInvariantUUID);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_UUID.name(), serviceModelUUID);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_NAME.name(), serviceName);
        parameters.put(OnapRequestParameters.BUSINESS_OWNING_ENTITY_ID.name(), owningId);
        parameters.put(OnapRequestParameters.BUSINESS_OWNING_ENTITY_NAME.name(), owningName);
        parameters.put(OnapRequestParameters.BUSINESS_CUSTOMER_ID.name(), customerId);
        parameters.put(OnapRequestParameters.BUSINESS_PROJECT_NAME.name(), projectName);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.RUNTIME_SERVICE_INSTANCE_CREATE, parameters);
        if (log.isInfoEnabled())
            log.info("[Runtime][ServiceInstance][Create] " + parameters + " , response:" + data.toString());
        return data;
    }

    public JSONObject deleteServiceInstance(String serviceInstanceId,
            String serviceName,
            String serviceModelUUID,
            String serviceModelInvariantUUID) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.RUNTIME_SERVICE_INSTANCE_ID.name(), serviceInstanceId);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_NAME.name(), serviceName);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_UUID.name(), serviceModelUUID);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_InvariantUUID.name(), serviceModelInvariantUUID);

        JSONObject data = (JSONObject) adaptor.call(OnapRequest.RUNTIME_SERVICE_INSTANCE_DELETE, parameters);
        if (log.isInfoEnabled())
            log.info("[Runtime][ServiceInstance][Delete] " + parameters + " , response:" + data.toString());
        return data;
    }

    public JSONObject getVNFs() throws IOException {
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.RUNTIME_VNFS);
        if (log.isInfoEnabled())
            log.info("[Runtime][Vnfs][Get] size: " + adaptor.getResponseSize(data, "generic-vnf"));
        return data;
    }

    public JSONObject getActionStatus(@RequestParam("url") String url) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.NOTIFICATION_URL.name(), url);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.RUNTIME_ACTION_STATUS, parameters);
        JSONObject requestStatus = null;
        if (data.has("request")) {
            JSONObject request = data.getJSONObject("request");
            if (request.has("requestStatus")) {
                requestStatus = request.getJSONObject("requestStatus");
            }
        }
        if (log.isInfoEnabled())
            log.info("[Runtime][Action][Status] " + parameters + ", response:" + requestStatus);
        return data;
    }

    public JSONObject createVNF(String vnfName,
            String serviceInstanceId,
            String serviceName,
            String serviceInvariantUUID,
            String serviceUUID,
            String serviceUniqueId,
            String cloudOwner,
            String cloudRegion,
            String tenantId,
            String vfName,
            String vfModelName,
            String vfInvariantUUID,
            String vfUUID,
            String lineOfBusiness,
            String platformName) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.RUNTIME_VNF_NAME.name(), vnfName);
        parameters.put(OnapRequestParameters.RUNTIME_SERVICE_INSTANCE_ID.name(), serviceInstanceId);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_NAME.name(), serviceName);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_InvariantUUID.name(), serviceInvariantUUID);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_UUID.name(), serviceUUID);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_UNIQUE_ID.name(), serviceUniqueId);

        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        parameters.put(OnapRequestParameters.CLOUD_REGION.name(), cloudRegion);
        parameters.put(OnapRequestParameters.CLOUD_TENANT_ID.name(), tenantId);
        parameters.put(OnapRequestParameters.DESIGN_VF_NAME.name(), vfName);
        parameters.put(OnapRequestParameters.DESIGN_VF_MODEL_NAME.name(), vfModelName);
        parameters.put(OnapRequestParameters.DESIGN_VF_invariantUUID.name(), vfInvariantUUID);
        parameters.put(OnapRequestParameters.DESIGN_VF_UUID.name(), vfUUID);
        parameters.put(OnapRequestParameters.BUSINESS_LINE_OF_BUSINESS.name(), lineOfBusiness);
        parameters.put(OnapRequestParameters.BUSINESS_PLATFORM_NAME.name(), platformName);

        JSONObject data = (JSONObject) adaptor.call(OnapRequest.RUNTIME_VNF_CREATE, parameters);
        if (log.isInfoEnabled())
            log.info("[Runtime][VNF][Create] " + parameters + " , response:" + data.toString());
        return data;
    }

    public JSONObject deleteVNF(String vnfId,
            String serviceInstanceId,
            String vfName,
            String vfUUID,
            String vfInvariantUUID,
            String vfModelName,
            String cloudOwner,
            String cloudRegion,
            String tenantId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.RUNTIME_VNF_ID.name(), vnfId);
        parameters.put(OnapRequestParameters.RUNTIME_SERVICE_INSTANCE_ID.name(), serviceInstanceId);

        parameters.put(OnapRequestParameters.DESIGN_VF_NAME.name(), vfName);
        parameters.put(OnapRequestParameters.DESIGN_VF_UUID.name(), vfUUID);
        parameters.put(OnapRequestParameters.DESIGN_VF_invariantUUID.name(), vfInvariantUUID);
        parameters.put(OnapRequestParameters.DESIGN_VF_MODEL_NAME.name(), vfModelName);

        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        parameters.put(OnapRequestParameters.CLOUD_REGION.name(), cloudRegion);
        parameters.put(OnapRequestParameters.CLOUD_TENANT_ID.name(), tenantId);

        JSONObject data = (JSONObject) adaptor.call(OnapRequest.RUNTIME_VNF_DELETE, parameters);
        if (log.isInfoEnabled())
            log.info("[Runtime][VNF][Delete] " + parameters + " , response:" + data.toString());
        return data;
    }

    public JSONObject getVNFDetail(String vnfId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.RUNTIME_VNF_ID.name(), vnfId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.RUNTIME_VNF_DETAIL, parameters);
        if (log.isInfoEnabled())
            log.info("[Runtime][VnfDetail][Get] vnfId:" + vnfId + " , response: " + data);
        return data;
    }

    public JSONObject getVNFDetailByService(
            String serviceUniqueId,
            String vnfName) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.RUNTIME_VNF_NAME.name(), vnfName);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_UNIQUE_ID.name(), serviceUniqueId);

        String filter = "";
        if (vnfName != null) {
            filter = "&vnf-name=" + vnfName;
        }
        if (serviceUniqueId != null) {
            filter = "&service-id=" + serviceUniqueId;
        }
        if (!"".equals(filter)) {
            parameters.put(OnapRequestParameters.REQUEST_PARAMETERS.name(), filter);
        }

        JSONObject data = (JSONObject) adaptor.call(OnapRequest.RUNTIME_VNFS_DETAIL_BY_SERVICE, parameters);
        if (log.isInfoEnabled())
            log.info("[Runtime][VnfDetailByService][Get] " + parameters + " , response: "
                    + adaptor.getResponseSize(data, "generic-vnf"));
        return data;
    }

    public JSONObject getVFModules(String vnfId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.RUNTIME_VNF_ID.name(), vnfId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.RUNTIME_VFMODULES, parameters);
        if (log.isInfoEnabled())
            log.info("[Runtime][VFModules][Get] vnfId:" + vnfId + " , size: "
                    + adaptor.getResponseSize(data, "vf-module"));
        return data;
    }

    public JSONObject preloadVFModule(
            String vnfName,
            String vnfType,
            String vfModuleName,
            String vfModuleType,
            String availabilityZone,
            VFModuleProfile vFModuleProfile) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.RUNTIME_VNF_NAME.name(), vnfName);
        parameters.put(OnapRequestParameters.DESIGN_VF_MODEL_NAME.name(), vnfType);
        parameters.put(OnapRequestParameters.RUNTIME_VFMODULE_NAME.name(), vfModuleName);
        parameters.put(OnapRequestParameters.DESIGN_VF_MODEL_TYPE.name(), vfModuleType);
        parameters.put(OnapRequestParameters.CLOUD_AVAILABILITY_ZONE.name(), availabilityZone);
        JSONArray preloadParameters = new JSONArray();
        for (VFModuleParameter parameter : vFModuleProfile.getParameters()) {
            JSONObject obj = new JSONObject();
            obj.put("name", parameter.getName());
            obj.put("value", parameter.getValue());
            preloadParameters.put(obj);
        }
        parameters.put(OnapRequestParameters.RUNTIME_VFMODULE_PARAMS.name(), preloadParameters.toString());
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.RUNTIME_VFMODULE_PRELOAD, parameters);
        if (log.isInfoEnabled())
            log.info("[Runtime][VFModule][Preload] " + parameters + " , response:" + data);
        return data;
    }

    public JSONObject createVfModule(
            String vnfId,
            String vfModuleName,
            String serviceInstanceId,
            String serviceName,
            String serviceInvariantUUID,
            String serviceUUID,
            String cloudOwner,
            String cloudRegion,
            String tenantId,
            String vfUUID,
            String vfName,
            String vfModelUUID,
            String vfModelType,
            String vfModelName,
            String vfModelInvariantId,
            String vfModelCustomizationId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.RUNTIME_VNF_ID.name(), vnfId);
        parameters.put(OnapRequestParameters.RUNTIME_VFMODULE_NAME.name(), vfModuleName);

        parameters.put(OnapRequestParameters.RUNTIME_SERVICE_INSTANCE_ID.name(), serviceInstanceId);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_NAME.name(), serviceName);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_InvariantUUID.name(), serviceInvariantUUID);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_UUID.name(), serviceUUID);

        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        parameters.put(OnapRequestParameters.CLOUD_REGION.name(), cloudRegion);
        parameters.put(OnapRequestParameters.CLOUD_TENANT_ID.name(), tenantId);

        parameters.put(OnapRequestParameters.DESIGN_VF_NAME.name(), vfName);
        parameters.put(OnapRequestParameters.DESIGN_VF_MODEL_UUID.name(), vfModelUUID);
        parameters.put(OnapRequestParameters.DESIGN_VF_MODEL_TYPE.name(), vfModelType);
        parameters.put(OnapRequestParameters.DESIGN_VF_MODEL_NAME.name(), vfModelName);
        parameters.put(OnapRequestParameters.DESIGN_VF_MODEL_InvariantId.name(), vfModelInvariantId);
        parameters.put(OnapRequestParameters.DESIGN_VF_UUID.name(), vfUUID);
        parameters.put(OnapRequestParameters.DESIGN_VF_MODEL_CUSTOMIZATION_ID.name(), vfModelCustomizationId);

        JSONObject data = (JSONObject) adaptor.call(OnapRequest.RUNTIME_VFMODULE_CREATE, parameters);
        if (log.isInfoEnabled())
            log.info("[Runtime][VFModule][Create] " + parameters + " , response:" + data.toString());
        return data;
    }

    public JSONObject deleteVfModule(
            String vnfId,
            String vfModuleId,
            String serviceInstanceId,
            String vfModelType,
            String vfModelUUID,
            String vfModelInvariantId,
            String cloudOwner,
            String cloudRegion,
            String tenantId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.RUNTIME_SERVICE_INSTANCE_ID.name(), serviceInstanceId);
        parameters.put(OnapRequestParameters.RUNTIME_VNF_ID.name(), vnfId);
        parameters.put(OnapRequestParameters.RUNTIME_VF_MODULE_ID.name(), vfModuleId);

        parameters.put(OnapRequestParameters.DESIGN_VF_MODEL_TYPE.name(), vfModelType);
        parameters.put(OnapRequestParameters.DESIGN_VF_MODEL_UUID.name(), vfModelUUID);
        parameters.put(OnapRequestParameters.DESIGN_VF_MODEL_InvariantId.name(), vfModelInvariantId);

        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        parameters.put(OnapRequestParameters.CLOUD_REGION.name(), cloudRegion);
        parameters.put(OnapRequestParameters.CLOUD_TENANT_ID.name(), tenantId);

        JSONObject data = (JSONObject) adaptor.call(OnapRequest.RUNTIME_VFMODULE_DELETE, parameters);
        if (log.isInfoEnabled())
            log.info("[Runtime][VFModule][Delete] " + parameters + " , response:" + data.toString());
        return data;
    }

    public JSONObject getVFModuleDetail(String vnfId,
            String vfModuleId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.RUNTIME_VNF_ID.name(), vnfId);
        parameters.put(OnapRequestParameters.RUNTIME_VF_MODULE_ID.name(), vfModuleId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.RUNTIME_VFMODULE_DETAIL, parameters);
        if (log.isInfoEnabled())
            log.info("[Runtime][VFModuleDetail][Get] vnfId:" + vnfId + ",vfModuleId:" + vfModuleId + " , response:"
                    + data.toString());
        return data;
    }

    public JSONObject getVFModuleInstantiationDetail(String vfModuleId)
            throws IOException {
        Map<String, String> parameters = new HashMap<>();

        parameters.put(OnapRequestParameters.RUNTIME_VF_MODULE_ID.name(), vfModuleId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.RUNTIME_VFMODULE_INSTANTIATE_DETAIL, parameters);
        if (log.isInfoEnabled())
            log.info("[Runtime][VFModuleDetail][Get] vfModuleId:" + vfModuleId + " , response:" + data.toString());
        return data;
    }

    public JSONObject getVFModuleTopology(String serviceInstanceId,
            String vnfId, String vfModuleId)
            throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.RUNTIME_SERVICE_INSTANCE_ID.name(), serviceInstanceId);
        parameters.put(OnapRequestParameters.RUNTIME_VNF_ID.name(), vnfId);
        parameters.put(OnapRequestParameters.RUNTIME_VF_MODULE_ID.name(), vfModuleId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.RUNTIME_VFMODULE_TOPOLOGY, parameters);
        if (log.isInfoEnabled())
            log.info("[Runtime][VFModuleDetail][Get] service-instance-id:" + serviceInstanceId + ", vnfId:" + vnfId
                    + " , vfModuleId:" + vfModuleId + " , response:" + data.toString());
        return data;
    }

}
