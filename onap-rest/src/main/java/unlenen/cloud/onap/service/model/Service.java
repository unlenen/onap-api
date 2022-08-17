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
package  unlenen.cloud.onap.service.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import unlenen.cloud.onap.service.constant.DistributionStatus;
import unlenen.cloud.onap.service.constant.EntityStatus;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@Getter
@Setter
public class Service {

    String uuid;
    String uniqueId;
    String invariantUUID;
    String name, description, versionName;
    List<VF> vfs;
    EntityStatus versionStatus;
    DistributionStatus distributionStatus;
    List<Customer> customers;
    List<Tenant> tenants;
    List<ServiceInstance> serviceInstances;

    @JsonIgnore
    Map<String, VF> vfMapByModelName = new HashMap<>();
    @JsonIgnore
    Map<String, VF> vfMapByName = new HashMap<>();

    @JsonIgnore
    Scenario scenario;

    public void mapVfs() {
        vfs.forEach((vf) -> {
            vfMapByModelName.put(vf.getModelName(), vf);
            vfMapByName.put(vf.getName(), vf);
        });

    }

    public VF getVFByModelName(String modelName) {
        return vfMapByModelName.get(modelName);
    }

    public VF getVFByName(String name) {
        return vfMapByName.get(name);
    }

    @Override
    public String toString() {
        return "Service{" + "uuid=" + uuid + ", uniqueId=" + uniqueId + ", invariantUUID=" + invariantUUID + ", name="
                + name + '}';
    }

}
