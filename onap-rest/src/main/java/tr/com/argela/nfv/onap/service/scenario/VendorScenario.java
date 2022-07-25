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
package tr.com.argela.nfv.onap.service.scenario;

import java.util.LinkedHashMap;
import java.util.Locale;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jayway.jsonpath.Criteria;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;

import tr.com.argela.nfv.onap.service.constant.EntityStatus;
import tr.com.argela.nfv.onap.service.constant.ScenarioStatus;
import tr.com.argela.nfv.onap.service.controller.DesignController;
import tr.com.argela.nfv.onap.service.model.Scenario;
import tr.com.argela.nfv.onap.service.model.Vendor;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@Service
public class VendorScenario extends CommonScenario {

    Logger log = LoggerFactory.getLogger(VendorScenario.class);
    @Autowired
    DesignController designService;

    public void processVendor(Scenario scenario) throws Exception {
        Vendor vendor = scenario.getVendor();
        if (vendor.getId() == null) {
            if (vendorExists(vendor)) {
                scenario.setScenarioStatus(ScenarioStatus.VENDOR_FOUND);
                readVendorVersion(vendor);
            } else {
                scenario.setScenarioStatus(ScenarioStatus.VENDOR_CREATING);
                createVendor(vendor);
                scenario.setScenarioStatus(ScenarioStatus.VENDOR_CREATED);
            }
        }

        if (vendor.getVersionStatus() != EntityStatus.CERTIFIED) {
            submitVendor(vendor);
            scenario.setScenarioStatus(ScenarioStatus.VENDOR_SUBMITTED);
        }
    }

    private boolean vendorExists(Vendor vendor) throws Exception {
        String root = readResponse(designService.getVendors());
        Filter vendorNameFilter = Filter.filter(Criteria.where("name").eq(vendor.getName()));
        DocumentContext rootContext = JsonPath.parse(root);
        net.minidev.json.JSONArray vendors = rootContext.read("$['results'][?]", vendorNameFilter);
        if (!vendors.isEmpty()) {
            LinkedHashMap<String, String> vendorObj = (LinkedHashMap<String, String>) vendors.get(0);
            vendor.setId(vendorObj.get("id"));
            log.info("[Scenario][Vendor][Exists] name:" + vendor.getName() + " , id : " + vendor.getId());
        }
        return !vendors.isEmpty();
    }

    private void readVendorVersion(Vendor vendor) throws Exception {
        String root = readResponse(designService.getVendorVersion(vendor.getId()));
        DocumentContext rootContext = JsonPath.parse(root);

        vendor.setVersionId(rootContext.read("$['results'][0]['id']"));
        vendor.setVersionStatus(
                EntityStatus.valueOf((rootContext.read("$['results'][0]['status']") + "").toUpperCase(Locale.ENGLISH)));
        log.info("[Scenario][Vendor][Exists][FindVersion] name:" + vendor.getName() + " , id : " + vendor.getId()
                + " , versionId:" + vendor.getVersionId() + ", versionStatus:" + vendor.getVersionStatus());
    }

    private void createVendor(Vendor vendor) throws Exception {
        JSONObject root = new JSONObject(
                readResponse(designService.createVendor(vendor.getName(), vendor.getDescription())));
        vendor.setId(root.getString("itemId"));
        JSONObject version = root.getJSONObject("version");
        vendor.setVersionId(version.getString("id"));
        vendor.setVersionStatus(EntityStatus.valueOf(version.getString("status").toUpperCase(Locale.ENGLISH)));
        log.info("[Scenario][Vendor][New] name:" + vendor.getName() + " , id : " + vendor.getId() + " , versionId:"
                + vendor.getVersionId() + ", versionStatus:" + vendor.getVersionStatus());
    }

    private void submitVendor(Vendor vendor) throws Exception {
        readResponse(designService.submitVendor(vendor.getId(), vendor.getVersionId()));
        vendor.setVersionStatus(EntityStatus.CERTIFIED);
        log.info("[Scenario][Vendor][Submit] name:" + vendor.getName() + " , id : " + vendor.getId() + " , versionId:"
                + vendor.getVersionId() + ", versionStatus:" + vendor.getVersionStatus());
    }
}
