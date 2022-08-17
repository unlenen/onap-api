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
package unlenen.cloud.onap.api.exception;

import java.io.IOException;

import lombok.Getter;
import unlenen.cloud.onap.api.client.model.OnapRequest;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@Getter
public class OnapRequestFailedException extends IOException {

    OnapRequest onapRequest;
    String url;
    int responseCode;

    public OnapRequestFailedException(OnapRequest onapRequest) {
        this.onapRequest = onapRequest;
    }

    public OnapRequestFailedException(OnapRequest onapRequest, String url, int responseCode, String msg) {
        super("request:" + onapRequest + ",url:" + url + ",responseCode:" + responseCode + " , msg:" + msg);
        this.onapRequest = onapRequest;
        this.url = url;
        this.responseCode = responseCode;
    }

    public OnapRequestFailedException(OnapRequest onapRequest, String string, Throwable thrwbl) {
        super(string, thrwbl);
        this.onapRequest = onapRequest;
    }

}
