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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jayway.jsonpath.Criteria;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;

import tr.com.argela.nfv.onap.service.constant.DistributionStatus;
import tr.com.argela.nfv.onap.service.constant.EntityStatus;
import tr.com.argela.nfv.onap.service.constant.ScenarioStatus;
import tr.com.argela.nfv.onap.service.controller.DesignController;
import tr.com.argela.nfv.onap.service.exception.ServiceDistributionFailedException;
import tr.com.argela.nfv.onap.service.model.Scenario;
import tr.com.argela.nfv.onap.service.model.Service;
import tr.com.argela.nfv.onap.service.model.VF;
import tr.com.argela.nfv.onap.service.model.VFModel;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@org.springframework.stereotype.Service
public class ServiceModelScenario extends CommonScenario {

    Logger log = LoggerFactory.getLogger(ServiceModelScenario.class);

    @Autowired
    DesignController designService;

    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");

    String[] checkObjs = { "aai-ml", "sdc-COpenSource-Env11-sdnc-dockero", "cds", "policy-id" };
    // String[] checkObjs = {"SO-COpenSource-Env11", "aai-ml",
    // "sdc-COpenSource-Env11-sdnc-dockero", "cds", "policy-id"};

    public void processService(Scenario scenario) throws Exception {
        Service service = scenario.getService();
        if (serviceExists(service)) {
            readServiceUniqueId(service);
            scenario.setScenarioStatus(ScenarioStatus.SERVICE_FOUND, service + "");
        } else {
            createService(service);
            scenario.setScenarioStatus(ScenarioStatus.SERVICE_CREATED, service + "");
        }

        if (service.getDistributionStatus() != DistributionStatus.DISTRIBUTED
                || service.getVersionStatus() != EntityStatus.CERTIFIED) {
            addVfsToService(scenario, service);
        }

        if (service.getVersionStatus() == EntityStatus.NOT_CERTIFIED_CHECKOUT) {
            certifyService(service);
            scenario.setScenarioStatus(ScenarioStatus.SERVICE_CERTIFIED, service + "");
        }

        if (service.getDistributionStatus() == DistributionStatus.DISTRIBUTION_NOT_APPROVED) {
            distributeService(service);
            scenario.setScenarioStatus(ScenarioStatus.SERVICE_DISTRUBUTE_STARTED, service + "");
        }
        checkDistribution(service);
        scenario.setScenarioStatus(ScenarioStatus.SERVICE_DISTRUBUTE_COMPLETED, service + "");
        loadVFsModuleInfo(service);
    }

    public void addVfsToService(Scenario scenario, Service service) throws Exception {
        int index = 1;
        for (VF vf : service.getVfs()) {
            addVfToService(vf, index++);
            scenario.setScenarioStatus(ScenarioStatus.SERVICE_VF_ADDED, vf + "");
        }
    }

    private boolean serviceExists(Service service) throws Exception {
        String root = readResponse(designService.getServiceModels(), true);
        Filter serviceName = Filter.filter(Criteria.where("name").eq(service.getName()));
        DocumentContext rootContext = JsonPath.parse(root);
        net.minidev.json.JSONArray services = rootContext.read("$[?]", serviceName);
        if (!services.isEmpty()) {
            LinkedHashMap<String, String> serviceObject = (LinkedHashMap<String, String>) services.get(0);
            service.setUuid(serviceObject.get("uuid"));
            service.setInvariantUUID(serviceObject.get("invariantUUID"));
            service.setVersionStatus(
                    EntityStatus.valueOf(serviceObject.get("lifecycleState").toUpperCase(Locale.ENGLISH)));
            service.setDistributionStatus(
                    DistributionStatus.valueOf(serviceObject.get("distributionStatus").toUpperCase(Locale.ENGLISH)));
            service.setVersionName(serviceObject.get("version"));
            log.info("[Scenario][Service][Exists] service:" + service.getName() + " , uuid : " + service.getUuid()
                    + " , invariantUUID :" + service.getInvariantUUID() + " , serviceStatus:"
                    + service.getVersionStatus());
        }
        return !services.isEmpty();
    }

    private void createService(Service service) throws Exception {
        JSONObject root = new JSONObject(
                readResponse(designService.createServiceModel(service.getName(), service.getDescription())));
        service.setInvariantUUID(root.getString("invariantUUID"));
        service.setUniqueId(root.getString("uniqueId"));
        service.setUuid(root.getString("uuid"));
        service.setVersionStatus(EntityStatus.valueOf(root.getString("lifecycleState").toUpperCase(Locale.ENGLISH)));
        service.setDistributionStatus(
                DistributionStatus.valueOf(root.getString("distributionStatus").toUpperCase(Locale.ENGLISH)));
        log.info("[Scenario][Service][New] service:" + service.getName() + " , uuid : " + service.getUuid()
                + " , invariantUUID :" + service.getInvariantUUID() + " , uniqueId:" + service.getUniqueId()
                + " , serviceStatus:" + service.getVersionStatus());
    }

    private void addVfToService(VF vf, int index) throws Exception {
        Service service = vf.getService();
        log.info("[Scenario][Service][AddVf] service:" + service.getName() + " , uuid : " + service.getUuid()
                + " , invariantUUID :" + service.getInvariantUUID() + " , uniqueId:" + service.getUniqueId()
                + " , serviceStatus:" + service.getVersionStatus() + " , vf:" + vf.getName() + " , vfUniqueId:"
                + vf.getUniqueId());
        JSONObject root = new JSONObject(readResponse(designService.addVFtoServiceModel(vf.getService().getUniqueId(),
                vf.getUniqueId(), vf.getName(), index)));
        /*
         * VFModel vfModel = vf.getVfModel(root.getString("name"));
         * vfModel.setCustomizationUUID(root.getString("customizationUUID"));
         */
    }

    protected void readServiceUniqueId(Service service) throws Exception {
        JSONObject root = new JSONObject(readResponse(designService.getServiceModelUniqueId(service.getUuid())));
        service.setUniqueId(root.getString("uniqueId"));
        service.setVersionStatus(EntityStatus.valueOf(root.getString("lifecycleState").toUpperCase(Locale.ENGLISH)));
        service.setVersionName(root.getString("version"));
        log.info("[Scenario][Service][UniqueId] service:" + service.getName() + " , uuid : " + service.getUuid()
                + " , invariantUUID :" + service.getInvariantUUID() + " , uniqueId:" + service.getUniqueId());
    }

    private void certifyService(Service service) throws Exception {
        JSONObject root = new JSONObject(readResponse(designService.certifyServiceModel(service.getUniqueId())));
        service.setVersionStatus(EntityStatus.CERTIFIED);
        service.setUniqueId(root.getString("uniqueId"));
        service.setVersionName(root.getString("version"));
        log.info("[Scenario][Service][Certify] service:" + service.getName() + " , uuid : " + service.getUuid()
                + " , invariantUUID :" + service.getInvariantUUID() + " , uniqueId:" + service.getUniqueId()
                + " , version:" + service.getVersionName());
        readServiceUniqueId(service);
    }

    private void distributeService(Service service) throws Exception {
        JSONObject root = new JSONObject(readResponse(designService.distributeServiceModel(service.getUniqueId())));
        service.setDistributionStatus(DistributionStatus.DISTRIBUTED);
        log.info("[Scenario][Service][Distribute] service:" + service.getName() + " , uuid : " + service.getUuid()
                + " , invariantUUID :" + service.getInvariantUUID() + " , uniqueId:" + service.getUniqueId()
                + " , version:" + service.getVersionName());
        service.mapVfs();
        JSONArray components = root.getJSONArray("componentInstances");
        for (int i = 0; i < components.length(); i++) {
            JSONObject component = components.getJSONObject(i);
            String modelName = component.getString("name");
            VF vf = service.getVFByModelName(modelName);
            if (vf == null) {
                continue;
            }
            JSONArray groupInstances = component.getJSONArray("groupInstances");
            for (int j = 0; j < groupInstances.length(); j++) {
                JSONObject groupInstance = groupInstances.getJSONObject(j);
                String modelType = groupInstance.getString("groupName");
                VFModel vfModel = vf.getVfModelType(modelType);
                vfModel.setModelType(groupInstance.getString("groupName"));
                vfModel.setCustomizationName(groupInstance.getString("groupName"));
                vfModel.setModelUUID(groupInstance.getString("groupUUID"));
                vfModel.setModelInvariantUUID(groupInstance.getString("invariantUUID"));
                vfModel.setModelCustomizationUUID(groupInstance.getString("customizationUUID"));
                log.info("[Scenario][Service][Distribute][VFModelUpdate] service:" + service.getName() + " ,  " + vf);
            }
        }

    }

    private void loadVFsModuleInfo(Service service) throws Exception {
        JSONObject root = new JSONObject(readResponse(designService.getServiceModelDetail(service.getUniqueId(),
                "filteredDataByParams?include=componentInstances")));
        log.info("[Scenario][Service][LoadVfInformation] service:" + service.getName() + " , uuid : "
                + service.getUuid() + " , invariantUUID :" + service.getInvariantUUID() + " , uniqueId:"
                + service.getUniqueId() + " , version:" + service.getVersionName());
        service.mapVfs();
        JSONArray components = root.getJSONArray("componentInstances");
        for (int i = 0; i < components.length(); i++) {
            JSONObject component = components.getJSONObject(i);
            String name = component.getString("componentName");
            String modelName = component.getString("name");
            VF vf = service.getVFByName(name);
            if (vf == null) {
                continue;
            }

            vf.setModelName(modelName);

            JSONArray groupInstances = component.getJSONArray("groupInstances");
            for (int j = 0; j < groupInstances.length(); j++) {

                JSONObject groupInstance = groupInstances.getJSONObject(j);
                String modelTypeName = groupInstance.getString("groupName");
                VFModel vfModel = vf.getVfModelType(modelTypeName);
                vfModel.setModelType(modelTypeName);
                vfModel.setCustomizationName(modelTypeName);
                vfModel.setCustomizationUUID(groupInstance.getString("customizationUUID"));
                vfModel.setModelUUID(groupInstance.getString("groupUUID"));
                vfModel.setModelInvariantUUID(groupInstance.getString("invariantUUID"));
                vfModel.setModelCustomizationUUID(groupInstance.getString("customizationUUID"));
                log.info("[Scenario][Service][LoadVfInformation][VFModelUpdate] service:" + service.getName() + " ,  "
                        + vf);
            }
        }
    }

    private void checkDistribution(Service service) throws Exception {

        try {
            JSONObject root = new JSONObject(
                    readResponse(designService.getServiceModelDistributions(service.getUuid())));
            JSONArray distributionDetailArray = root.getJSONArray("distributionStatusOfServiceList");
            Date biggestTime = null;
            String latestDistributionId = null;
            for (int i = 0; i < distributionDetailArray.length(); i++) {
                JSONObject distibutionDetail = distributionDetailArray.getJSONObject(i);
                String distributionId = distibutionDetail.getString("distributionID");
                String timestamp = distibutionDetail.getString("timestamp");
                Date date = dateFormatter.parse(timestamp);
                if (biggestTime == null || biggestTime.before(date)) {
                    biggestTime = date;
                    latestDistributionId = distributionId;
                }
            }
            controlDistribution(service, latestDistributionId);
        } catch (ServiceDistributionFailedException ex) {
            log.error("[Scenario][Service][Distribute][Failed] " + ex.getService() + ", component:" + ex.getComponent()
                    + " , status:" + ex.getStatus() + ", distributionId:" + ex.getDistributionId());
            // distributeService(service);
            // checkDistribution(service);
            throw ex;
        }
    }

    private boolean validateDistribution(int tryNumber, Service service, String distributionId) throws Exception {
        JSONObject root = new JSONObject(readResponse(designService.getServiceModelDistributionDetail(distributionId)));
        JSONArray distributionStatusList = root.getJSONArray("distributionStatusList");

        Map<String, Boolean> checkList = new HashMap<>();

        for (String component : checkObjs) {
            checkList.put(component, Boolean.FALSE);
        }

        for (int i = 0; i < distributionStatusList.length(); i++) {
            JSONObject distributionStatus = distributionStatusList.getJSONObject(i);
            String component = distributionStatus.getString("omfComponentID");
            String status = distributionStatus.getString("status");
            if (checkList.get(component) != null && "DOWNLOAD_OK".equals(status)) {
                checkList.put(component, Boolean.TRUE);
            }
            if (checkList.get(component) != null && "DISTRIBUTION_COMPLETE_ERROR".equals(status)) {
                throw new ServiceDistributionFailedException(service, distributionId, component, status);
            }

        }

        boolean totalStatus = true;
        for (String component : checkObjs) {
            boolean status = checkList.get(component);
            log.info("[Scenario][Service][Distribute][Status][No:" + tryNumber + "] service:" + service.getName()
                    + " , component : " + component + ", status: " + status);
            if (!status) {
                totalStatus = false;
            }
        }
        return totalStatus;

    }

    private void controlDistribution(Service service, String distributionId) throws Exception {
        int tryNumber = 1;
        while (true) {
            if (validateDistribution((tryNumber++), service, distributionId)) {
                break;
            }
            Thread.sleep(5000l);
        }
    }

}
