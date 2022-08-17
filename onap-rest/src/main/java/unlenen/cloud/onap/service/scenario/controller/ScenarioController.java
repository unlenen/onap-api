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
package unlenen.cloud.onap.service.scenario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import lombok.extern.slf4j.Slf4j;
import unlenen.cloud.onap.api.client.OnapClient;
import unlenen.cloud.onap.service.model.Scenario;
import unlenen.cloud.onap.service.model.ScenarioError;
import unlenen.cloud.onap.service.scenario.CloudScenario;
import unlenen.cloud.onap.service.scenario.RuntimeScenario;
import unlenen.cloud.onap.service.scenario.ServiceModelScenario;
import unlenen.cloud.onap.service.scenario.SubscriptionScenario;
import unlenen.cloud.onap.service.scenario.VFScenario;
import unlenen.cloud.onap.service.scenario.VSPScenario;
import unlenen.cloud.onap.service.scenario.VendorScenario;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@RestController
@Slf4j
public class ScenarioController {

    @Autowired
    OnapClient adaptor;

    @Autowired
    VendorScenario vendorScenario;

    @Autowired
    VSPScenario vspScenario;

    @Autowired
    VFScenario vfScenario;

    @Autowired
    ServiceModelScenario serviceModelScenario;

    @Autowired
    SubscriptionScenario subscriptionScenario;

    @Autowired
    RuntimeScenario runtimeScenario;

    @Autowired
    CloudScenario cloudScenario;

    ObjectMapper mapper;

    public ScenarioController() {
        mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
    }

    @PostMapping(path = "/scenario/load", consumes = "text/yaml", produces = "text/plain")
    public ResponseEntity<String> loadScenario(@RequestBody String yaml) {
        Scenario scenario = null;
        try {
            scenario = readFromYaml(yaml);

            runScenario(scenario);
        } catch (JsonProcessingException e) {
            scenario = new Scenario();
            scenario.setError(new ScenarioError("YamlParseError", e.getMessage(), e));
        } catch (Exception e) {
            scenario.setError(new ScenarioError(e.getClass().getName(), e.getMessage(), e));
            log.error(e.getMessage()+" ---------- "+scenario, e);
        }
        return ResponseEntity.ok(writeToYaml(scenario));
    }

    private Scenario readFromYaml(String yaml) throws JsonProcessingException {
        Scenario scenario = mapper.readValue(yaml, Scenario.class);
        return scenario;
    }

    private String writeToYaml(Scenario scenario) {
        try {
            return mapper.writeValueAsString(scenario);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private void runScenario(Scenario scenario) throws Exception {
        if (scenario.getProfiles() != null) {
            scenario.mapProfiles(scenario);
        }
        if (scenario.getVendor() == null) {
            log.error("Vendor is null");
            return;
        }
        vendorScenario.processVendor(scenario);
        if (scenario.getVendor().getVsps() == null) {
            log.error("Vsps is null");
            return;
        }
        vspScenario.processVSPs(scenario);
        if (scenario.getService() == null) {
            log.error("Service is null");
            return;
        }

        if (scenario.getCloudRegions() != null) {
            cloudScenario.processCloudRegions(scenario);
        }
        vfScenario.processVFs(scenario);
        scenario.getService().setScenario(scenario);
        serviceModelScenario.processService(scenario);
        if (scenario.getService().getCustomers() == null || scenario.getService().getTenants() == null
                || scenario.getCloudRegions() == null) {
            log.error("Customer or Tenants or CloudRegion is null");
            return;
        }
        subscriptionScenario.processSubscription(scenario);
        if (scenario.getService().getServiceInstances() == null) {
            log.error("Service Instance is null");
            return;
        }
        runtimeScenario.processServiceInstances(scenario);
    }

}
