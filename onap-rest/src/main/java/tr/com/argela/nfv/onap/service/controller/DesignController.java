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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jayway.jsonpath.Criteria;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;

import tr.com.argela.nfv.onap.api.client.OnapClient;
import tr.com.argela.nfv.onap.api.client.model.OnapRequest;
import tr.com.argela.nfv.onap.api.client.model.OnapRequestParameters;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@RestController

public class DesignController {

    @Autowired
    OnapClient adaptor;

    Logger log = LoggerFactory.getLogger(DesignController.class);

    @GetMapping(path = "/design/vendors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getVendors() throws IOException {
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VENDORS);
        log.info("[Design][Vendors][Get] size:" + adaptor.getResponseSize(data, "results"));
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/design/vendor/{vendorName}/{vendorDescription}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createVendor(@PathVariable String vendorName, @PathVariable(required = false) String vendorDescription) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VENDOR_NAME.name(), vendorName);
        parameters.put(OnapRequestParameters.DESIGN_VENDOR_DESC.name(), vendorDescription);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VENDOR_CREATE, parameters);
        log.info("[Design][Vendor][Create] " + parameters + ", response --> id:" + adaptor.getResponseItem(data, "itemId"));
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/design/vendor-version/{vendorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getVendorVersion(@PathVariable String vendorId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VENDOR_ID.name(), vendorId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VENDOR_VERSION, parameters);

        log.info("[Design][Vendor][Version] " + parameters + ", response : " + data);
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/design/vendor-submit/{vendorId}/{vendorVersionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity submitVendor(@PathVariable String vendorId, @PathVariable String vendorVersionId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VENDOR_ID.name(), vendorId);
        parameters.put(OnapRequestParameters.DESIGN_VENDOR_VERSION_ID.name(), vendorVersionId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VENDOR_SUBMIT, parameters);
        log.info("[Design][Vendor][Submit] " + parameters);
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/design/vsps", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getVSPs() throws IOException {
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VSPS);
        log.info("[Design][VSPs][Get] response --> size:" + adaptor.getResponseSize(data, "results"));
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/design/vsp-versions/{vspId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getVSPVersion(@PathVariable String vspId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VSP_ID.name(), vspId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VSP_VERSION, parameters);
        log.info("[Design][VSP-Version][Get]" + parameters + ", response --> size:" + adaptor.getResponseSize(data, "results"));
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/design/vsp/{vendorId}/{vendorName}/{vspName}/{vspDescription}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createVsp(@PathVariable String vendorId, @PathVariable String vendorName, @PathVariable String vspName, @PathVariable String vspDescription) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VENDOR_ID.name(), vendorId);
        parameters.put(OnapRequestParameters.DESIGN_VENDOR_NAME.name(), vendorName);
        parameters.put(OnapRequestParameters.DESIGN_VSP_NAME.name(), vspName);
        parameters.put(OnapRequestParameters.DESIGN_VSP_DESC.name(), vspDescription);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VSP_CREATE, parameters);
        log.info("[Design][Vsp][Create] " + parameters + " , response --> id:" + adaptor.getResponseItem(data, "itemId"));
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/design/vsp-file-upload/{vspId}/{vspVersionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity uploadVSPFile(@PathVariable String vspId, @PathVariable String vspVersionId, @RequestParam(name = "vspFileLocalPath") String vspFileLocalPath) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VSP_ID.name(), vspId);
        parameters.put(OnapRequestParameters.DESIGN_VSP_VERSION_ID.name(), vspVersionId);

        Map<String, Object> files = new HashMap<>();
        files.put("upload", new File(vspFileLocalPath));

        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VSP_UPLOAD_FILE, parameters, files);
        log.info("[Design][Vsp][FileUpload] " + parameters + " ,response:" + data);
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/design/vsp-file-process/{vspId}/{vspVersionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity processVSPFile(@PathVariable String vspId, @PathVariable String vspVersionId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VSP_ID.name(), vspId);
        parameters.put(OnapRequestParameters.DESIGN_VSP_VERSION_ID.name(), vspVersionId);

        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VSP_PROCESS_FILE, parameters);
        log.info("[Design][Vsp][FileProcess] " + parameters + " ,response:" + data);
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/design/vsp-commit/{vspId}/{vspVersionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity commitVSP(@PathVariable String vspId, @PathVariable String vspVersionId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VSP_ID.name(), vspId);
        parameters.put(OnapRequestParameters.DESIGN_VSP_VERSION_ID.name(), vspVersionId);

        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VSP_COMMIT, parameters);
        log.info("[Design][Vsp][Commit] " + parameters + ",response:" + data);
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/design/vsp-submit/{vspId}/{vspVersionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity submitVSP(@PathVariable String vspId, @PathVariable String vspVersionId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VSP_ID.name(), vspId);
        parameters.put(OnapRequestParameters.DESIGN_VSP_VERSION_ID.name(), vspVersionId);

        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VSP_SUBMIT, parameters);
        log.info("[Design][Vsp][Submit] " + parameters + ",response:" + data);
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/design/vsp-csar/{vspId}/{vspVersionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity csarVSP(@PathVariable String vspId, @PathVariable String vspVersionId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VSP_ID.name(), vspId);
        parameters.put(OnapRequestParameters.DESIGN_VSP_VERSION_ID.name(), vspVersionId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VSP_CSAR, parameters);
        log.info("[Design][Vsp][CSAR] " + parameters + " ,response:" + data);
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/design/vfs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getVFs() throws IOException {
        JSONArray data = (JSONArray) adaptor.call(OnapRequest.SDC_VFS);
        log.info("[Design][VFs][Get] size:" + data.length());
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/design/vf/{vendorName}/{vspId}/{vspVersionName}/{vfName}/{vfDescription}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createVF(@PathVariable String vendorName, String vspId, String vspVersionName, String vfName, String vfDescription) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VENDOR_NAME.name(), vendorName);
        parameters.put(OnapRequestParameters.DESIGN_VSP_ID.name(), vspId);
        parameters.put(OnapRequestParameters.DESIGN_VSP_VERSION_NAME.name(), vspVersionName);
        parameters.put(OnapRequestParameters.DESIGN_VF_NAME.name(), vfName);
        parameters.put(OnapRequestParameters.DESIGN_VF_DESCRIPTION.name(), vfDescription);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VF_CREATE, parameters);
        log.info("[Design][VF][Create] " + parameters + " ,response-->    invariantUUID :" + adaptor.getResponseItem(data, "invariantUUID")
                + ", uuid:" + adaptor.getResponseItem(data, "uuid")
                + ", uniqueId:" + adaptor.getResponseItem(data, "uniqueId"));
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/design/vf-uniqueId/{vfUUID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getVFUniqueId(@PathVariable String vfUUID) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VF_UUID.name(), vfUUID);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_UNIQUE_ID, parameters);

        Filter vfUUIDFilter = Filter.filter(Criteria.where("uuid").eq(vfUUID).and("isHighestVersion").eq(Boolean.TRUE));
        DocumentContext rootContext = JsonPath.parse(data.toString());
        net.minidev.json.JSONArray vfs = rootContext.read("$['resources'][?]", vfUUIDFilter);
        JSONObject response = new JSONObject();
        if (!vfs.isEmpty()) {
            LinkedHashMap<String, String> vfData = (LinkedHashMap<String, String>) vfs.get(0);
            for (String key : vfData.keySet()) {
                response.put(key, vfData.get(key));
            }

        }
        log.info("[Design][VF][UniqueId] " + parameters + " ,response:" + response);
        return ResponseEntity.ok(response.toString());
    }

    @PutMapping(path = "/design/vf-checkIn/{vfUUID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity checkInVF(@PathVariable String vfUUID) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VF_UUID.name(), vfUUID);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VF_CHECKIN, parameters);
        log.info("[Design][VF][CheckIn] " + parameters + " ,response:" + data);
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/design/vf-certify/{vfUniqueId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity certifyVF(@PathVariable String vfUniqueId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VF_UNIQUE_ID.name(), vfUniqueId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VF_CERTIFY, parameters);
        log.info("[Design][VF][Certify] " + parameters + " ,response:" + data);
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/design/service-models", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getServiceModels() throws IOException {
        JSONArray data = (JSONArray) adaptor.call(OnapRequest.SDC_SERVICE_MODELS);
        log.info("[Design][ServiceModels][Get] size:" + data.length());
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/design/service-model/{serviceName}/{serviceDescription}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createServiceModel(@PathVariable String serviceName, @PathVariable String serviceDescription) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_NAME.name(), serviceName);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_DESCRIPTION.name(), serviceDescription);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_SERVICE_MODEL_CREATE, parameters);
        log.info("[Design][ServiceModel][Create] " + parameters + " ,response-->    invariantUUID :" + adaptor.getResponseItem(data, "invariantUUID")
                + ", uuid:" + adaptor.getResponseItem(data, "uuid")
                + ", uniqueId:" + adaptor.getResponseItem(data, "uniqueId"));
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/design/service-uniqueId/{serviceUUID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getServiceModelUniqueId(@PathVariable String serviceUUID) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_UNIQUE_ID.name(), serviceUUID);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_UNIQUE_ID, parameters);

        Filter serviceUUIDFilter = Filter.filter(Criteria.where("uuid").eq(serviceUUID).and("isHighestVersion").eq(Boolean.TRUE));
        DocumentContext rootContext = JsonPath.parse(data.toString());
        net.minidev.json.JSONArray services = rootContext.read("$['services'][?]", serviceUUIDFilter);
        JSONObject response = new JSONObject();
        if (!services.isEmpty()) {
            LinkedHashMap<String, String> serviceData = (LinkedHashMap<String, String>) services.get(0);
            for (String key : serviceData.keySet()) {
                response.put(key, serviceData.get(key));
            }

        }
        log.info("[Design][Service][UniqueId] " + parameters + " ,response:" + response);
        return ResponseEntity.ok(response.toString());
    }

    @PutMapping(path = "/design/service-model/{serviceUniqueId}/{vfUniqueId}/{vfName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addVFtoServiceModel(@PathVariable String serviceUniqueId, @PathVariable String vfUniqueId, @PathVariable String vfName, @RequestParam(name = "index", defaultValue = "1") int index) throws IOException {
        Map<String, String> parameters = new HashMap<>();

        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_UNIQUE_ID.name(), serviceUniqueId);
        parameters.put(OnapRequestParameters.DESIGN_VF_UNIQUE_ID.name(), vfUniqueId);
        parameters.put(OnapRequestParameters.DESIGN_VF_NAME.name(), vfName);
        parameters.put(OnapRequestParameters.DESIGN_VF_POSX.name(), (100 * index) + "");
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_SERVICE_MODEL_ADD_VF, parameters);
        log.info("[Design][ServiceModel][AddVF] " + parameters + " ,response-->    name :" + adaptor.getResponseItem(data, "name")
                + ", customizationUUID:" + adaptor.getResponseItem(data, "customizationUUID"));
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/design/service-model-certify/{serviceUniqueId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity certifyServiceModel(@PathVariable String serviceUniqueId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_UNIQUE_ID.name(), serviceUniqueId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_SERVICE_MODEL_CERTIFY, parameters);
        log.info("[Design][ServiceModel][Certify] " + parameters + " ,response:" + data);
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/design/service-model-distribute/{serviceUniqueId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity distributeServiceModel(@PathVariable String serviceUniqueId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_UNIQUE_ID.name(), serviceUniqueId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_SERVICE_MODEL_DISTRIBUTE, parameters);
        log.info("[Design][ServiceModel][Distribute] " + parameters);
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/design/service-model-distribution/{serviceUUID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getServiceModelDistributions(@PathVariable String serviceUUID) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_UUID.name(), serviceUUID);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_SERVICE_MODEL_DISTRIBUTE_LIST, parameters);
        log.info("[Design][ServiceModel][DistributionList] " + parameters + " , size:" + adaptor.getResponseSize(data, "distributionStatusOfServiceList"));
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/design/service-model-distribution-detail/{distributionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getServiceModelDistributionDetail(@PathVariable String distributionId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_DISTRIBUTION_ID.name(), distributionId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_SERVICE_MODEL_DISTRIBUTE_DETAIL, parameters);
        log.info("[Design][ServiceModel][DistributionDetail] " + parameters + " , size:" + adaptor.getResponseSize(data, "distributionStatusList"));
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/design/service-model/{serviceUniqueId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getServiceModelDetail(@PathVariable String serviceUniqueId, @RequestParam(name = "filter", required = false) String filter) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_UNIQUE_ID.name(), serviceUniqueId);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_FILTER.name(), filter);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_SERVICE_MODEL_DETAIL, parameters);
        log.info("[Design][ServiceModel][Detail] " + parameters);
        return ResponseEntity.ok(data.toString());
    }

}
