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
package  tr.com.argela.nfv.onap.service.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tr.com.argela.nfv.onap.service.constant.ScenarioStatus;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@Getter
@Setter
@ToString
public class Scenario {

    ScenarioStatus scenarioStatus = ScenarioStatus.INIT;
    Vendor vendor;
    Service service;
    List<CloudRegion> cloudRegions;
    List<VFModuleProfile> profiles;

    @JsonIgnore
    Map<String, Tenant> tenantMapById = new HashMap();
    @JsonIgnore
    Map<String, VFModuleProfile> profileMapByName = new HashMap();
    List<ScenarioStatus> actionHistory= new ArrayList<>();

    public ScenarioStatus getScenarioStatus() {
        return this.scenarioStatus;
    }

    public void setScenarioStatus(ScenarioStatus scenarioStatus) {
        this.scenarioStatus = scenarioStatus;
        actionHistory.add(scenarioStatus);
    }

    public void mapTenants(Scenario scenario) {
        if (scenario.getCloudRegions() != null) {
            for (CloudRegion cloudRegion : scenario.getCloudRegions()) {
                for (Tenant tenant : cloudRegion.getTenants()) {
                    tenant.setCloudRegion(cloudRegion);
                    tenantMapById.put(tenant.getId(), tenant);
                }
            }
        }
    }

    public void mapProfiles(Scenario scenario) {
        scenario.setScenarioStatus(ScenarioStatus.PROFILE_PARSING);
        if (scenario.getProfileMapByName() != null) {
            for (VFModuleProfile vfmp : scenario.getProfiles()) {
                profileMapByName.put(vfmp.getName(), vfmp);
            }
        }
        scenario.setScenarioStatus(ScenarioStatus.PROFILE_PARSED);
    }

}
