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
package unlenen.cloud.onap.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = { "service" })
public class Customer {

    String id;
    String name;
    String versionId = "";
    String type = "";

    @JsonIgnore
    Service service;

    public Customer(String id, String name, String versionId, String type) {
        this.id = id;
        this.name = name;
        this.versionId = versionId;
        this.type = type;
    }

    public void copy(Customer customerOnap) {
        this.id = customerOnap.getId();
        this.name = customerOnap.getName();
        this.versionId = customerOnap.getVersionId();
        this.type = customerOnap.getType();
    }

}
