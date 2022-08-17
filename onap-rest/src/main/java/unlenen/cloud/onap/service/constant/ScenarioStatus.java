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
package unlenen.cloud.onap.service.constant;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
public enum ScenarioStatus {
    INIT,
    PROFILE_PARSING,
    PROFILE_PARSED,
    VENDOR_CREATING,
    VENDOR_CREATED,
    VENDOR_FOUND,
    VENDOR_SUBMITTED,
    VSP_FOUND,
    VSP_CREATING,
    VSP_CREATED,
    VSP_ARTIFACT_UPLOADED,
    VSP_ARTIFACT_PROCESSED,
    VSP_SUBMIT,
    VSP_TO_CSAR,
    CLOUD_COMPLEX_CREATING,
    CLOUD_COMPLEX_CREATED,
    CLOUD_COMPLEX_FOUND,
    CLOUD_REGION_CREATING,
    CLOUD_REGION_CREATED,
    CLOUD_REGION_FOUND,
    
    CLOUD_TENANT_FOUND,
    CLOUD_TENANT_CREATED,

    CLOUD_AZ_FOUND,
    CLOUD_AZ_CREATED,

    
    SERVICE_VF_CREATED,
    SERVICE_VF_FOUND,
    SERVICE_VF_CHECKIN,
    SERVICE_VF_CERTIFIED,

    SERVICE_FOUND,
    SERVICE_CREATING,
    SERVICE_CREATED,
    SERVICE_VF_ADDED,
    SERVICE_CERTIFIED,
    SERVICE_DISTRIBUTE_STARTED,
    SERVICE_DISTRIBUTE_COMPLETED,

    SERVICE_SUBSCRIPTION_COMPLETED,
    CUSTOMER_CREATED,
    CUSTOMER_FOUND,

    SERVICE_SUBSCRIPTION_CUSTOMER_FOUND,
    SERVICE_SUBSCRIPTION_CUSTOMER_CREATED,

    SERVICE_SUBSCRIPTION_CUSTOMER_TENANT_FOUND,
    SERVICE_SUBSCRIPTION_CUSTOMER_TENANT_CREATED,


    SERVICE_INSTANCE_FOUND,
    SERVICE_INSTANCE_CREATE_REQUESTED,
    SERVICE_INSTANCE_CREATED,

    VNF_FOUND,
    VNF_CREATE_REQUESTED,
    VNF_CREATED,

    VF_MODULE_FOUND,
    VF_MODULE_PRELOAD,
    VF_MODULE_CREATE_REQUESTED,
    VF_MODULE_CREATED,
    ;

}
