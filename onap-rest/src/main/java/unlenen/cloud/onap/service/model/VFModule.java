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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import unlenen.cloud.onap.api.client.model.VFModuleProfile;
import unlenen.cloud.onap.service.model.serverInstance.Server;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@Getter
@Setter
public class VFModule {

    String id;
    String name;
    String type;
    String reqId;
    String reqUrl;
    String availabilityZone;
    Server server;

    VFModel vfModel;

    VFModuleProfile profile;

    @JsonIgnore
    VNF vnf;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("VFModule{id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", availabilityZone=").append(availabilityZone);
        sb.append('}');
        return sb.toString();
    }

}
