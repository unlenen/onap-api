/*
# Copyright © 2022 Nebi Volkan UNLENEN
#
# Licensed under the GNU Affero General Public License v3.0
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     https://github.com/unlenen/onap-api/blob/master/LICENSE
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
 */
package unlenen.cloud.onap.service.scenario;

import java.util.LinkedHashMap;
import java.util.Locale;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jayway.jsonpath.Criteria;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;
import unlenen.cloud.onap.service.constant.EntityStatus;
import unlenen.cloud.onap.service.constant.ScenarioStatus;
import unlenen.cloud.onap.service.controller.DesignController;
import unlenen.cloud.onap.service.model.Scenario;
import unlenen.cloud.onap.service.model.Vendor;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@Service
@Slf4j
public class VendorScenario extends CommonScenario {

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
            log.info("[Scenario][Vendor][Exists] " + vendor);
        }
        return !vendors.isEmpty();
    }

    private void readVendorVersion(Vendor vendor) throws Exception {
        String root = readResponse(designService.getVendorVersion(vendor.getId()));
        DocumentContext rootContext = JsonPath.parse(root);

        vendor.setVersionId(rootContext.read("$['results'][0]['id']"));
        vendor.setVersionStatus(
                EntityStatus.valueOf((rootContext.read("$['results'][0]['status']") + "").toUpperCase(Locale.ENGLISH)));
        log.info("[Scenario][Vendor][Exists][FindVersion] " + vendor);
    }

    private void createVendor(Vendor vendor) throws Exception {
        JSONObject root = new JSONObject(
                readResponse(designService.createVendor(vendor.getName(), vendor.getDescription())));
        vendor.setId(root.getString("itemId"));
        JSONObject version = root.getJSONObject("version");
        vendor.setVersionId(version.getString("id"));
        vendor.setVersionStatus(EntityStatus.valueOf(version.getString("status").toUpperCase(Locale.ENGLISH)));
        log.info("[Scenario][Vendor][New] " + vendor);
    }

    private void submitVendor(Vendor vendor) throws Exception {
        readResponse(designService.submitVendor(vendor.getId(), vendor.getVersionId()));
        vendor.setVersionStatus(EntityStatus.CERTIFIED);
        log.info("[Scenario][Vendor][Submit]" + vendor);
    }
}
