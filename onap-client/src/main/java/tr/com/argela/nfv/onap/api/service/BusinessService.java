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
package tr.com.argela.nfv.onap.api.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tr.com.argela.nfv.onap.api.client.OnapClient;
import tr.com.argela.nfv.onap.api.client.model.OnapRequest;
import tr.com.argela.nfv.onap.api.client.model.OnapRequestParameters;
import tr.com.argela.nfv.onap.api.exception.CustomerNotFoundException;
import tr.com.argela.nfv.onap.api.exception.OnapException;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@Service
public class BusinessService {

    @Autowired
    OnapClient adaptor;

    Logger log = LoggerFactory.getLogger(BusinessService.class);

    public JSONObject getCustomers() throws IOException {
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.BUSINESS_CUSTOMERS);
        if (log.isInfoEnabled())
            log.info("[Business][Customers][Get] size:" + adaptor.getResponseSize(data, "customer"));
        return data;
    }

    public JSONObject getCustomer(String customerId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_CUSTOMER_ID.name(), customerId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.BUSINESS_CUSTOMER, parameters);
        if (log.isInfoEnabled())
            log.info("[Business][Customer][Get] customerId:" + customerId + ", data: " + data);
        return data;
    }

    public void createCustomer(String customerId, String customerName)
            throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_CUSTOMER_ID.name(), customerId);
        parameters.put(OnapRequestParameters.BUSINESS_CUSTOMER_NAME.name(), customerName);
        adaptor.call(OnapRequest.BUSINESS_CUSTOMER_CREATE, parameters);
        if (log.isInfoEnabled())
            log.info("[Business][Customer][Create] customerId:" + customerId + " , customerName:" + customerName);
    }

    public void deleteCustomer(String customerId) throws OnapException {
        JSONObject customer = getCustomer(customerId);
        if (!customer.has("resource-version")) {
            throw new CustomerNotFoundException(customerId);
        }
        String resourceVersion = customer.getString("resource-version");
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_CUSTOMER_ID.name(), customerId);
        parameters.put(OnapRequestParameters.RESOURCE_VERSION.name(), resourceVersion);
        adaptor.call(OnapRequest.BUSINESS_CUSTOMER_DELETE, parameters);
        if (log.isInfoEnabled())
            log.info("[Business][Customer][Delete] customerId:" + customerId);
    }

    public JSONObject getCustomerSubscriptions(String customerId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_CUSTOMER_ID.name(), customerId);

        JSONObject data = (JSONObject) adaptor.call(OnapRequest.BUSINESS_CUSTOMER_SUBSCRIPTIONS, parameters);
        if (log.isInfoEnabled())
            log.info("[Business][Customer][Subscriptions] customerId:" + customerId + " , response:  size : "
                    + adaptor.getResponseSize(data, "service-subscription"));
        return data;
    }

    public JSONObject getCustomerServiceSubscription(String customerId,
            String serviceName) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_CUSTOMER_ID.name(), customerId);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_NAME.name(), serviceName);

        JSONObject data = (JSONObject) adaptor.call(OnapRequest.BUSINESS_CUSTOMER_SERVICE_SUBSCRIPTION, parameters);
        if (log.isInfoEnabled())
            log.info("[Business][Customer][Subscription][Service][Get] customerId:" + customerId + ",serviceName:"
                    + serviceName + " , data:" + data);
        return data;
    }

    public void createCustomerServiceSubscription(String customerId,
            String serviceName) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_CUSTOMER_ID.name(), customerId);

        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_NAME.name(), serviceName);

        adaptor.call(OnapRequest.BUSINESS_CUSTOMER_SUBSCRIPTION_CREATE, parameters);
        if (log.isInfoEnabled())
            log.info("[Business][Customer][Subscription][Service][Create] customerId:" + customerId + ",serviceName:"
                    + serviceName);

    }

    public void createCustomerTenantSubscription(String customerId,
            String cloudOwner, String regionId, String tenantId,
            String tenantName, String serviceUniqueId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_CUSTOMER_ID.name(), customerId);
        parameters.put(OnapRequestParameters.CLOUD_OWNER.name(), cloudOwner);
        parameters.put(OnapRequestParameters.CLOUD_REGION.name(), regionId);
        parameters.put(OnapRequestParameters.CLOUD_TENANT_ID.name(), tenantId);
        parameters.put(OnapRequestParameters.CLOUD_TENANT_NAME.name(), tenantName);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_NAME.name(), serviceUniqueId);

        adaptor.call(OnapRequest.BUSINESS_CUSTOMER_TENANT_SUBSCRIPTION_CREATE, parameters);
        if (log.isInfoEnabled())
            log.info("[Business][Customer][Subscription][Tenant][Create] customerId:" + customerId + ",cloudOwner:"
                    + cloudOwner + " ,regionId:" + regionId + ",tenantId:" + tenantId + ", serviceUniqueId:"
                    + serviceUniqueId);

    }

    public JSONObject getServiceSubscriptions(String serviceInvariantUUID)
            throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_InvariantUUID.name(), serviceInvariantUUID);

        JSONObject data = (JSONObject) adaptor.call(OnapRequest.AAI_SERVICE_MODEL_SUBSCRIPTIONS, parameters);
        if (log.isInfoEnabled())
            log.info("[Business][Service][Subscriptions] serviceInvariantUUID: " + serviceInvariantUUID + " , data: "
                    + data);
        return data;
    }

    public void createServiceSubscription(String serviceInvariantUUID,
            String serviceName) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_InvariantUUID.name(), serviceInvariantUUID);
        parameters.put(OnapRequestParameters.DESIGN_SERVICE_MODEL_NAME.name(), serviceName);

        adaptor.call(OnapRequest.AAI_SERVICE_MODEL_SUBSCRIPTION_CREATE, parameters);
        if (log.isInfoEnabled())
            log.info("[Business][Service][Subscriptions] serviceInvariantUUID: " + serviceInvariantUUID
                    + " ,serviceName:"
                    + serviceName);

    }

    public JSONObject getOwningEntities() throws IOException {
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.BUSINESS_OWNING_ENTITIES);
        if (log.isInfoEnabled())
            log.info("[Business][OwningEntities][Get] size:" + adaptor.getResponseSize(data, "owning-entity"));
        return data;
    }

    public JSONObject getOwningEntity(String owningEntityId) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_OWNING_ENTITY_ID.name(), owningEntityId);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.BUSINESS_OWNING_ENTITY, parameters);
        if (log.isInfoEnabled())
            log.info("[Business][OwningEntity][Get] " + parameters + " , response :" + data);
        return data;
    }

    public void createOwningEntity(String owningEntityId,
            String owningName) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_OWNING_ENTITY_ID.name(), owningEntityId);
        parameters.put(OnapRequestParameters.BUSINESS_OWNING_ENTITY_NAME.name(), owningName);
        adaptor.call(OnapRequest.BUSINESS_OWNING_ENTITY_CREATE, parameters);
        if (log.isInfoEnabled())
            log.info("[Business][OwningEntity][Create] " + parameters);
    }

    public JSONObject getPlatforms() throws IOException {
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.BUSINESS_PLATFORMS);
        if (log.isInfoEnabled())
            log.info("[Business][Platforms][Get] size:" + adaptor.getResponseSize(data, "platform"));
        return data;
    }

    public JSONObject getPlatform(String platformName) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_PLATFORM_NAME.name(), platformName);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.BUSINESS_PLATFORM, parameters);
        if (log.isInfoEnabled())
            log.info("[Business][Platform][Get] " + parameters + " , response : " + data);
        return data;
    }

    public void createPlatform(String platformName) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_PLATFORM_NAME.name(), platformName);
        adaptor.call(OnapRequest.BUSINESS_PLATFORM_CREATE, parameters);
        if (log.isInfoEnabled())
            log.info("[Business][Project][Create] " + parameters);
    }

    public JSONObject getProjects() throws IOException {
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.BUSINESS_PROJECTS);
        if (log.isInfoEnabled())
            log.info("[Business][Projects][Get] size:" + adaptor.getResponseSize(data, "project"));
        return data;
    }

    public JSONObject getProject(String projectName) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_PROJECT_NAME.name(), projectName);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.BUSINESS_PROJECT, parameters);
        if (log.isInfoEnabled())
            log.info("[Business][Project][Get] " + parameters + " , response : " + data);
        return data;
    }

    public void createProject(String projectName) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_PROJECT_NAME.name(), projectName);
        adaptor.call(OnapRequest.BUSINESS_PROJECT_CREATE, parameters);
        if (log.isInfoEnabled())
            log.info("[Business][Project][Create] " + parameters);
    }

    public JSONObject getLineOfBusinesses() throws IOException {
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.BUSINESS_LineOfBusiness);

        if (log.isInfoEnabled())
            log.info("[Business][LineOfBusinesses][Get]");
        return data;
    }

    public JSONObject getLineOfBusiness(String lineOfBusiness) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_LINE_OF_BUSINESS.name(), lineOfBusiness);
        JSONObject data = (JSONObject) adaptor.call(OnapRequest.BUSINESS_LineOfBusiness, parameters);
        if (log.isInfoEnabled())
            log.info("[Business][LineOfBusiness][Get] " + parameters + " , response : " + data);
        return data;
    }

    public void createLineOfBusiness(String lineOfBusiness) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OnapRequestParameters.BUSINESS_LINE_OF_BUSINESS.name(), lineOfBusiness);
        adaptor.call(OnapRequest.BUSINESS_LineOfBusiness_CREATE, parameters);
        if (log.isInfoEnabled())
            log.info("[Business][LineOfBusiness][Create] " + parameters);
    }

}
