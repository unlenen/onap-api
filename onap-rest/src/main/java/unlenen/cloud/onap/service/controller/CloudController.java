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

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import unlenen.cloud.onap.api.service.CloudService;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@RestController
public class CloudController {

    @Autowired
    CloudService cloudService;

    Logger log = LoggerFactory.getLogger(CloudController.class);

    @GetMapping(path = "/cloud/complexs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCloudComplexs() throws IOException {
        return ResponseEntity.ok(cloudService.getCloudComplexs().toString());
    }

    @GetMapping(path = "/cloud/complexs/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCloudComplex(@PathVariable String name) throws IOException {
        return ResponseEntity.ok(cloudService.getCloudComplex(name).toString());
    }

    @PutMapping(path = "/cloud/complex/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createCloudComplex(@PathVariable String name) throws IOException {
        return ResponseEntity.ok(createCloudComplex(name).toString());
    }

    @GetMapping(path = "/cloud/regions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCloudRegions() throws IOException {
        return ResponseEntity.ok(cloudService.getCloudRegions().toString());
    }

    @GetMapping(path = "/cloud/region/{cloudOwner}/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCloudRegion(@PathVariable String cloudOwner, @PathVariable String name)
            throws IOException {
        return ResponseEntity.ok(cloudService.getCloudRegion(cloudOwner, name).toString());
    }

    @PutMapping(path = "/cloud/openstack/{cloudOwner}/{name}/{regionName}/{complexName}/{osDomain}/{osDefaultProject}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createOpenstackRegion(@PathVariable String cloudOwner, @PathVariable String name,
            @PathVariable String regionName, @PathVariable String complexName,
            @PathVariable String osDomain, @PathVariable String osDefaultProject,
            @RequestParam(name = "keystoneURL") String osKeystoneURL, @RequestParam(name = "user") String osUser,
            @RequestParam(name = "password") String osPassword) throws IOException {

        cloudService.createOpenstackRegion(cloudOwner, name, regionName, complexName, osDomain, osDefaultProject,
                osKeystoneURL, osUser, osPassword);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping(path = "/cloud/k8s/{cloudOwner}/{name}/{complex}/{namespace}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createK8SRegion(
            @PathVariable String cloudOwner,
            @PathVariable String name,
            @PathVariable String complex,
            @PathVariable String namespace,
            @RequestBody String kubeconfig) throws IOException {

        cloudService.createK8SRegion(cloudOwner, name, complex, namespace, kubeconfig);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping(path = "/cloud/region-complex/{cloudOwner}/{name}/{complex}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCloudRegionComplexRelations(
            @PathVariable String cloudOwner,
            @PathVariable String name,
            @PathVariable String complex) throws IOException {
        cloudService.createCloudRegionComplexRelations(cloudOwner, name, complex);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/cloud/tenants/{cloudOwner}/{cloudRegion}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCloudTenants(@PathVariable String cloudOwner, @PathVariable String cloudRegion)
            throws IOException {
        return ResponseEntity.ok(cloudService.getCloudTenants(cloudOwner, cloudRegion).toString());
    }

    @GetMapping(path = "/cloud/tenant/{cloudOwner}/{cloudRegion}/{tenantId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCloudTenant(@PathVariable String cloudOwner, @PathVariable String cloudRegion,
            @PathVariable String tenantId) throws IOException {
        return ResponseEntity.ok(cloudService.getCloudTenant(cloudOwner, cloudRegion, tenantId).toString());
    }

    @PutMapping(path = "/cloud/tenants/{cloudOwner}/{cloudRegion}/{tenantId}/{tenantName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createCloudTenant(
            @PathVariable String cloudOwner,
            @PathVariable String cloudRegion,
            @PathVariable String tenantId,
            @PathVariable String tenantName) throws IOException {
        cloudService.createCloudTenant(cloudOwner, cloudRegion, tenantId, tenantName);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/cloud/availability-zones/{cloudOwner}/{cloudRegion}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCloudAvailabilityZones(@PathVariable String cloudOwner,
            @PathVariable String cloudRegion)
            throws IOException {
        return ResponseEntity.ok(cloudService.getCloudAvailabilityZones(cloudOwner, cloudRegion).toString());
    }

    @GetMapping(path = "/cloud/availability-zone/{cloudOwner}/{cloudRegion}/{availabilityZone}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCloudAvailabilityZone(@PathVariable String cloudOwner,
            @PathVariable String cloudRegion,
            @PathVariable String availibilityZone) throws IOException {
        return ResponseEntity
                .ok(cloudService.getCloudAvailabilityZone(cloudOwner, cloudRegion, availibilityZone).toString());
    }

    @PutMapping(path = "/cloud/availability-zone/{cloudOwner}/{cloudRegion}/{availabilityZone}/{hypervisorType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCloudAvailibilityZone(
            @PathVariable String cloudOwner,
            @PathVariable String cloudRegion,
            @PathVariable String azName,
            @PathVariable String azHypervisorType) throws IOException {
        cloudService.createCloudAvailibilityZone(cloudOwner, cloudRegion, azName, azHypervisorType);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/cloud/vserver/{cloudOwner}/{regionName}/{tenantId}/{vServerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getVServerDetail(
            @PathVariable(required = true) String cloudOwner,
            @PathVariable(required = true) String regionName,
            @PathVariable(required = true) String tenantId,
            @PathVariable(required = true) String vServerId) throws IOException {
        return ResponseEntity
                .ok(cloudService.getVServerDetail(cloudOwner, regionName, tenantId, vServerId).toString());
    }

    @GetMapping(path = "/cloud/flavor/{cloudOwner}/{regionName}/{flavorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getFlavorDetail(
            @PathVariable(required = true) String cloudOwner,
            @PathVariable(required = true) String regionName,
            @PathVariable(required = true) String flavorId) throws IOException {
        return ResponseEntity
                .ok(cloudService.getFlavorDetail(cloudOwner, regionName, flavorId).toString());
    }

    @GetMapping(path = "/cloud/k8s/{helmInstId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getK8SInstanceDetail(
            @PathVariable(required = true) String helmInstId) throws IOException {
        return ResponseEntity
                .ok(cloudService.getK8SInstanceDetail(helmInstId).toString());
    }
}
