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
package unlenen.cloud.onap.service.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import unlenen.cloud.onap.api.client.model.VFModuleProfile;
import unlenen.cloud.onap.service.constant.ScenarioStatus;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@Getter
@Setter
@ToString
public class Scenario {

    ScenarioStatus scenarioStatus = ScenarioStatus.INIT;
    ScenarioError error;
    Vendor vendor;
    Service service;
    List<CloudRegion> cloudRegions;
    List<VFModuleProfile> profiles;

    @JsonIgnore
    Map<String, Tenant> tenantMapById = new HashMap();
    @JsonIgnore
    Map<String, VFModuleProfile> profileMapByName = new HashMap();
    List<String> actionHistory = new ArrayList<>();

    public ScenarioStatus getScenarioStatus() {
        return this.scenarioStatus;
    }

    public void setScenarioStatus(ScenarioStatus scenarioStatus) {
        setScenarioStatus(scenarioStatus, null);
    }

    public void setScenarioStatus(ScenarioStatus scenarioStatus, String message) {
        this.scenarioStatus = scenarioStatus;
        String messageData = (message != null) ? scenarioStatus + " --> " + message : scenarioStatus.name();
        actionHistory.add(messageData);
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
