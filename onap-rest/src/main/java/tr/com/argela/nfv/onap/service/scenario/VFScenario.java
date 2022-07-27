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
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jayway.jsonpath.Criteria;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;

import tr.com.argela.nfv.onap.service.constant.EntityStatus;
import tr.com.argela.nfv.onap.service.constant.ScenarioStatus;
import tr.com.argela.nfv.onap.service.controller.DesignController;
import tr.com.argela.nfv.onap.service.model.Scenario;
import tr.com.argela.nfv.onap.service.model.VF;
import tr.com.argela.nfv.onap.service.model.VSP;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@org.springframework.stereotype.Service
public class VFScenario extends CommonScenario {

    Logger log = LoggerFactory.getLogger(VFScenario.class);
    @Autowired
    DesignController designService;

    public void processVFs(Scenario scenario) throws Exception {

        for (VF vf : scenario.getService().getVfs()) {
            vf.setService(scenario.getService());
            vf.setVsp(findVsp(scenario.getVendor().getVsps(), vf));
            processVF(scenario, vf);
        }

    }

    private VSP findVsp(List<VSP> vsps, VF vf) throws Exception {
        for (VSP vsp : vsps) {
            if (vf.getVsp().getName().equals(vsp.getName())) {
                return vsp;
            }
        }
        throw new Exception("VF vsp name is not valid . vfName: " + vf.getName() + " , vspName:" + vf.getVsp().getName()
                + ", vspList:" + vsps.size());
    }

    private void processVF(Scenario scenario, VF vf) throws Exception {
        if (vfExists(vf)) {
            readVFUniqueId(vf);
            scenario.setScenarioStatus(ScenarioStatus.SERVICE_VF_FOUND, vf + "");
        } else {
            createVF(vf);
            scenario.setScenarioStatus(ScenarioStatus.SERVICE_VF_CREATED, vf + "");
        }

        if (vf.getVersionStatus() != EntityStatus.CERTIFIED) {
            if (vf.getVersionStatus() == EntityStatus.NOT_CERTIFIED_CHECKOUT) {
                checkInVf(vf);
                scenario.setScenarioStatus(ScenarioStatus.SERVICE_VF_CHECKIN, vf + "");
            }
            certifyVf(vf);
            scenario.setScenarioStatus(ScenarioStatus.SERVICE_VF_CERTIFIED, vf + "");
        }

    }

    private boolean vfExists(VF vf) throws Exception {
        String root = readResponseValidateOption(designService.getVFs(), false);
        Filter vfName = Filter.filter(Criteria.where("name").eq(vf.getName()));
        DocumentContext rootContext = JsonPath.parse(root);
        net.minidev.json.JSONArray vfs = rootContext.read("$[?]", vfName);
        if (!vfs.isEmpty()) {
            LinkedHashMap<String, String> vfObject = (LinkedHashMap<String, String>) vfs.get(0);
            vf.setUuid(vfObject.get("uuid"));
            vf.setInvariantUUID(vfObject.get("invariantUUID"));
            vf.setVersionStatus(EntityStatus.valueOf(vfObject.get("lifecycleState").toUpperCase(Locale.ENGLISH)));
            log.info("[Scenario][VF][Exists] vf:" + vf.getName() + " , uuid : " + vf.getUuid() + " , invariantUUID :"
                    + vf.getInvariantUUID() + " , vfStatus:" + vf.getVersionStatus());
        }
        return !vfs.isEmpty();
    }

    private void createVF(VF vf) throws Exception {
        JSONObject root = new JSONObject(readResponse(designService.createVF(vf.getVsp().getVendor().getName(),
                vf.getVsp().getId(), vf.getVsp().getVersionName(), vf.getName(), vf.getDescription())));
        vf.setInvariantUUID(root.getString("invariantUUID"));
        vf.setUniqueId(root.getString("uniqueId"));
        vf.setUuid(root.getString("uuid"));
        vf.setVersionStatus(EntityStatus.valueOf(root.getString("lifecycleState").toUpperCase(Locale.ENGLISH)));
        log.info("[Scenario][VF][New] vf:" + vf.getName() + " , uuid : " + vf.getUuid() + " , invariantUUID :"
                + vf.getInvariantUUID() + " , vfStatus:" + vf.getVersionStatus());
    }

    protected void readVFUniqueId(VF vf) throws Exception {
        JSONObject root = new JSONObject(readResponse(designService.getVFUniqueId(vf.getUuid())));
        vf.setUniqueId(root.getString("uniqueId"));
        vf.setVersionName(root.getString("version"));
        vf.setVersionStatus(EntityStatus.valueOf(root.getString("lifecycleState").toUpperCase(Locale.ENGLISH)));
        log.info("[Scenario][VF][Exists][UniqueId] vf:" + vf.getName() + " , uuid : " + vf.getUuid()
                + " , invariantUUID :" + vf.getInvariantUUID() + " , uniqueId:" + vf.getUniqueId() + " , version:"
                + vf.getVersionName());
    }

    private void checkInVf(VF vf) throws Exception {
        readResponse(designService.checkInVF(vf.getUuid()));
        log.info("[Scenario][VF][CheckIn] vf:" + vf.getName() + " , uuid : " + vf.getUuid() + " , invariantUUID :"
                + vf.getInvariantUUID() + " , uniqueId:" + vf.getUniqueId());
    }

    private void certifyVf(VF vf) throws Exception {
        String data = readResponse(designService.certifyVF(vf.getUniqueId()));
        vf.setVersionStatus(EntityStatus.CERTIFIED);
        log.info("[Scenario][VF][Certify] vf:" + vf.getName() + " , uuid : " + vf.getUuid() + " , invariantUUID :"
                + vf.getInvariantUUID() + " , uniqueId:" + vf.getUniqueId() + ",vfStatus:" + vf.getVersionStatus());
        readVFUniqueId(vf);
    }

}
