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
package unlenen.cloud.onap.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import unlenen.cloud.onap.api.service.BusinessService;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@RestController
@RequestMapping("/business")
public class BusinessController {

    @Autowired
    BusinessService businessService;

    @GetMapping(path = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCustomers() throws Exception {
        return ResponseEntity.ok(businessService.getCustomers().toString());
    }

    @GetMapping(path = "/customer/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCustomer(@PathVariable String customerId) throws Exception {
        return ResponseEntity.ok(businessService.getCustomer(customerId).toString());
    }

    @PutMapping(path = "/customer/{customerId}/{customerName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCustomer(@PathVariable String customerId, @PathVariable String customerName)
            throws Exception {
        businessService.createCustomer(customerId, customerName);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping(path = "/customer/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteCustomer(@PathVariable String customerId) throws Exception {
        businessService.deleteCustomer(customerId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/customer-subscribe/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCustomerSubscriptions(@PathVariable String customerId) throws Exception {
        return ResponseEntity.ok(businessService.getCustomerSubscriptions(customerId).toString());
    }

    @GetMapping(path = "/customer-subscribe/{customerId}/{serviceName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCustomerServiceSubscription(@PathVariable String customerId,
            @PathVariable String serviceName) throws Exception {
        return ResponseEntity.ok(businessService.getCustomerServiceSubscription(customerId, serviceName).toString());
    }

    @PutMapping(path = "/customer-subscribe/{customerId}/{serviceName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCustomerServiceSubscription(@PathVariable String customerId,
            @PathVariable String serviceName) throws Exception {
        businessService.createCustomerServiceSubscription(customerId, serviceName);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping(path = "/customer-subscribe-tenant/{customerId}/{cloudOwner}/{regionId}/{tenantId}/{tenantName}/{serviceName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCustomerTenantSubscription(@PathVariable String customerId,
            @PathVariable String cloudOwner, @PathVariable String regionId, @PathVariable String tenantId,
            @PathVariable String tenantName, @PathVariable String serviceUniqueId) throws Exception {
        businessService.createCustomerTenantSubscription(customerId, cloudOwner, regionId, tenantId, tenantName,
                serviceUniqueId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/service-subscribe/{serviceInvariantUUID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getServiceSubscriptions(@PathVariable String serviceInvariantUUID)
            throws Exception {
        return ResponseEntity.ok(businessService.getServiceSubscriptions(serviceInvariantUUID).toString());
    }

    @PutMapping(path = "/service-subscribe/{serviceInvariantUUID}/{serviceName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createServiceSubscription(@PathVariable String serviceInvariantUUID,
            @PathVariable String serviceName) throws Exception {
        businessService.createServiceSubscription(serviceInvariantUUID, serviceName);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/owning-entities", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getOwningEntities() throws Exception {
        return ResponseEntity.ok(businessService.getOwningEntities().toString());
    }

    @GetMapping(path = "/owning-entity/{owningId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getOwningEntity(@PathVariable String owningEntityId) throws Exception {
        return ResponseEntity.ok(businessService.getOwningEntity(owningEntityId).toString());
    }

    @PutMapping(path = "/owning-entity/{owningId}/{owningName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createOwningEntity(@PathVariable String owningEntityId,
            @PathVariable String owningName) throws Exception {
        businessService.createOwningEntity(owningEntityId, owningName);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/platforms", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPlatforms() throws Exception {
        return ResponseEntity.ok(businessService.getPlatforms().toString());
    }

    @GetMapping(path = "/platform/{platformName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPlatform(@PathVariable String platformName) throws Exception {
        return ResponseEntity.ok(businessService.getPlatform(platformName).toString());
    }

    @PutMapping(path = "/platform/{platformName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createPlatform(@PathVariable String platformName) throws Exception {
        businessService.createPlatform(platformName);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/projects", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProjects() throws Exception {
        return ResponseEntity.ok(businessService.getProjects().toString());
    }

    @GetMapping(path = "/project/{projectName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProject(@PathVariable String projectName) throws Exception {
        return ResponseEntity.ok(businessService.getProject(projectName).toString());
    }

    @PutMapping(path = "/project/{projectName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createProject(@PathVariable String projectName) throws Exception {
        businessService.createProject(projectName);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/line-of-businesses", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getLineOfBusinesses() throws Exception {
        return ResponseEntity.ok(businessService.getLineOfBusinesses().toString());
    }

    @GetMapping(path = "/line-of-business/{lineOfBusiness}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getLineOfBusiness(@PathVariable String lineOfBusiness) throws Exception {
        return ResponseEntity.ok(businessService.getLineOfBusiness(lineOfBusiness).toString());
    }

    @PutMapping(path = "/line-of-business/{lineOfBusiness}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createLineOfBusiness(@PathVariable String lineOfBusiness) throws Exception {
        businessService.createLineOfBusiness(lineOfBusiness);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
