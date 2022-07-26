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
package tr.com.argela.nfv.onap.service.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tr.com.argela.nfv.onap.api.client.OnapClient;
import tr.com.argela.nfv.onap.api.client.model.OnapRequest;
import tr.com.argela.nfv.onap.api.client.model.OnapRequestParameters;
import tr.com.argela.nfv.onap.api.client.model.VFModuleParameter;
import tr.com.argela.nfv.onap.api.client.model.VFModuleProfile;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@RestController

public class RuntimeController {

    @Autowired
    OnapClient adaptor;

    Logger log = LoggerFactory.getLogger(RuntimeController.class);

    @GetMapping(path = "/runtime/service-instances/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getServiceInstances(@PathVariable(required = true) String customerId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_CUSTOMER_ID.name(), customerId);
        JSONArray data = (JSONArray) adaptor.call(OnapRequest.RUNTIME_SERVICE_INSTANCES, parameters);
        log.info("[Runtime][ServiceInstances][Get] " + parameters + " ,size:" + data.length());
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/runtime/service-instance/{serviceInstanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getServiceInstanceDetails(@PathVariable(required = true) String serviceInstanceId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.RUNTIME_SERVICE_INSTANCE_ID.name(), serviceInstanceId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.RUNTIME_SERVICE_INSTANCE_DETAIL, parameters);
        log.info("[Runtime][ServiceInstanceDetail][Get] service-instance-id:" + serviceInstanceId + " , response:" + data.toString());
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/runtime/service-instance/{serviceInstanceName}/{serviceModelInvariantUUID}/{serviceModelUUID}/{serviceName}/{owningId}/{owningName}/{customerId}/{projectName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createServiceInstance(@PathVariable String serviceInstanceName,
            @PathVariable String serviceModelInvariantUUID,
            @PathVariable String serviceModelUUID,
            @PathVariable String serviceName,
            @PathVariable String owningId,
            @PathVariable String owningName,
            @PathVariable String customerId,
            @PathVariable String projectName
    ) throws IOException {
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
        log.info("[Runtime][ServiceInstance][Create] " + parameters + " , response:" + data.toString());
        return ResponseEntity.ok(data.toString());
    }

    @DeleteMapping(path = "/runtime/service-instance/{serviceInstanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteServiceInstance(@PathVariable String serviceInstanceId,
            @RequestParam(name = "serviceName") String serviceName,
            @RequestParam(name = "serviceUUID") String serviceModelUUID,
            @RequestParam(name = "serviceInvariantUUID") String serviceModelInvariantUUID
    ) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.RUNTIME_SERVICE_INSTANCE_ID.name(), serviceInstanceId);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_NAME.name(), serviceName);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_UUID.name(), serviceModelUUID);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_InvariantUUID.name(), serviceModelInvariantUUID);

        JSONObject data = (JSONObject) adaptor.call(OnapRequest.RUNTIME_SERVICE_INSTANCE_DELETE, parameters);
        log.info("[Runtime][ServiceInstance][Delete] " + parameters + " , response:" + data.toString());
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/runtime/vnfs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getVNFs() throws IOException {
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.RUNTIME_VNFS);
        log.info("[Runtime][Vnfs][Get] size: " + adaptor.getResponseSize(data, "generic-vnf"));
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/runtime/action/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getActionStatus(@RequestParam("url") String url) {
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
        log.info("[Runtime][Action][Status] " + parameters + ", response:" + requestStatus);
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/runtime/vnf/{vnfName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createVNF(@PathVariable String vnfName,
            @RequestParam(name = "serviceInstanceId") String serviceInstanceId,
            @RequestParam(name = "serviceName") String serviceName,
            @RequestParam(name = "serviceInvariantUUID") String serviceInvariantUUID,
            @RequestParam(name = "serviceUUID") String serviceUUID,
            @RequestParam(name = "serviceUniqueId") String serviceUniqueId,
            @RequestParam(name = "cloudOwner") String cloudOwner,
            @RequestParam(name = "cloudRegion") String cloudRegion,
            @RequestParam(name = "tenantId") String tenantId,
            @RequestParam(name = "vfName") String vfName,
            @RequestParam(name = "vfModelName") String vfModelName,
            @RequestParam(name = "vfInvariantUUID") String vfInvariantUUID,
            @RequestParam(name = "vfUUID") String vfUUID,
            @RequestParam(name = "lineOfBusiness") String lineOfBusiness,
            @RequestParam(name = "platformName") String platformName
    ) throws IOException {
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
        log.info("[Runtime][VNF][Create] " + parameters + " , response:" + data.toString());
        return ResponseEntity.ok(data.toString());
    }

    @DeleteMapping(path = "/runtime/vnf/{vnfId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteVNF(@PathVariable String vnfId,
            @RequestParam(name = "serviceInstanceId") String serviceInstanceId,
            @RequestParam(name = "vfName") String vfName,
            @RequestParam(name = "vfUUID") String vfUUID,
            @RequestParam(name = "vfInvariantUUID") String vfInvariantUUID,
            @RequestParam(name = "vfModelName") String vfModelName,
            @RequestParam(name = "cloudOwner") String cloudOwner,
            @RequestParam(name = "cloudRegion") String cloudRegion,
            @RequestParam(name = "tenantId") String tenantId
    ) throws IOException {
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
        log.info("[Runtime][VNF][Delete] " + parameters + " , response:" + data.toString());
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/runtime/vnf/{vnfId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getVNFDetail(@PathVariable(required = true) String vnfId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.RUNTIME_VNF_ID.name(), vnfId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.RUNTIME_VNF_DETAIL, parameters);
        log.info("[Runtime][VnfDetail][Get] vnfId:" + vnfId + " , response: " + data);
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/runtime/vnf/{serviceUniqueId}/vnfName", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getVNFDetailByService(
            @PathVariable(required = true) String serviceUniqueId,
            @PathVariable(required = true) String vnfName
    ) throws IOException {
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
        log.info("[Runtime][VnfDetailByService][Get] " + parameters + " , response: " + adaptor.getResponseSize(data, "generic-vnf"));
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/runtime/vf-modules/{vnfId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getVFModules(@PathVariable(required = true) String vnfId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.RUNTIME_VNF_ID.name(), vnfId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.RUNTIME_VFMODULES, parameters);
        log.info("[Runtime][VFModules][Get] vnfId:" + vnfId + " , size: " + adaptor.getResponseSize(data, "vf-module"));
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/runtime/vf-module/{vnfName}/{vnfType}/{vfModuleName}/{vfModuleType}/{availabilityZone}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity preloadVFModule(
            @PathVariable(required = true) String vnfName,
            @PathVariable(required = true) String vnfType,
            @PathVariable(required = true) String vfModuleName,
            @PathVariable(required = true) String vfModuleType,
            @PathVariable(required = true) String availabilityZone,
            @RequestBody VFModuleProfile vFModuleProfile) throws IOException {
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
        log.info("[Runtime][VFModule][Preload] " + parameters + " , response:" + data);
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/runtime/vfModule/{vnfId}/{vfModuleName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createVfModule(
            @PathVariable String vnfId,
            @PathVariable String vfModuleName,
            @RequestParam(name = "serviceInstanceId") String serviceInstanceId,
            @RequestParam(name = "serviceName") String serviceName,
            @RequestParam(name = "serviceInvariantUUID") String serviceInvariantUUID,
            @RequestParam(name = "serviceUUID") String serviceUUID,
            @RequestParam(name = "cloudOwner") String cloudOwner,
            @RequestParam(name = "cloudRegion") String cloudRegion,
            @RequestParam(name = "tenantId") String tenantId,
            @RequestParam(name = "vfUUID") String vfUUID,
            @RequestParam(name = "vfName") String vfName,
            @RequestParam(name = "vfModelUUID") String vfModelUUID,
            @RequestParam(name = "vfModelType") String vfModelType,
            @RequestParam(name = "vfModelName") String vfModelName,
            @RequestParam(name = "vfModelInvariantId") String vfModelInvariantId,
            @RequestParam(name = "vfModelCustomizationId") String vfModelCustomizationId
    ) throws IOException {
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
        log.info("[Runtime][VFModule][Create] " + parameters + " , response:" + data.toString());
        return ResponseEntity.ok(data.toString());
    }

    @DeleteMapping(path = "/runtime/vfModule/{vnfId}/{vfModuleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteVfModule(
            @PathVariable String vnfId,
            @PathVariable String vfModuleId,
            @RequestParam(name = "serviceInstanceId") String serviceInstanceId,
            @RequestParam(name = "vfModelType") String vfModelType,
            @RequestParam(name = "vfModelUUID") String vfModelUUID,
            @RequestParam(name = "vfModelInvariantId") String vfModelInvariantId,
            @RequestParam(name = "cloudOwner") String cloudOwner,
            @RequestParam(name = "cloudRegion") String cloudRegion,
            @RequestParam(name = "tenantId") String tenantId
    ) throws IOException {
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
        log.info("[Runtime][VFModule][Delete] " + parameters + " , response:" + data.toString());
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/runtime/vf-module/{vnfId}/{vfModuleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getVFModuleDetail(@PathVariable(required = true) String vnfId, @PathVariable String vfModuleId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.RUNTIME_VNF_ID.name(), vnfId);
        parameters.put(OnapRequestParameters.RUNTIME_VF_MODULE_ID.name(), vfModuleId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.RUNTIME_VFMODULE_DETAIL, parameters);
        log.info("[Runtime][VFModuleDetail][Get] vnfId:" + vnfId + ",vfModuleId:" + vfModuleId + " , response:" + data.toString());
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/runtime/vf-module-properties/{vfModuleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getVFModuleInstantiationDetail(@PathVariable(required = true) String vfModuleId) throws IOException {
        Map<String, String> parameters = new HashMap<>();

        parameters.put(OnapRequestParameters.RUNTIME_VF_MODULE_ID.name(), vfModuleId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.RUNTIME_VFMODULE_INSTANTIATE_DETAIL, parameters);
        log.info("[Runtime][VFModuleDetail][Get] vfModuleId:" + vfModuleId + " , response:" + data.toString());
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/runtime/vf-module-topology/{serviceInstanceId}/{vnfId}/{vfModuleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getVFModuleTopology(@PathVariable(required = true) String serviceInstanceId, @PathVariable(required = true) String vnfId, @PathVariable(required = true) String vfModuleId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.RUNTIME_SERVICE_INSTANCE_ID.name(), serviceInstanceId);
        parameters.put(OnapRequestParameters.RUNTIME_VNF_ID.name(), vnfId);
        parameters.put(OnapRequestParameters.RUNTIME_VF_MODULE_ID.name(), vfModuleId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.RUNTIME_VFMODULE_TOPOLOGY, parameters);
        log.info("[Runtime][VFModuleDetail][Get] service-instance-id:" + serviceInstanceId + ", vnfId:" + vnfId + " , vfModuleId:" + vfModuleId + " , response:" + data.toString());
        return ResponseEntity.ok(data.toString());
    }

}
