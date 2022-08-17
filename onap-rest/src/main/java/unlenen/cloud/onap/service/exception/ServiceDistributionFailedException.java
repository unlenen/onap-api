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
package unlenen.cloud.onap.service.exception;

import lombok.Getter;
import unlenen.cloud.onap.api.exception.OnapException;
import unlenen.cloud.onap.service.model.Service;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@Getter
public class ServiceDistributionFailedException extends OnapException {

    Service service;
    String distributionId;
    String component;
    String status;

    public ServiceDistributionFailedException(Service service, String distributionId, String component, String status) {
        super("Service could not be distributed ."+service);
        this.service = service;
        this.distributionId = distributionId;
        this.component = component;
        this.status = status;
        
    }

}
