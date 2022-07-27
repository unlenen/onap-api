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

import java.util.HashMap;
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

import tr.com.argela.nfv.onap.service.constant.ScenarioStatus;
import tr.com.argela.nfv.onap.service.controller.BusinessController;
import tr.com.argela.nfv.onap.service.controller.DesignController;
import tr.com.argela.nfv.onap.service.model.Customer;
import tr.com.argela.nfv.onap.service.model.Scenario;
import tr.com.argela.nfv.onap.service.model.Service;
import tr.com.argela.nfv.onap.service.model.Tenant;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@org.springframework.stereotype.Service
public class SubscriptionScenario extends CommonScenario {

    Logger log = LoggerFactory.getLogger(SubscriptionScenario.class);

    @Autowired
    DesignController designService;

    @Autowired
    BusinessController businessService;

    public void processSubscription(Scenario scenario) throws Exception {
        Service service = scenario.getService();
        service.getScenario().mapTenants(service.getScenario());

        subscribeService(service);
        saveCustomers(scenario, service);
        subscribeConsumers(scenario, service);

    }

    private void saveCustomers(Scenario scenario, Service service) throws Exception {
        Map<String, Customer> customersAtOnap = getCustomers();

        for (Customer customer : service.getCustomers()) {
            customer.setService(service);
            Customer customerOnap = customersAtOnap.get(customer.getId());
            if (customerOnap == null) {
                createCustomer(customer);
                scenario.setScenarioStatus(ScenarioStatus.CUSTOMER_CREATED, customer + "");
            } else {
                customer.copy(customerOnap);
                scenario.setScenarioStatus(ScenarioStatus.CUSTOMER_FOUND, customer + "");
                log.info("[Scenario][Subscription][Customer][Exists] " + customerOnap);
            }
        }
    }

    private Map<String, Customer> getCustomers() throws Exception {
        Map<String, Customer> customerIds = new HashMap<>();
        String data = readResponseValidateOption(businessService.getCustomers(), false);
        JSONObject root = new JSONObject(data);
        if (root.has("error")) {
            return new HashMap<>();
        }
        JSONArray array = root.getJSONArray("customer");
        for (int i = 0; i < array.length(); i++) {
            JSONObject customer = array.getJSONObject(i);
            String id = customer.getString("global-customer-id");
            String name = customer.getString("subscriber-name");
            String versionId = customer.getString("resource-version");
            String type = customer.getString("subscriber-type");
            customerIds.put(id, new Customer(id, name, versionId, type));
        }
        return customerIds;
    }

    private void createCustomer(Customer customer) throws Exception {
        readResponseValidateOption(businessService.createCustomer(customer.getId(), customer.getName()), false);
        log.info("[Scenario][Subscription][Customer][New] " + customer);
    }

    private void subscribeService(Service service) throws Exception {

        JSONObject subscription = new JSONObject(
                readResponseValidateOption(businessService.getServiceSubscriptions(service.getUniqueId()), false));
        if (subscription.has("service-id")) {
            log.info("[Scenario][Subscription][Service][Exists] " + service);
        } else {
            readResponseValidateOption(
                    businessService.createServiceSubscription(service.getUniqueId(), service.getName()), false);
            log.info("[Scenario][Subscription][Service][Create] " + service);
        }
    }

    private void subscribeConsumers(Scenario scenario, Service service) throws Exception {
        for (Customer customer : service.getCustomers()) {
            subscribeConsumerToService(scenario, customer);
        }
    }

    private void subscribeConsumerToService(Scenario scenario, Customer customer) throws Exception {
        JSONObject subscription = new JSONObject(readResponseValidateOption(
                businessService.getCustomerServiceSubscription(customer.getId(), customer.getService().getName()),
                false));
        if (subscription.has("service-type")) {
            log.info("[Scenario][Subscription][Service][Exists] " + customer + " to " + customer.getService());
            scenario.setScenarioStatus(ScenarioStatus.SERVICE_SUBSCRIPTION_CUSTOMER_FOUND,
                    customer.getService() + " <-> " + customer);
        } else {
            businessService.createCustomerServiceSubscription(customer.getId(), customer.getService().getName());
            scenario.setScenarioStatus(ScenarioStatus.SERVICE_SUBSCRIPTION_CUSTOMER_CREATED,
                    customer.getService() + " <-> " + customer);
            log.info("[Scenario][Subscription][Service][Create] " + customer + " to " + customer.getService());
        }

        subscribeTenants(scenario, customer, subscription);
    }

    private void subscribeTenants(Scenario scenario, Customer customer, JSONObject subscription) throws Exception {
        DocumentContext rootContext = JsonPath.parse(subscription.toString());

        for (Tenant tenant : customer.getService().getTenants()) {
            Tenant tenantFull = customer.getService().getScenario().getTenantMapById().get(tenant.getId());
            tenant.setName(tenantFull.getName());
            tenant.setCloudRegion(tenantFull.getCloudRegion());
            boolean subscriptionFound = false;
            if (subscription.has("relationship-list")) {

                Filter tenantId = Filter.filter(Criteria.where("related-link").contains(tenant.getId()));
                net.minidev.json.JSONArray tenantFound = rootContext.read("$['relationship-list']['relationship'][?]",
                        tenantId);
                if (tenantFound.size() > 0) {
                    log.info("[Scenario][Subscription][Service][Tenant][Exists] " + customer + " to "
                            + customer.getService() + " on " + tenant);
                    subscriptionFound = true;
                }

            }
            if (!subscriptionFound) {
                createTenantSubscription(tenant, customer);
                scenario.setScenarioStatus(ScenarioStatus.SERVICE_SUBSCRIPTION_CUSTOMER_TENANT_CREATED,
                        customer.getService() + " <-> " + customer + " <-> " + tenant);
                log.info("[Scenario][Subscription][Service][Tenant][New] " + customer + " to " + customer.getService()
                        + " on " + tenant);
            }else{
                scenario.setScenarioStatus(ScenarioStatus.SERVICE_SUBSCRIPTION_CUSTOMER_TENANT_FOUND,
                        customer.getService() + " <-> " + customer + " <-> " + tenant);
            }
        }

    }

    private void createTenantSubscription(Tenant tenant, Customer customer) throws Exception {
        String result = readResponse(businessService.createCustomerTenantSubscription(customer.getId(),
                tenant.getCloudRegion().getCloudOwner(), tenant.getCloudRegion().getName(), tenant.getId(),
                tenant.getName(), customer.getService().getName()));
    }

}
