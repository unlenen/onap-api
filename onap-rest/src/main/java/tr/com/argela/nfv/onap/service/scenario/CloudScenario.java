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

import java.io.IOException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import tr.com.argela.nfv.onap.service.constant.ScenarioStatus;
import tr.com.argela.nfv.onap.service.controller.CloudController;
import tr.com.argela.nfv.onap.service.model.AvailabilityZone;
import tr.com.argela.nfv.onap.service.model.CloudRegion;
import tr.com.argela.nfv.onap.service.model.Scenario;
import tr.com.argela.nfv.onap.service.model.Tenant;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@org.springframework.stereotype.Service
public class CloudScenario extends CommonScenario {

    Logger log = LoggerFactory.getLogger(CloudScenario.class);

    @Autowired
    CloudController cloudService;

    public void processCloudRegions(Scenario scenario) throws Exception {
        for (CloudRegion cloudRegion : scenario.getCloudRegions()) {
            if (!checkComplexExists(cloudRegion.getComplexName())) {
                scenario.setScenarioStatus(ScenarioStatus.CLOUD_COMPLEX_CREATING, cloudRegion.getComplexName() + "");
                createComplex(cloudRegion.getComplexName());
                scenario.setScenarioStatus(ScenarioStatus.CLOUD_COMPLEX_CREATED, cloudRegion.getComplexName() + "");
            } else {
                scenario.setScenarioStatus(ScenarioStatus.CLOUD_COMPLEX_FOUND, cloudRegion.getComplexName() + "");
            }
            if (!checkCloudRegionExists(cloudRegion.getCloudOwner(), cloudRegion.getName())) {
                scenario.setScenarioStatus(ScenarioStatus.CLOUD_REGION_CREATING, cloudRegion + "");
                createCloudRegion(cloudRegion);
                scenario.setScenarioStatus(ScenarioStatus.CLOUD_REGION_CREATED, cloudRegion + "");
            } else {
                scenario.setScenarioStatus(ScenarioStatus.CLOUD_REGION_FOUND, cloudRegion + "");
            }

            cloudService.createCloudRegionComplexRelations(cloudRegion.getCloudOwner(), cloudRegion.getName(),
                    cloudRegion.getComplexName());

            processTenants(scenario, cloudRegion);
            processAvailabilityZones(scenario, cloudRegion);
        }
    }

    private boolean checkComplexExists(String complexName) throws Exception {
        JSONObject root = new JSONObject(readResponseValidateOption(cloudService.getCloudComplex(complexName), false));
        if (root.has("error")) {
            return false;
        }
        log.info("[Scenario][Cloud][Complex][Exists] " + complexName);
        return true;
    }

    private void createComplex(String complexName) throws IOException {
        cloudService.createCloudComplex(complexName);
        log.info("[Scenario][Cloud][Complex][New] " + complexName);
    }

    private boolean checkCloudRegionExists(String cloudOwner, String name) throws Exception {
        JSONObject root = new JSONObject(
                readResponseValidateOption(cloudService.getCloudRegion(cloudOwner, name), false));
        if (root.has("error")) {
            return false;
        }
        log.info("[Scenario][Cloud][Region][Exists] " + cloudOwner + "/" + name);
        return true;
    }

    private void createCloudRegion(CloudRegion cloudRegion) throws Exception {

        switch (cloudRegion.getCloudType()) {
            case OPENSTACK: {
                readResponseValidateOption(cloudService.createOpenstackRegion(
                        cloudRegion.getCloudOwner(),
                        cloudRegion.getName(),
                        cloudRegion.getRegionName(),
                        cloudRegion.getComplexName(),
                        cloudRegion.getDomain(),
                        cloudRegion.getDefaultProject(),
                        cloudRegion.getAuthServiceURL(),
                        cloudRegion.getAuthUser(),
                        cloudRegion.getAuthPassword()), false);
                break;
            }
            case KUBERNETES: {
                readResponseValidateOption(cloudService.createK8SRegion(
                        cloudRegion.getCloudOwner(),
                        cloudRegion.getName(),
                        cloudRegion.getComplexName(),
                        cloudRegion.getDomain(),
                        cloudRegion.getConfigParameters()), false);
                break;
            }
        }
        log.info("[Scenario][Cloud][Region][New] " + cloudRegion);
    }

    private void processTenants(Scenario scenario, CloudRegion cloudRegion) throws Exception {
        for (Tenant tenant : cloudRegion.getTenants()) {
            tenant.setCloudRegion(cloudRegion);
            if (!checkTenant(tenant)) {
                createTenant(tenant);
                scenario.setScenarioStatus(ScenarioStatus.CLOUD_TENANT_CREATED, tenant + "");
            } else {
                scenario.setScenarioStatus(ScenarioStatus.CLOUD_TENANT_FOUND, tenant + "");
            }
        }
    }

    private boolean checkTenant(Tenant tenant) throws Exception {
        JSONObject root = new JSONObject(readResponseValidateOption(cloudService.getCloudTenant(
                tenant.getCloudRegion().getCloudOwner(),
                tenant.getCloudRegion().getName(),
                tenant.getId()), false));
        if (root.has("error")) {
            return false;
        }
        log.info("[Scenario][Cloud][Tenant][Exists] " + tenant);
        return true;
    }

    private void createTenant(Tenant tenant) throws IOException {
        cloudService.createCloudTenant(
                tenant.getCloudRegion().getCloudOwner(),
                tenant.getCloudRegion().getName(),
                tenant.getId(),
                tenant.getName());
        log.info("[Scenario][Cloud][Tenant][New] " + tenant);

    }

    private void processAvailabilityZones(Scenario scenario, CloudRegion cloudRegion) throws Exception {
        for (AvailabilityZone availabilityZone : cloudRegion.getAvailabilityZones()) {
            availabilityZone.setCloudRegion(cloudRegion);
            if (!checkAvailabilityZone(availabilityZone)) {
                createAvailabilityZone(availabilityZone);
                scenario.setScenarioStatus(ScenarioStatus.CLOUD_AZ_CREATED, availabilityZone + "");
            } else {
                scenario.setScenarioStatus(ScenarioStatus.CLOUD_AZ_FOUND, availabilityZone + "");
            }
        }
    }

    private boolean checkAvailabilityZone(AvailabilityZone availabilityZone) throws Exception {
        JSONObject root = new JSONObject(readResponseValidateOption(cloudService.getCloudAvailabilityZone(
                availabilityZone.getCloudRegion().getCloudOwner(),
                availabilityZone.getCloudRegion().getName(),
                availabilityZone.getName()), false));
        if (root.has("error")) {
            return false;
        }
        log.info("[Scenario][Cloud][AvailabilityZone][Exists] " + availabilityZone);
        return true;
    }

    private void createAvailabilityZone(AvailabilityZone availabilityZone) throws IOException {
        cloudService.createCloudAvailibilityZone(
                availabilityZone.getCloudRegion().getCloudOwner(),
                availabilityZone.getCloudRegion().getName(),
                availabilityZone.getName(),
                availabilityZone.getHypervisorType());
        log.info("[Scenario][Cloud][AZ][New] " + availabilityZone);
    }

}
