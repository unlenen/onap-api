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
package tr.com.argela.nfv.onap.service.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tr.com.argela.nfv.onap.api.client.OnapClient;
import tr.com.argela.nfv.onap.api.client.model.OnapRequest;
import tr.com.argela.nfv.onap.api.client.model.OnapRequestParameters;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@RestController
@RequestMapping("/business")
public class BusinessController {

    @Autowired
    OnapClient adaptor;

    Logger log = LoggerFactory.getLogger(BusinessController.class);

    @GetMapping(path = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCustomers() throws IOException {
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.BUSINESS_CUSTOMERS);
        log.info("[Business][Customers][Get] size:" + adaptor.getResponseSize(data, "customer"));
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/customer/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCustomer(@PathVariable String customerId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_CUSTOMER_ID.name(), customerId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.BUSINESS_CUSTOMER, parameters);
        log.info("[Business][Customer][Get] customerId:" + customerId + ", data: " + data);
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/customer/{customerId}/{customerName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createCustomer(@PathVariable String customerId, @PathVariable String customerName) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_CUSTOMER_ID.name(), customerId);
        parameters.put(OnapRequestParameters.BUSINESS_CUSTOMER_NAME.name(), customerName);
        String data = (String) adaptor.call(OnapRequest.BUSINESS_CUSTOMER_CREATE, parameters);
        log.info("[Business][Customer][Create] customerId:" + customerId + " , customerName:" + customerName);
        return ResponseEntity.ok(data);
    }

    @DeleteMapping(path = "/customer/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteCustomer(@PathVariable String customerId) throws IOException {

        ResponseEntity<String> responseEntity = getCustomer(customerId);
        JSONObject obj = new JSONObject(responseEntity.getBody() + "");
        if (!obj.has("resource-version")) {
            return ResponseEntity.ok("{\"error\":{\"msg\":\"Customer Not Found\"}}");
        }
        String resourceVersion = obj.getString("resource-version");
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_CUSTOMER_ID.name(), customerId);
        parameters.put(OnapRequestParameters.RESOURCE_VERSION.name(), resourceVersion);
        String data = (String) adaptor.call(OnapRequest.BUSINESS_CUSTOMER_DELETE, parameters);
        log.info("[Business][Customer][Delete] customerId:" + customerId);
        return ResponseEntity.ok(data);
    }

    @GetMapping(path = "/customer-subscribe/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCustomerSubscriptions(@PathVariable String customerId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_CUSTOMER_ID.name(), customerId);

        JSONObject data = (JSONObject) adaptor.call(OnapRequest.BUSINESS_CUSTOMER_SUBSCRIPTIONS, parameters);
        log.info("[Business][Customer][Subscriptions] customerId:" + customerId + " , response:  size : " + adaptor.getResponseSize(data, "service-subscription"));
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/customer-subscribe/{customerId}/{serviceName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCustomerServiceSubscription(@PathVariable String customerId, @PathVariable String serviceName) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_CUSTOMER_ID.name(), customerId);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_NAME.name(), serviceName);

        JSONObject data = (JSONObject) adaptor.call(OnapRequest.BUSINESS_CUSTOMER_SERVICE_SUBSCRIPTION, parameters);
        log.info("[Business][Customer][Subscription][Service][Get] customerId:" + customerId + ",serviceName:" + serviceName + " , data:" + data);
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/customer-subscribe/{customerId}/{serviceName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createCustomerServiceSubscription(@PathVariable String customerId, @PathVariable String serviceName) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_CUSTOMER_ID.name(), customerId);

        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_NAME.name(), serviceName);

        String data = (String) adaptor.call(OnapRequest.BUSINESS_CUSTOMER_SUBSCRIPTION_CREATE, parameters);
        log.info("[Business][Customer][Subscription][Service][Create] customerId:" + customerId + ",serviceName:" + serviceName + ", data:" + data);
        return ResponseEntity.ok("{}");
    }

    @PutMapping(path = "/customer-subscribe-tenant/{customerId}/{cloudOwner}/{regionId}/{tenantId}/{tenantName}/{serviceName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createCustomerTenantSubscription(@PathVariable String customerId, @PathVariable String cloudOwner, @PathVariable String regionId, @PathVariable String tenantId, @PathVariable String tenantName, @PathVariable String serviceUniqueId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_CUSTOMER_ID.name(), customerId);
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        parameters.put(OnapRequestParameters.CLOUD_REGION.name(), regionId);
        parameters.put(OnapRequestParameters.CLOUD_TENANT_ID.name(), tenantId);
        parameters.put(OnapRequestParameters.CLOUD_TENANT_NAME.name(), tenantName);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_NAME.name(), serviceUniqueId);

        String data = (String) adaptor.call(OnapRequest.BUSINESS_CUSTOMER_TENANT_SUBSCRIPTION_CREATE, parameters);
        log.info("[Business][Customer][Subscription][Tenant][Create] customerId:" + customerId + ",cloudOwner:" + cloudOwner + " ,regionId:" + regionId + ",tenantId:" + tenantId + ", serviceUniqueId:" + serviceUniqueId + ", data:" + data);
        return ResponseEntity.ok("{}");
    }

    @GetMapping(path = "/service-subscribe/{serviceInvariantUUID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getServiceSubscriptions(@PathVariable String serviceInvariantUUID) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_InvariantUUID.name(), serviceInvariantUUID);

        JSONObject data = (JSONObject) adaptor.call(OnapRequest.AAI_SERVICE_MODEL_SUBSCRIPTIONS, parameters);
        log.info("[Business][Service][Subscriptions] serviceInvariantUUID: " + serviceInvariantUUID + " , data: " + data);
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/service-subscribe/{serviceInvariantUUID}/{serviceName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createServiceSubscription(@PathVariable String serviceInvariantUUID, @PathVariable String serviceName) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_InvariantUUID.name(), serviceInvariantUUID);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_NAME.name(), serviceName);

        String data = (String) adaptor.call(OnapRequest.AAI_SERVICE_MODEL_SUBSCRIPTION_CREATE, parameters);
        log.info("[Business][Service][Subscriptions] serviceInvariantUUID: " + serviceInvariantUUID + " ,serviceName:" + serviceName + ", data: " + data);
        return ResponseEntity.ok("{]");
    }

    @GetMapping(path = "/owning-entities", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getOwningEntities() throws IOException {
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.BUSINESS_OWNING_ENTITIES);
        log.info("[Business][OwningEntities][Get] size:" + adaptor.getResponseSize(data, "owning-entity"));
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/owning-entity/{owningId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getOwningEntity(@PathVariable String owningEntityId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_OWNING_ENTITY_ID.name(), owningEntityId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.BUSINESS_OWNING_ENTITY, parameters);
        log.info("[Business][OwningEntity][Get] " + parameters + " , response :" + data);
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/owning-entity/{owningId}/{owningName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createOwningEntity(@PathVariable String owningEntityId, @PathVariable String owningName) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_OWNING_ENTITY_ID.name(), owningEntityId);
        parameters.put(OnapRequestParameters.BUSINESS_OWNING_ENTITY_NAME.name(), owningName);
        String data = (String) adaptor.call(OnapRequest.BUSINESS_OWNING_ENTITY_CREATE, parameters);
        log.info("[Business][OwningEntity][Create] " + parameters + " , response :" + data);
        return ResponseEntity.ok(data);
    }

    @GetMapping(path = "/platforms", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPlatforms() throws IOException {
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.BUSINESS_PLATFORMS);
        log.info("[Business][Platforms][Get] size:" + adaptor.getResponseSize(data, "platform"));
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/platform/{platformName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPlatform(@PathVariable String platformName) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_PLATFORM_NAME.name(), platformName);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.BUSINESS_PLATFORM, parameters);
        log.info("[Business][Platform][Get] " + parameters + " , response : " + data);
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/platform/{platformName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createPlatform(@PathVariable String platformName) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_PLATFORM_NAME.name(), platformName);
        String data = (String) adaptor.call(OnapRequest.BUSINESS_PLATFORM_CREATE, parameters);
        log.info("[Business][Project][Create] " + parameters + " , response : " + data);
        return ResponseEntity.ok(data);
    }

    @GetMapping(path = "/projects", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProjects() throws IOException {
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.BUSINESS_PROJECTS);
        log.info("[Business][Projects][Get] size:" + adaptor.getResponseSize(data, "project"));
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/project/{projectName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProject(@PathVariable String projectName) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_PROJECT_NAME.name(), projectName);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.BUSINESS_PROJECT, parameters);
        log.info("[Business][Project][Get] " + parameters + " , response : " + data);
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/project/{projectName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createProject(@PathVariable String projectName) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_PROJECT_NAME.name(), projectName);
        String data = (String) adaptor.call(OnapRequest.BUSINESS_PROJECT_CREATE, parameters);
        log.info("[Business][Project][Create] " + parameters + " , response : " + data);
        return ResponseEntity.ok(data);
    }

    @GetMapping(path = "/line-of-businesses", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getLineOfBusinesses() throws IOException {
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.BUSINESS_LineOfBusiness);
        log.info("[Business][LineOfBusinesses][Get]");
        return ResponseEntity.ok(data.toString());
    }

    @GetMapping(path = "/line-of-business/{lineOfBusiness}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getLineOfBusiness(@PathVariable String lineOfBusiness) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_LINE_OF_BUSINESS.name(), lineOfBusiness);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.BUSINESS_LineOfBusiness, parameters);
        log.info("[Business][LineOfBusiness][Get] " + parameters + " , response : " + data);
        return ResponseEntity.ok(data.toString());
    }

    @PutMapping(path = "/line-of-business/{lineOfBusiness}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createLineOfBusiness(@PathVariable String lineOfBusiness) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_LINE_OF_BUSINESS.name(), lineOfBusiness);
        String data = (String) adaptor.call(OnapRequest.BUSINESS_LineOfBusiness_CREATE, parameters);
        log.info("[Business][LineOfBusiness][Create] " + parameters + " , response : " + data);
        return ResponseEntity.ok(data);
    }

}
