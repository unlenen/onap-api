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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import unlenen.cloud.onap.api.client.model.VFModuleProfile;
import unlenen.cloud.onap.api.service.RuntimeService;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@RestController

public class RuntimeController {

        @Autowired
        RuntimeService runtimeService;

        @GetMapping(path = "/runtime/service-instances/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<String> getServiceInstances(@PathVariable(required = true) String customerId)
                        throws Exception {
                return ResponseEntity.ok(runtimeService.getServiceInstances(customerId).toString());
        }

        @GetMapping(path = "/runtime/service-instance/{serviceInstanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<String> getServiceInstanceDetails(@PathVariable(required = true) String serviceInstanceId)
                        throws Exception {
                return ResponseEntity.ok(runtimeService.getServiceInstanceDetails(serviceInstanceId).toString());
        }

        @PutMapping(path = "/runtime/service-instance/{serviceInstanceName}/{serviceModelInvariantUUID}/{serviceModelUUID}/{serviceName}/{owningId}/{owningName}/{customerId}/{projectName}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<String> createServiceInstance(@PathVariable String serviceInstanceName,
                        @PathVariable String serviceModelInvariantUUID,
                        @PathVariable String serviceModelUUID,
                        @PathVariable String serviceName,
                        @PathVariable String owningId,
                        @PathVariable String owningName,
                        @PathVariable String customerId,
                        @PathVariable String projectName) throws Exception {
                return ResponseEntity.ok(
                                runtimeService.createServiceInstance(serviceInstanceName, serviceModelInvariantUUID,
                                                serviceModelUUID, serviceName, owningId, owningName, customerId,
                                                projectName).toString());
        }

        @DeleteMapping(path = "/runtime/service-instance/{serviceInstanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<String> deleteServiceInstance(@PathVariable String serviceInstanceId,
                        @RequestParam(name = "serviceName") String serviceName,
                        @RequestParam(name = "serviceUUID") String serviceModelUUID,
                        @RequestParam(name = "serviceInvariantUUID") String serviceModelInvariantUUID)
                        throws Exception {
                return ResponseEntity.ok(runtimeService
                                .deleteServiceInstance(serviceInstanceId, serviceName, serviceModelUUID,
                                                serviceModelInvariantUUID)
                                .toString());
        }

        @GetMapping(path = "/runtime/vnfs", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<String> getVNFs() throws Exception {
                return ResponseEntity.ok(runtimeService.getVNFs().toString());
        }

        @GetMapping(path = "/runtime/action/status", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<String> getActionStatus(@RequestParam("url") String url) {
                return ResponseEntity.ok(runtimeService.getActionStatus(url).toString());
        }

        @PutMapping(path = "/runtime/vnf/{vnfName}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<String> createVNF(@PathVariable String vnfName,
                        @RequestParam(name = "serviceInstanceId") String serviceInstanceId,
                        @RequestParam(name = "serviceName") String serviceName,
                        @RequestParam(name = "serviceInvariantUUID") String serviceInvariantUUID,
                        @RequestParam(name = "serviceUUID") String serviceUUID,
                        @RequestParam(name = "serviceUniqueId") String serviceUniqueId,
                        @RequestParam(name = "cloudOwner") String cloudOwner,
                        @RequestParam(name = "cloudRegion") String cloudRegion,
                        @RequestParam(name = "tenantId") String tenantId,
                        @RequestParam(name = "vfName") String vfName,
                        @RequestParam(name = "vfModelName") String vfModelName,
                        @RequestParam(name = "vfInvariantUUID") String vfInvariantUUID,
                        @RequestParam(name = "vfUUID") String vfUUID,
                        @RequestParam(name = "lineOfBusiness") String lineOfBusiness,
                        @RequestParam(name = "platformName") String platformName) throws Exception {
                return ResponseEntity.ok(runtimeService.createVNF(vnfName, serviceInstanceId, serviceName,
                                serviceInvariantUUID, serviceUUID, serviceUniqueId, cloudOwner, cloudRegion, tenantId,
                                vfName,
                                vfModelName, vfInvariantUUID, vfUUID, lineOfBusiness, platformName).toString());
        }

        @DeleteMapping(path = "/runtime/vnf/{vnfId}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<String> deleteVNF(@PathVariable String vnfId,
                        @RequestParam(name = "serviceInstanceId") String serviceInstanceId,
                        @RequestParam(name = "vfName") String vfName,
                        @RequestParam(name = "vfUUID") String vfUUID,
                        @RequestParam(name = "vfInvariantUUID") String vfInvariantUUID,
                        @RequestParam(name = "vfModelName") String vfModelName,
                        @RequestParam(name = "cloudOwner") String cloudOwner,
                        @RequestParam(name = "cloudRegion") String cloudRegion,
                        @RequestParam(name = "tenantId") String tenantId) throws Exception {
                return ResponseEntity
                                .ok(runtimeService
                                                .deleteVNF(vnfId, serviceInstanceId, vfName, vfUUID, vfInvariantUUID,
                                                                vfModelName, cloudOwner, cloudRegion, tenantId)
                                                .toString());
        }

        @GetMapping(path = "/runtime/vnf/{vnfId}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<String> getVNFDetail(@PathVariable(required = true) String vnfId) throws Exception {
                return ResponseEntity.ok(runtimeService.getVNFDetail(vnfId).toString());
        }

        @GetMapping(path = "/runtime/vnf/{serviceUniqueId}/vnfName", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<String> getVNFDetailByService(
                        @PathVariable(required = true) String serviceUniqueId,
                        @PathVariable(required = true) String vnfName) throws Exception {
                return ResponseEntity.ok(runtimeService.getVNFDetailByService(serviceUniqueId, vnfName).toString());
        }

        @GetMapping(path = "/runtime/vf-modules/{vnfId}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<String> getVFModules(@PathVariable(required = true) String vnfId) throws Exception {
                return ResponseEntity.ok(runtimeService.getVFModules(vnfId).toString());
        }

        @PutMapping(path = "/runtime/vf-module/{vnfName}/{vnfType}/{vfModuleName}/{vfModuleType}/{availabilityZone}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<String> preloadVFModule(
                        @PathVariable(required = true) String vnfName,
                        @PathVariable(required = true) String vnfType,
                        @PathVariable(required = true) String vfModuleName,
                        @PathVariable(required = true) String vfModuleType,
                        @PathVariable(required = true) String availabilityZone,
                        @RequestBody VFModuleProfile vFModuleProfile) throws Exception {
                return ResponseEntity.ok(runtimeService
                                .preloadVFModule(vnfName, vnfType, vfModuleName, vfModuleType, availabilityZone,
                                                vFModuleProfile)
                                .toString());
        }

        @PutMapping(path = "/runtime/vfModule/{vnfId}/{vfModuleName}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<String> createVfModule(
                        @PathVariable String vnfId,
                        @PathVariable String vfModuleName,
                        @RequestParam(name = "serviceInstanceId") String serviceInstanceId,
                        @RequestParam(name = "serviceName") String serviceName,
                        @RequestParam(name = "serviceInvariantUUID") String serviceInvariantUUID,
                        @RequestParam(name = "serviceUUID") String serviceUUID,
                        @RequestParam(name = "cloudOwner") String cloudOwner,
                        @RequestParam(name = "cloudRegion") String cloudRegion,
                        @RequestParam(name = "tenantId") String tenantId,
                        @RequestParam(name = "vfUUID") String vfUUID,
                        @RequestParam(name = "vfName") String vfName,
                        @RequestParam(name = "vfModelUUID") String vfModelUUID,
                        @RequestParam(name = "vfModelType") String vfModelType,
                        @RequestParam(name = "vfModelName") String vfModelName,
                        @RequestParam(name = "vfModelInvariantId") String vfModelInvariantId,
                        @RequestParam(name = "vfModelCustomizationId") String vfModelCustomizationId) throws Exception {
                return ResponseEntity.ok(runtimeService
                                .createVfModule(vnfId, vfModuleName, serviceInstanceId, serviceName,
                                                serviceInvariantUUID, serviceUUID,
                                                cloudOwner, cloudRegion, tenantId, vfUUID, vfName, vfModelUUID,
                                                vfModelType, vfModelName,
                                                vfModelInvariantId, vfModelCustomizationId)
                                .toString());
        }

        @DeleteMapping(path = "/runtime/vfModule/{vnfId}/{vfModuleId}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<String> deleteVfModule(
                        @PathVariable String vnfId,
                        @PathVariable String vfModuleId,
                        @RequestParam(name = "serviceInstanceId") String serviceInstanceId,
                        @RequestParam(name = "vfModelType") String vfModelType,
                        @RequestParam(name = "vfModelUUID") String vfModelUUID,
                        @RequestParam(name = "vfModelInvariantId") String vfModelInvariantId,
                        @RequestParam(name = "cloudOwner") String cloudOwner,
                        @RequestParam(name = "cloudRegion") String cloudRegion,
                        @RequestParam(name = "tenantId") String tenantId) throws Exception {
                return ResponseEntity.ok(runtimeService
                                .deleteVfModule(vnfId, vfModuleId, serviceInstanceId, vfModelType, vfModelUUID,
                                                vfModelInvariantId,
                                                cloudOwner, cloudRegion, tenantId)
                                .toString());
        }

        @GetMapping(path = "/runtime/vf-module/{vnfId}/{vfModuleId}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<String> getVFModuleDetail(@PathVariable(required = true) String vnfId,
                        @PathVariable String vfModuleId) throws Exception {
                return ResponseEntity.ok(runtimeService
                                .getVFModuleDetail(vnfId, vfModuleId).toString());
        }

        @GetMapping(path = "/runtime/vf-module-properties/{vfModuleId}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<String> getVFModuleInstantiationDetail(@PathVariable(required = true) String vfModuleId)
                        throws Exception {
                return ResponseEntity.ok(runtimeService
                                .getVFModuleInstantiationDetail(vfModuleId).toString());
        }

        @GetMapping(path = "/runtime/vf-module-topology/{serviceInstanceId}/{vnfId}/{vfModuleId}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<String> getVFModuleTopology(@PathVariable(required = true) String serviceInstanceId,
                        @PathVariable(required = true) String vnfId, @PathVariable(required = true) String vfModuleId)
                        throws Exception {
                return ResponseEntity.ok(runtimeService
                                .getVFModuleTopology(serviceInstanceId, vnfId, vfModuleId).toString());
        }

}
