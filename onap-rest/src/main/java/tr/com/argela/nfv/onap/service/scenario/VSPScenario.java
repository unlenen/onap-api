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
import tr.com.argela.nfv.onap.service.model.VSP;
import tr.com.argela.nfv.onap.service.model.Vendor;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@Service
public class VSPScenario extends CommonScenario {

    Logger log = LoggerFactory.getLogger(VSPScenario.class);
    @Autowired
    DesignController designService;

    public void processVSPs(Scenario scenario) throws Exception {
        Vendor vendor = scenario.getVendor();
        for (VSP vsp : vendor.getVsps()) {
            vsp.setVendor(vendor);
            processVSP(scenario, vsp);
        }
    }

    public void processVSP(Scenario scenario, VSP vsp) throws Exception {
        if (vsp.getId() == null) {
            if (vspExists(vsp)) {
                scenario.setScenarioStatus(ScenarioStatus.VSP_FOUND);
                readVspVersion(vsp);
            } else {
                scenario.setScenarioStatus(ScenarioStatus.VSP_CREATING);
                createVSP(vsp);
                scenario.setScenarioStatus(ScenarioStatus.VSP_CREATED);
            }
        }

        if (vsp.getVersionStatus() != EntityStatus.CERTIFIED) {
            uploadFile(vsp);
            scenario.setScenarioStatus(ScenarioStatus.VSP_ARTIFACT_UPLOADED);
            processFile(vsp);
            scenario.setScenarioStatus(ScenarioStatus.VSP_ARTIFACT_PROCESSED);
            submitVsp(vsp);
            scenario.setScenarioStatus(ScenarioStatus.VSP_SUBMIT);
        }
        csarVSP(vsp);
        scenario.setScenarioStatus(ScenarioStatus.VSP_TO_CSAR);
    }

    private boolean vspExists(VSP vsp) throws Exception {
        String root = readResponse(designService.getVSPs());
        Filter vspNameFilter = Filter
                .filter(Criteria.where("name").eq(vsp.getName()).and("vendorName").eq(vsp.getVendor().getName()));
        DocumentContext rootContext = JsonPath.parse(root);
        net.minidev.json.JSONArray vsps = rootContext.read("$['results'][?]", vspNameFilter);
        if (!vsps.isEmpty()) {
            LinkedHashMap<String, String> vendorObj = (LinkedHashMap<String, String>) vsps.get(0);
            vsp.setId(vendorObj.get("id"));
            log.info("[Scenario][VSP][Exists] vspName:" + vsp.getName() + " , vendorName:" + vsp.getVendor().getName()
                    + " , vspId:" + vsp.getId());
        }
        return !vsps.isEmpty();
    }

    private void readVspVersion(VSP vsp) throws Exception {
        String root = readResponse(designService.getVSPVersion(vsp.getId()));
        DocumentContext rootContext = JsonPath.parse(root);

        vsp.setVersionId(rootContext.read("$['results'][0]['id']"));
        vsp.setVersionName(rootContext.read("$['results'][0]['name']"));
        vsp.setVersionStatus(
                EntityStatus.valueOf((rootContext.read("$['results'][0]['status']") + "").toUpperCase(Locale.ENGLISH)));
        log.info("[Scenario][VSP][Exists][FindVersion] vspName:" + vsp.getName() + " , vendorName:"
                + vsp.getVendor().getName() + " , vspId:" + vsp.getId() + " , vspVersionId:" + vsp.getVersionId()
                + ", vspVersionStatus:" + vsp.getVersionStatus());
    }

    private void createVSP(VSP vsp) throws Exception {
        JSONObject root = new JSONObject(readResponse(designService.createVsp(vsp.getVendor().getId(),
                vsp.getVendor().getName(), vsp.getName(), vsp.getDescription())));
        vsp.setId(root.getString("itemId"));
        JSONObject version = root.getJSONObject("version");
        vsp.setVersionId(version.getString("id"));
        vsp.setVersionName(version.getString("name"));
        vsp.setVersionStatus(EntityStatus.valueOf(version.getString("status").toUpperCase(Locale.ENGLISH)));
        log.info("[Scenario][VSP][New] vspName:" + vsp.getName() + " , vendorName:" + vsp.getVendor().getName()
                + " , vspId:" + vsp.getId() + " , vspVersionId:" + vsp.getVersionId() + ", vspVersionStatus:"
                + vsp.getVersionStatus());
    }

    private void uploadFile(VSP vsp) throws Exception {
        JSONObject root = new JSONObject(
                readResponse(designService.uploadVSPFile(vsp.getId(), vsp.getVersionId(), vsp.getFile())));
        log.info("[Scenario][VSP][UploadFile] vspName:" + vsp.getName() + " , vendorName:" + vsp.getVendor().getName()
                + " , vspId:" + vsp.getId() + " , vspVersionId:" + vsp.getVersionId() + ", vspVersionStatus:"
                + vsp.getVersionStatus() + " , vspFile:" + vsp.getFile());
    }

    private void processFile(VSP vsp) throws Exception {
        String json = readResponse(designService.processVSPFile(vsp.getId(), vsp.getVersionId()));
        vsp.setFileWarnings(json);
        log.info("[Scenario][VSP][ProcessFile] vspName:" + vsp.getName() + " , vendorName:" + vsp.getVendor().getName()
                + " , vspId:" + vsp.getId() + " , vspVersionId:" + vsp.getVersionId() + ", vspVersionStatus:"
                + vsp.getVersionStatus() + " , vspFile:" + vsp.getFile() + ", processResult:" + vsp.getFileWarnings());
    }

    private void submitVsp(VSP vsp) throws Exception {
        readResponse(designService.submitVSP(vsp.getId(), vsp.getVersionId()));
        vsp.setVersionStatus(EntityStatus.CERTIFIED);
        log.info("[Scenario][VSP][Submit] vspName:" + vsp.getName() + " , vendorName:" + vsp.getVendor().getName()
                + " , vspId:" + vsp.getId() + " , vspVersionId:" + vsp.getVersionId() + ", vspVersionStatus:"
                + vsp.getVersionStatus());
    }

    private void csarVSP(VSP vsp) throws Exception {
        String data = readResponse(designService.csarVSP(vsp.getId(), vsp.getVersionId()));
        log.info("[Scenario][VSP][CreateCSAR] vspName:" + vsp.getName() + " , vendorName:" + vsp.getVendor().getName()
                + " , vspId:" + vsp.getId() + " , vspVersionId:" + vsp.getVersionId() + ", vspVersionStatus:"
                + vsp.getVersionStatus() + ", result:" + data);
    }
}
