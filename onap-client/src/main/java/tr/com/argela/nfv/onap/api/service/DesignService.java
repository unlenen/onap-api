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
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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

public class DesignService {

    @Autowired
    OnapClient adaptor;

    Logger log = LoggerFactory.getLogger(DesignService.class);

    public JSONObject getVendors() throws IOException {
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VENDORS);
        if (log.isInfoEnabled())
            log.info("[Design][Vendors][Get] size:" + adaptor.getResponseSize(data, "results"));
        return data;
    }

    public JSONObject createVendor(String vendorName,
            @PathVariable(required = false) String vendorDescription) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VENDOR_NAME.name(), vendorName);
        parameters.put(OnapRequestParameters.DESIGN_VENDOR_DESC.name(), vendorDescription);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VENDOR_CREATE, parameters);
        if (log.isInfoEnabled())
            log.info("[Design][Vendor][Create] " + parameters + ", response --> id:"
                    + adaptor.getResponseItem(data, "itemId"));
        return data;
    }

    public JSONObject getVendorVersion(String vendorId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VENDOR_ID.name(), vendorId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VENDOR_VERSION, parameters);

        if (log.isInfoEnabled())
            log.info("[Design][Vendor][Version] " + parameters + ", response : " + data);
        return data;
    }

    public JSONObject submitVendor(String vendorId, String vendorVersionId)
            throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VENDOR_ID.name(), vendorId);
        parameters.put(OnapRequestParameters.DESIGN_VENDOR_VERSION_ID.name(), vendorVersionId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VENDOR_SUBMIT, parameters);
        if (log.isInfoEnabled())
            log.info("[Design][Vendor][Submit] " + parameters);
        return data;
    }

    public JSONObject getVSPs() throws IOException {
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VSPS);
        if (log.isInfoEnabled())
            log.info("[Design][VSPs][Get] response --> size:" + adaptor.getResponseSize(data, "results"));
        return data;
    }

    public JSONObject getVSPVersion(String vspId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VSP_ID.name(), vspId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VSP_VERSION, parameters);
        if (log.isInfoEnabled())
            log.info("[Design][VSP-Version][Get]" + parameters + ", response --> size:"
                    + adaptor.getResponseSize(data, "results"));
        return data;
    }

    public JSONObject createVsp(String vendorId, String vendorName,
            String vspName, String vspDescription) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VENDOR_ID.name(), vendorId);
        parameters.put(OnapRequestParameters.DESIGN_VENDOR_NAME.name(), vendorName);
        parameters.put(OnapRequestParameters.DESIGN_VSP_NAME.name(), vspName);
        parameters.put(OnapRequestParameters.DESIGN_VSP_DESC.name(), vspDescription);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VSP_CREATE, parameters);
        if (log.isInfoEnabled())
            log.info("[Design][Vsp][Create] " + parameters + " , response --> id:"
                    + adaptor.getResponseItem(data, "itemId"));
        return data;
    }

    public JSONObject uploadVSPFile(String vspId, String vspVersionId,
            @RequestParam(name = "vspFileLocalPath") String vspFileLocalPath) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VSP_ID.name(), vspId);
        parameters.put(OnapRequestParameters.DESIGN_VSP_VERSION_ID.name(), vspVersionId);

        Map<String, Object> files = new HashMap<>();
        files.put("upload", new File(vspFileLocalPath));

        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VSP_UPLOAD_FILE, parameters, files);
        if (log.isInfoEnabled())
            log.info("[Design][Vsp][FileUpload] " + parameters + " ,response:" + data);
        return data;
    }

    public JSONObject processVSPFile(String vspId, String vspVersionId)
            throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VSP_ID.name(), vspId);
        parameters.put(OnapRequestParameters.DESIGN_VSP_VERSION_ID.name(), vspVersionId);

        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VSP_PROCESS_FILE, parameters);
        if (log.isInfoEnabled())
            log.info("[Design][Vsp][FileProcess] " + parameters + " ,response:" + data);
        return data;
    }

    public JSONObject commitVSP(String vspId, String vspVersionId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VSP_ID.name(), vspId);
        parameters.put(OnapRequestParameters.DESIGN_VSP_VERSION_ID.name(), vspVersionId);

        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VSP_COMMIT, parameters);
        if (log.isInfoEnabled())
            log.info("[Design][Vsp][Commit] " + parameters + ",response:" + data);
        return data;
    }

    public JSONObject submitVSP(String vspId, String vspVersionId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VSP_ID.name(), vspId);
        parameters.put(OnapRequestParameters.DESIGN_VSP_VERSION_ID.name(), vspVersionId);

        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VSP_SUBMIT, parameters);
        if (log.isInfoEnabled())
            log.info("[Design][Vsp][Submit] " + parameters + ",response:" + data);
        return data;
    }

    public JSONObject csarVSP(String vspId, String vspVersionId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VSP_ID.name(), vspId);
        parameters.put(OnapRequestParameters.DESIGN_VSP_VERSION_ID.name(), vspVersionId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VSP_CSAR, parameters);
        if (log.isInfoEnabled())
            log.info("[Design][Vsp][CSAR] " + parameters + " ,response:" + data);
        return data;
    }

    public JSONArray getVFs() throws IOException {
        JSONArray data = (JSONArray) adaptor.call(OnapRequest.SDC_VFS);
        if (log.isInfoEnabled())
            log.info("[Design][VFs][Get] size:" + data.length());
        return data;
    }

    public JSONObject createVF(String vendorName, String vspId, String vspVersionName, String vfName,
            String vfDescription) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VENDOR_NAME.name(), vendorName);
        parameters.put(OnapRequestParameters.DESIGN_VSP_ID.name(), vspId);
        parameters.put(OnapRequestParameters.DESIGN_VSP_VERSION_NAME.name(), vspVersionName);
        parameters.put(OnapRequestParameters.DESIGN_VF_NAME.name(), vfName);
        parameters.put(OnapRequestParameters.DESIGN_VF_DESCRIPTION.name(), vfDescription);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VF_CREATE, parameters);
        if (log.isInfoEnabled())
            log.info("[Design][VF][Create] " + parameters + " ,response-->    invariantUUID :"
                    + adaptor.getResponseItem(data, "invariantUUID")
                    + ", uuid:" + adaptor.getResponseItem(data, "uuid")
                    + ", uniqueId:" + adaptor.getResponseItem(data, "uniqueId"));
        return data;
    }

    public JSONObject getVFUniqueId(String vfUUID) throws IOException {
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
        if (log.isInfoEnabled())
            log.info("[Design][VF][UniqueId] " + parameters + " ,response:" + response);
        return response;
    }

    public JSONObject checkInVF(String vfUUID) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VF_UUID.name(), vfUUID);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VF_CHECKIN, parameters);
        if (log.isInfoEnabled())
            log.info("[Design][VF][CheckIn] " + parameters + " ,response:" + data);
        return data;
    }

    public JSONObject certifyVF(String vfUniqueId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_VF_UNIQUE_ID.name(), vfUniqueId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_VF_CERTIFY, parameters);
        if (log.isInfoEnabled())
            log.info("[Design][VF][Certify] " + parameters + " ,response:" + data);
        return data;
    }

    public JSONArray getServiceModels() throws IOException {
        JSONArray data = (JSONArray) adaptor.call(OnapRequest.SDC_SERVICE_MODELS);
        if (log.isInfoEnabled())
            log.info("[Design][ServiceModels][Get] size:" + data.length());
        return data;
    }

    public JSONObject createServiceModel(String serviceName, String serviceDescription)
            throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_NAME.name(), serviceName);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_DESCRIPTION.name(), serviceDescription);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_SERVICE_MODEL_CREATE, parameters);
        if (log.isInfoEnabled())
            log.info("[Design][ServiceModel][Create] " + parameters + " ,response-->    invariantUUID :"
                    + adaptor.getResponseItem(data, "invariantUUID")
                    + ", uuid:" + adaptor.getResponseItem(data, "uuid")
                    + ", uniqueId:" + adaptor.getResponseItem(data, "uniqueId"));
        return data;
    }

    public JSONObject getServiceModelUniqueId(String serviceUUID) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_UNIQUE_ID.name(), serviceUUID);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_UNIQUE_ID, parameters);

        Filter serviceUUIDFilter = Filter
                .filter(Criteria.where("uuid").eq(serviceUUID).and("isHighestVersion").eq(Boolean.TRUE));
        DocumentContext rootContext = JsonPath.parse(data.toString());
        net.minidev.json.JSONArray services = rootContext.read("$['services'][?]", serviceUUIDFilter);
        JSONObject response = new JSONObject();
        if (!services.isEmpty()) {
            LinkedHashMap<String, String> serviceData = (LinkedHashMap<String, String>) services.get(0);
            for (String key : serviceData.keySet()) {
                response.put(key, serviceData.get(key));
            }

        }
        if (log.isInfoEnabled())
            log.info("[Design][Service][UniqueId] " + parameters + " ,response:" + response);
        return response;
    }

    public JSONObject addVFtoServiceModel(String serviceUniqueId, String vfUniqueId,
            String vfName, @RequestParam(name = "index", defaultValue = "1") int index)
            throws IOException {
        Map<String, String> parameters = new HashMap<>();

        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_UNIQUE_ID.name(), serviceUniqueId);
        parameters.put(OnapRequestParameters.DESIGN_VF_UNIQUE_ID.name(), vfUniqueId);
        parameters.put(OnapRequestParameters.DESIGN_VF_NAME.name(), vfName);
        parameters.put(OnapRequestParameters.DESIGN_VF_POSX.name(), (100 * index) + "");
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_SERVICE_MODEL_ADD_VF, parameters);
        if (log.isInfoEnabled())
            log.info("[Design][ServiceModel][AddVF] " + parameters + " ,response-->    name :"
                    + adaptor.getResponseItem(data, "name")
                    + ", customizationUUID:" + adaptor.getResponseItem(data, "customizationUUID"));
        return data;
    }

    public JSONObject certifyServiceModel(String serviceUniqueId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_UNIQUE_ID.name(), serviceUniqueId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_SERVICE_MODEL_CERTIFY, parameters);
        if (log.isInfoEnabled())
            log.info("[Design][ServiceModel][Certify] " + parameters + " ,response:" + data);
        return data;
    }

    public JSONObject distributeServiceModel(String serviceUniqueId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_UNIQUE_ID.name(), serviceUniqueId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_SERVICE_MODEL_DISTRIBUTE, parameters);
        if (log.isInfoEnabled())
            log.info("[Design][ServiceModel][Distribute] " + parameters);
        return data;
    }

    public JSONObject getServiceModelDistributions(String serviceUUID) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_UUID.name(), serviceUUID);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_SERVICE_MODEL_DISTRIBUTE_LIST, parameters);
        if (log.isInfoEnabled())
            log.info("[Design][ServiceModel][DistributionList] " + parameters + " , size:"
                    + adaptor.getResponseSize(data, "distributionStatusOfServiceList"));
        return data;
    }

    public JSONObject getServiceModelDistributionDetail(String distributionId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_DISTRIBUTION_ID.name(), distributionId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_SERVICE_MODEL_DISTRIBUTE_DETAIL, parameters);
        if (log.isInfoEnabled())
            log.info("[Design][ServiceModel][DistributionDetail] " + parameters + " , size:"
                    + adaptor.getResponseSize(data, "distributionStatusList"));
        return data;
    }

    public JSONObject getServiceModelDetail(String serviceUniqueId, String filter) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_UNIQUE_ID.name(), serviceUniqueId);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_FILTER.name(), filter);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.SDC_SERVICE_MODEL_DETAIL, parameters);
        if (log.isInfoEnabled())
            log.info("[Design][ServiceModel][Detail] " + parameters);
        return data;
    }

}
