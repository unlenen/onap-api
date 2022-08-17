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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import unlenen.cloud.onap.api.service.DesignService;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@RestController

public class DesignController {

    @Autowired
    DesignService designService;

    @GetMapping(path = "/design/vendors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getVendors() throws Exception {
        return ResponseEntity.ok(designService.getVendors().toString());
    }

    @PutMapping(path = "/design/vendor/{vendorName}/{vendorDescription}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createVendor(@PathVariable String vendorName,
            @PathVariable(required = false) String vendorDescription) throws Exception {
        return ResponseEntity.ok(designService.createVendor(vendorName, vendorDescription).toString());
    }

    @GetMapping(path = "/design/vendor-version/{vendorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getVendorVersion(@PathVariable String vendorId) throws Exception {
        return ResponseEntity.ok(designService.getVendorVersion(vendorId).toString());
    }

    @PutMapping(path = "/design/vendor-submit/{vendorId}/{vendorVersionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> submitVendor(@PathVariable String vendorId, @PathVariable String vendorVersionId)
            throws Exception {
        return ResponseEntity.ok(designService.submitVendor(vendorId, vendorVersionId).toString());
    }

    @GetMapping(path = "/design/vsps", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getVSPs() throws Exception {
        return ResponseEntity.ok(designService.getVSPs().toString());
    }

    @GetMapping(path = "/design/vsp-versions/{vspId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getVSPVersion(@PathVariable String vspId) throws Exception {
        return ResponseEntity.ok(designService.getVSPVersion(vspId).toString());
    }

    @PutMapping(path = "/design/vsp/{vendorId}/{vendorName}/{vspName}/{vspDescription}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createVsp(@PathVariable String vendorId, @PathVariable String vendorName,
            @PathVariable String vspName, @PathVariable String vspDescription) throws Exception {
        return ResponseEntity.ok(designService.createVsp(vendorId, vendorName, vspName, vspDescription).toString());
    }

    @PutMapping(path = "/design/vsp-file-upload/{vspId}/{vspVersionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadVSPFile(@PathVariable String vspId, @PathVariable String vspVersionId,
            @RequestParam(name = "vspFileLocalPath") String vspFileLocalPath) throws Exception {
        return ResponseEntity.ok(designService.uploadVSPFile(vspId, vspVersionId, vspFileLocalPath).toString());
    }

    @PutMapping(path = "/design/vsp-file-process/{vspId}/{vspVersionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> processVSPFile(@PathVariable String vspId, @PathVariable String vspVersionId)
            throws Exception {
        return ResponseEntity.ok(designService.processVSPFile(vspId, vspVersionId).toString());
    }

    @PutMapping(path = "/design/vsp-commit/{vspId}/{vspVersionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> commitVSP(@PathVariable String vspId, @PathVariable String vspVersionId)
            throws Exception {
        return ResponseEntity.ok(designService.commitVSP(vspId, vspVersionId).toString());
    }

    @PutMapping(path = "/design/vsp-submit/{vspId}/{vspVersionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> submitVSP(@PathVariable String vspId, @PathVariable String vspVersionId)
            throws Exception {
        return ResponseEntity.ok(designService.submitVSP(vspId, vspVersionId).toString());
    }

    @PutMapping(path = "/design/vsp-csar/{vspId}/{vspVersionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> csarVSP(@PathVariable String vspId, @PathVariable String vspVersionId)
            throws Exception {
        return ResponseEntity.ok(designService.csarVSP(vspId, vspVersionId).toString());
    }

    @GetMapping(path = "/design/vfs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getVFs() throws Exception {
        return ResponseEntity.ok(designService.getVFs().toString());
    }

    @PutMapping(path = "/design/vf/{vendorName}/{vspId}/{vspVersionName}/{vfName}/{vfDescription}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createVF(@PathVariable String vendorName, String vspId, String vspVersionName,
            String vfName,
            String vfDescription) throws Exception {
        return ResponseEntity
                .ok(designService.createVF(vendorName, vspId, vspVersionName, vfName, vfDescription).toString());
    }

    @GetMapping(path = "/design/vf-uniqueId/{vfUUID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getVFUniqueId(@PathVariable String vfUUID) throws Exception {
        return ResponseEntity.ok(designService.getVFUniqueId(vfUUID).toString());
    }

    @PutMapping(path = "/design/vf-checkIn/{vfUUID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> checkInVF(@PathVariable String vfUUID) throws Exception {
        return ResponseEntity.ok(designService.checkInVF(vfUUID).toString());
    }

    @PutMapping(path = "/design/vf-certify/{vfUniqueId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> certifyVF(@PathVariable String vfUniqueId) throws Exception {
        return ResponseEntity.ok(designService.certifyVF(vfUniqueId).toString());
    }

    @GetMapping(path = "/design/service-models", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getServiceModels() throws Exception {
        return ResponseEntity.ok(designService.getServiceModels().toString());
    }

    @PutMapping(path = "/design/service-model/{serviceName}/{serviceDescription}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createServiceModel(@PathVariable String serviceName,
            @PathVariable String serviceDescription)
            throws Exception {
        return ResponseEntity.ok(designService.createServiceModel(serviceName, serviceDescription).toString());
    }

    @GetMapping(path = "/design/service-uniqueId/{serviceUUID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getServiceModelUniqueId(@PathVariable String serviceUUID) throws Exception {
        return ResponseEntity.ok(designService.getServiceModelUniqueId(serviceUUID).toString());
    }

    @PutMapping(path = "/design/service-model/{serviceUniqueId}/{vfUniqueId}/{vfName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addVFtoServiceModel(@PathVariable String serviceUniqueId,
            @PathVariable String vfUniqueId,
            @PathVariable String vfName, @RequestParam(name = "index", defaultValue = "1") int index)
            throws Exception {
        return ResponseEntity
                .ok(designService.addVFtoServiceModel(serviceUniqueId, vfUniqueId, vfName, index).toString());
    }

    @PutMapping(path = "/design/service-model-certify/{serviceUniqueId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> certifyServiceModel(@PathVariable String serviceUniqueId) throws Exception {
        return ResponseEntity.ok(designService.certifyServiceModel(serviceUniqueId).toString());
    }

    @PutMapping(path = "/design/service-model-distribute/{serviceUniqueId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> distributeServiceModel(@PathVariable String serviceUniqueId) throws Exception {
        return ResponseEntity.ok(designService.certifyServiceModel(serviceUniqueId).toString());
    }

    @GetMapping(path = "/design/service-model-distribution/{serviceUUID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getServiceModelDistributions(@PathVariable String serviceUUID) throws Exception {
        return ResponseEntity.ok(designService.getServiceModelDistributions(serviceUUID).toString());
    }

    @GetMapping(path = "/design/service-model-distribution-detail/{distributionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getServiceModelDistributionDetail(@PathVariable String distributionId)
            throws Exception {
        return ResponseEntity.ok(designService.getServiceModelDistributionDetail(distributionId).toString());
    }

    @GetMapping(path = "/design/service-model/{serviceUniqueId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getServiceModelDetail(@PathVariable String serviceUniqueId,
            @RequestParam(name = "filter", required = false) String filter) {
        return ResponseEntity.ok(designService.getServiceModelDetail(serviceUniqueId, filter).toString());
    }

}
