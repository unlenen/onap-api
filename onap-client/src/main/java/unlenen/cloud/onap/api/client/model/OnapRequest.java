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
package unlenen.cloud.onap.api.client.model;

import java.util.Map;

import lombok.Getter;
import unlenen.cloud.onap.api.client.http.HttpCallType;
import unlenen.cloud.onap.api.client.http.HttpResponseType;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@Getter
public enum OnapRequest {
    /* CLOUD */

    /* CLOUD Complex */
    CLOUD_COMPLEXS(HttpCallType.GET, OnapModule.AAI, "/cloud-infrastructure/complexes", 200,
            HttpResponseType.JSONObject),
    CLOUD_COMPLEX(HttpCallType.GET, OnapModule.AAI,
            "/cloud-infrastructure/complexes/complex/${" + OnapRequestParameters.CLOUD_COMPLEX_NAME + "}", 200,
            HttpResponseType.JSONObject),
    CLOUD_COMPLEX_CREATE(HttpCallType.PUT, OnapModule.AAI,
            "/cloud-infrastructure/complexes/complex/${" + OnapRequestParameters.CLOUD_COMPLEX_NAME + "}", 201,
            HttpResponseType.STRING, "payloads/cloud/complex_create.json", "application/json"),
    /* CLOUD Regions */
    CLOUD_REGIONS(HttpCallType.GET, OnapModule.AAI, "/cloud-infrastructure/cloud-regions", 200,
            HttpResponseType.JSONObject),
    CLOUD_REGION(HttpCallType.GET, OnapModule.AAI,
            "/cloud-infrastructure/cloud-regions/cloud-region/${" + OnapRequestParameters.CLOUD_OWNER + "}/${"
                    + OnapRequestParameters.CLOUD_NAME + "}",
            200, HttpResponseType.JSONObject),
    CLOUD_REGION_COMPLEX_Relations(HttpCallType.PUT, OnapModule.AAI,
            "/cloud-infrastructure/cloud-regions/cloud-region/${" + OnapRequestParameters.CLOUD_OWNER + "}/${"
                    + OnapRequestParameters.CLOUD_REGION + "}/relationship-list/relationship",
            200, HttpResponseType.STRING, "payloads/cloud/region_complex_relations.json", "application/json"),
    CLOUD_REGION_CREATE(HttpCallType.PUT, OnapModule.AAI,
            "/cloud-infrastructure/cloud-regions/cloud-region/${" + OnapRequestParameters.CLOUD_OWNER + "}/${"
                    + OnapRequestParameters.CLOUD_NAME + "}",
            201, HttpResponseType.STRING, "payloads/cloud/region_openstack_create.json", "application/json"),
    CLOUD_REGION_CREATE_MSB(HttpCallType.POST, OnapModule.MSB,
            "/${" + OnapRequestParameters.CLOUD_OWNER + "}/${" + OnapRequestParameters.CLOUD_NAME + "}/registry", 202,
            HttpResponseType.STRING),
    CLOUD_K8S_MSB_ADD_KUBECONFIG(HttpCallType.POST_FILE, OnapModule.MULTICLOUD_K8S, "/v1/connectivity-info", 201,
            HttpResponseType.STRING),
    /* CLOUD Tenants */
    CLOUD_TENANTS(HttpCallType.GET, OnapModule.AAI,
            "/cloud-infrastructure/cloud-regions/cloud-region/${" + OnapRequestParameters.CLOUD_OWNER + "}/${"
                    + OnapRequestParameters.CLOUD_REGION + "}/tenants",
            200, HttpResponseType.JSONObject),
    CLOUD_TENANT(HttpCallType.GET, OnapModule.AAI,
            "/cloud-infrastructure/cloud-regions/cloud-region/${" + OnapRequestParameters.CLOUD_OWNER + "}/${"
                    + OnapRequestParameters.CLOUD_REGION + "}/tenants/tenant/${" + OnapRequestParameters.CLOUD_TENANT_ID
                    + "}",
            200, HttpResponseType.JSONObject),
    CLOUD_TENANT_CREATE(HttpCallType.PUT, OnapModule.AAI,
            "/cloud-infrastructure/cloud-regions/cloud-region/${" + OnapRequestParameters.CLOUD_OWNER + "}/${"
                    + OnapRequestParameters.CLOUD_REGION + "}/tenants/tenant/${" + OnapRequestParameters.CLOUD_TENANT_ID
                    + "}",
            201, HttpResponseType.STRING, "payloads/cloud/tenant_create.json", "application/json"),
    /* CLOUD AZ */
    CLOUD_AVAILABILITY_ZONES(HttpCallType.GET, OnapModule.AAI,
            "/cloud-infrastructure/cloud-regions/cloud-region/${" + OnapRequestParameters.CLOUD_OWNER + "}/${"
                    + OnapRequestParameters.CLOUD_REGION + "}/availability-zones",
            200, HttpResponseType.JSONObject),
    CLOUD_AVAILABILITY_ZONE(HttpCallType.GET, OnapModule.AAI,
            "/cloud-infrastructure/cloud-regions/cloud-region/${" + OnapRequestParameters.CLOUD_OWNER + "}/${"
                    + OnapRequestParameters.CLOUD_REGION + "}/availability-zones/availability-zone/${"
                    + OnapRequestParameters.CLOUD_AVAILABILITY_ZONE + "}",
            200, HttpResponseType.JSONObject),
    CLOUD_AVAILABILITY_ZONE_CREATE(HttpCallType.PUT, OnapModule.AAI,
            "/cloud-infrastructure/cloud-regions/cloud-region/${" + OnapRequestParameters.CLOUD_OWNER + "}/${"
                    + OnapRequestParameters.CLOUD_REGION + "}/availability-zones/availability-zone/${"
                    + OnapRequestParameters.CLOUD_AVAILABILITY_ZONE + "}",
            201, HttpResponseType.STRING, "payloads/cloud/az_create.json", "application/json"),
    /* CLOUD Other */
    CLOUD_VSERVER_DETAIL(HttpCallType.GET, OnapModule.AAI,
            "/aai/v16/cloud-infrastructure/cloud-regions/cloud-region/${" + OnapRequestParameters.CLOUD_OWNER + "}/${"
                    + OnapRequestParameters.CLOUD_REGION + "}/tenants/tenant/${" + OnapRequestParameters.CLOUD_TENANT_ID
                    + "}/vservers/vserver/${" + OnapRequestParameters.CLOUD_VSERVER_ID + "}",
            200, HttpResponseType.JSONObject),
    CLOUD_VSERVER_FLAVOR_DETAIL(HttpCallType.GET, OnapModule.AAI,
            "/aai/v16/cloud-infrastructure/cloud-regions/cloud-region/${" + OnapRequestParameters.CLOUD_OWNER + "}/${"
                    + OnapRequestParameters.CLOUD_REGION + "}/flavors/flavor/${"
                    + OnapRequestParameters.CLOUD_OS_FLAVOR_ID + "}",
            200, HttpResponseType.JSONObject),
    CLOUD_K8S_INST_DETAIL(HttpCallType.GET, OnapModule.MULTICLOUD_K8S,
            "/v1/instance/${" + OnapRequestParameters.CLOUD_HELM_INST_ID + "}", 200, HttpResponseType.JSONObject),
    /* BUSINESS */
    /* BUSINESS Customers */
    BUSINESS_CUSTOMERS(HttpCallType.GET, OnapModule.AAI, "/business/customers", 200, HttpResponseType.JSONObject),
    BUSINESS_CUSTOMER(HttpCallType.GET, OnapModule.AAI,
            "/business/customers/customer/${" + OnapRequestParameters.BUSINESS_CUSTOMER_ID + "}", 200,
            HttpResponseType.JSONObject),
    BUSINESS_CUSTOMER_CREATE(HttpCallType.PUT, OnapModule.AAI,
            "/business/customers/customer/${" + OnapRequestParameters.BUSINESS_CUSTOMER_ID + "}", 201,
            HttpResponseType.STRING, "payloads/business/customer/create.json", "application/json"),
    BUSINESS_CUSTOMER_DELETE(HttpCallType.DELETE, OnapModule.AAI,
            "/business/customers/customer/${" + OnapRequestParameters.BUSINESS_CUSTOMER_ID + "}?resource-version=${"
                    + OnapRequestParameters.RESOURCE_VERSION + "}",
            204, HttpResponseType.STRING),
    BUSINESS_CUSTOMER_SUBSCRIPTIONS(HttpCallType.GET, OnapModule.AAI,
            "/business/customers/customer/${" + OnapRequestParameters.BUSINESS_CUSTOMER_ID + "}/service-subscriptions",
            200, HttpResponseType.JSONObject),
    BUSINESS_CUSTOMER_SERVICE_SUBSCRIPTION(HttpCallType.GET, OnapModule.AAI,
            "/business/customers/customer/${" + OnapRequestParameters.BUSINESS_CUSTOMER_ID
                    + "}/service-subscriptions/service-subscription/${"
                    + OnapRequestParameters.DESIGN_SERVICE_MODEL_NAME + "}",
            200, HttpResponseType.JSONObject),
    BUSINESS_CUSTOMER_SUBSCRIPTION_CREATE(HttpCallType.PUT, OnapModule.AAI,
            "/business/customers/customer/${" + OnapRequestParameters.BUSINESS_CUSTOMER_ID
                    + "}/service-subscriptions/service-subscription/${"
                    + OnapRequestParameters.DESIGN_SERVICE_MODEL_NAME + "}",
            201, HttpResponseType.STRING, "payloads/business/subscription/customerToService.json", "application/json"),
    BUSINESS_CUSTOMER_TENANT_SUBSCRIPTION_CREATE(HttpCallType.PUT, OnapModule.AAI,
            "/business/customers/customer/${" + OnapRequestParameters.BUSINESS_CUSTOMER_ID
                    + "}/service-subscriptions/service-subscription/${"
                    + OnapRequestParameters.DESIGN_SERVICE_MODEL_NAME + "}/relationship-list/relationship",
            200, HttpResponseType.STRING, "payloads/business/subscription/customerToTenant.json", "application/json"),
    /* BUSINESS Owning Entity */
    BUSINESS_OWNING_ENTITIES(HttpCallType.GET, OnapModule.AAI, "/business/owning-entities", 200,
            HttpResponseType.JSONObject),
    BUSINESS_OWNING_ENTITY(HttpCallType.GET, OnapModule.AAI,
            "/business/owning-entities/owning-entity/${" + OnapRequestParameters.BUSINESS_OWNING_ENTITY_ID + "}", 200,
            HttpResponseType.JSONObject),
    BUSINESS_OWNING_ENTITY_CREATE(HttpCallType.PUT, OnapModule.AAI,
            "/business/owning-entities/owning-entity/${" + OnapRequestParameters.BUSINESS_OWNING_ENTITY_ID + "}", 201,
            HttpResponseType.STRING, "payloads/business/general/owning_entity_create.json", "application/json"),
    /* BUSINESS Project */
    BUSINESS_PROJECTS(HttpCallType.GET, OnapModule.AAI, "/business/projects", 200, HttpResponseType.JSONObject),
    BUSINESS_PROJECT(HttpCallType.GET, OnapModule.AAI,
            "/business/projects/project/${" + OnapRequestParameters.BUSINESS_PROJECT_NAME + "}", 200,
            HttpResponseType.JSONObject),
    BUSINESS_PROJECT_CREATE(HttpCallType.PUT, OnapModule.AAI,
            "/business/projects/project/${" + OnapRequestParameters.BUSINESS_PROJECT_NAME + "}", 201,
            HttpResponseType.STRING, "payloads/business/general/project_create.json", "application/json"),
    /* BUSINESS Platform */
    BUSINESS_PLATFORMS(HttpCallType.GET, OnapModule.AAI, "/business/platforms", 200, HttpResponseType.JSONObject),
    BUSINESS_PLATFORM(HttpCallType.GET, OnapModule.AAI,
            "/business/platforms/platform/${" + OnapRequestParameters.BUSINESS_PLATFORM_NAME + "}", 200,
            HttpResponseType.JSONObject),
    BUSINESS_PLATFORM_CREATE(HttpCallType.PUT, OnapModule.AAI,
            "/business/platforms/platform/${" + OnapRequestParameters.BUSINESS_PLATFORM_NAME + "}", 201,
            HttpResponseType.STRING, "payloads/business/general/platform_create.json", "application/json"),
    /* BUSINESS Platform */
    BUSINESS_LineOfBusinesses(HttpCallType.GET, OnapModule.AAI, "/business/lines-of-business", 200,
            HttpResponseType.JSONObject),
    BUSINESS_LineOfBusiness(HttpCallType.GET, OnapModule.AAI,
            "/business/lines-of-business/line-of-business/${" + OnapRequestParameters.BUSINESS_LINE_OF_BUSINESS + "}",
            200, HttpResponseType.JSONObject),
    BUSINESS_LineOfBusiness_CREATE(HttpCallType.PUT, OnapModule.AAI,
            "/business/lines-of-business/line-of-business/${" + OnapRequestParameters.BUSINESS_LINE_OF_BUSINESS + "}",
            201, HttpResponseType.STRING, "payloads/business/general/line_of_business_create.json", "application/json"),
    /* SDC */
    /* SDC VENDOR */
    SDC_VENDORS(HttpCallType.GET, OnapModule.SDC_FeProxy, "/onboarding-api/v1.0/vendor-license-models", 200,
            HttpResponseType.JSONObject),
    SDC_VENDOR_VERSION(HttpCallType.GET, OnapModule.SDC_FeProxy,
            "/onboarding-api/v1.0/items/${" + OnapRequestParameters.DESIGN_VENDOR_ID + "}/versions/", 200,
            HttpResponseType.JSONObject),
    SDC_VENDOR_CREATE(HttpCallType.POST, OnapModule.SDC_FeProxy, "/onboarding-api/v1.0/vendor-license-models", 200,
            HttpResponseType.JSONObject, "payloads/design/vendor/create.json", "application/json"),
    SDC_VENDOR_SUBMIT(HttpCallType.PUT, OnapModule.SDC_FeProxy,
            "/onboarding-api/v1.0/vendor-license-models/${" + OnapRequestParameters.DESIGN_VENDOR_ID + "}/versions/${"
                    + OnapRequestParameters.DESIGN_VENDOR_VERSION_ID + "}/actions",
            200, HttpResponseType.JSONObject, "payloads/design/vendor/submit.json", "application/json"),
    /* SDC VSP */
    SDC_VSPS(HttpCallType.GET, OnapModule.SDC_FeProxy, "/onboarding-api/v1.0/vendor-software-products", 200,
            HttpResponseType.JSONObject),
    SDC_VSP_VERSION(HttpCallType.GET, OnapModule.SDC_FeProxy,
            "/onboarding-api/v1.0/items/${" + OnapRequestParameters.DESIGN_VSP_ID + "}/versions", 200,
            HttpResponseType.JSONObject),
    SDC_VSP_CREATE(HttpCallType.POST, OnapModule.SDC_FeProxy, "/onboarding-api/v1.0/vendor-software-products", 200,
            HttpResponseType.JSONObject, "payloads/design/vsp/create.json", "application/json"),
    SDC_VSP_UPLOAD_FILE(HttpCallType.POST_FILE, OnapModule.SDC_FeProxy,
            "/onboarding-api/v1.0/vendor-software-products/${" + OnapRequestParameters.DESIGN_VSP_ID + "}/versions/${"
                    + OnapRequestParameters.DESIGN_VSP_VERSION_ID + "}/orchestration-template-candidate",
            200, HttpResponseType.JSONObject),
    SDC_VSP_PROCESS_FILE(HttpCallType.PUT, OnapModule.SDC_FeProxy,
            "/onboarding-api/v1.0/vendor-software-products/${" + OnapRequestParameters.DESIGN_VSP_ID + "}/versions/${"
                    + OnapRequestParameters.DESIGN_VSP_VERSION_ID + "}/orchestration-template-candidate/process",
            200, HttpResponseType.JSONObject, "payloads/design/vsp/process.json", "application/json"),
    SDC_VSP_COMMIT(HttpCallType.PUT, OnapModule.SDC_FeProxy,
            "/onboarding-api/v1.0/vendor-software-products/${" + OnapRequestParameters.DESIGN_VSP_ID + "}/versions/${"
                    + OnapRequestParameters.DESIGN_VSP_VERSION_ID + "}/actions",
            200, HttpResponseType.JSONObject, "payloads/design/vsp/commit.json", "application/json"),
    SDC_VSP_SUBMIT(HttpCallType.PUT, OnapModule.SDC_FeProxy,
            "/onboarding-api/v1.0/vendor-software-products/${" + OnapRequestParameters.DESIGN_VSP_ID + "}/versions/${"
                    + OnapRequestParameters.DESIGN_VSP_VERSION_ID + "}/actions",
            200, HttpResponseType.JSONObject, "payloads/design/vsp/submit.json", "application/json"),
    SDC_VSP_CSAR(HttpCallType.PUT, OnapModule.SDC_FeProxy,
            "/onboarding-api/v1.0/vendor-software-products/${" + OnapRequestParameters.DESIGN_VSP_ID + "}/versions/${"
                    + OnapRequestParameters.DESIGN_VSP_VERSION_ID + "}/actions",
            200, HttpResponseType.JSONObject, "payloads/design/vsp/csar.json", "application/json"),
    /* SDC VF */
    SDC_VFS(HttpCallType.GET, OnapModule.SDC_CATALOG, "/resources?resourceType=VF", 200, HttpResponseType.JSONArray),
    SDC_VF_CREATE(HttpCallType.POST, OnapModule.SDC_FeProxy, "/rest/v1/catalog/resources", 201,
            HttpResponseType.JSONObject, "payloads/design/vf/create.json", "application/json"),
    SDC_UNIQUE_ID(HttpCallType.GET, OnapModule.SDC_FeProxy,
            "/rest/v1/screen?excludeTypes=VFCMT&excludeTypes=Configuration", 200, HttpResponseType.JSONObject),
    SDC_VF_CHECKIN(HttpCallType.POST, OnapModule.SDC_CATALOG,
            "/resources/${" + OnapRequestParameters.DESIGN_VF_UUID + "}/lifecycleState/checkin", 201,
            HttpResponseType.JSONObject, "payloads/design/vf/checkin.json", "application/json"),
    SDC_VF_CERTIFY(HttpCallType.POST, OnapModule.SDC_FeProxy,
            "/rest/v1/catalog/resources/${" + OnapRequestParameters.DESIGN_VF_UNIQUE_ID + "}/lifecycleState/certify",
            200, HttpResponseType.JSONObject, "payloads/design/vf/certify.json", "application/json"),
    /* SDC SERVICE MODEL */
    SDC_SERVICE_MODELS(HttpCallType.GET, OnapModule.SDC_CATALOG, "/services", 200, HttpResponseType.JSONArray),
    SDC_SERVICE_MODEL_DETAIL(HttpCallType.GET, OnapModule.SDC_FeProxy,
            "/rest/v1/catalog/services/${" + OnapRequestParameters.DESIGN_SERVICE_MODEL_UNIQUE_ID + "}/${"
                    + OnapRequestParameters.DESIGN_SERVICE_MODEL_FILTER + "}",
            200, HttpResponseType.JSONObject),
    SDC_SERVICE_MODEL_CREATE(HttpCallType.POST, OnapModule.SDC_FeProxy, "/rest/v1/catalog/services", 201,
            HttpResponseType.JSONObject, "payloads/design/service/create.json", "application/json"),
    SDC_SERVICE_MODEL_ADD_VF(HttpCallType.POST, OnapModule.SDC_FeProxy,
            "/rest/v1/catalog/services/${" + OnapRequestParameters.DESIGN_SERVICE_MODEL_UNIQUE_ID
                    + "}/resourceInstance",
            201, HttpResponseType.JSONObject, "payloads/design/service/vfAdd.json", "application/json"),
    SDC_SERVICE_MODEL_CERTIFY(HttpCallType.POST, OnapModule.SDC_FeProxy,
            "/rest/v1/catalog/services/${" + OnapRequestParameters.DESIGN_SERVICE_MODEL_UNIQUE_ID
                    + "}/lifecycleState/certify",
            200, HttpResponseType.JSONObject, "payloads/design/service/certify.json", "application/json"),
    SDC_SERVICE_MODEL_DISTRIBUTE(HttpCallType.POST, OnapModule.SDC_FeProxy,
            "/rest/v1/catalog/services/${" + OnapRequestParameters.DESIGN_SERVICE_MODEL_UNIQUE_ID
                    + "}/distribution/PROD/activate",
            200, HttpResponseType.JSONObject, "payloads/design/service/distribute.json", "application/json"),
    SDC_SERVICE_MODEL_DISTRIBUTE_LIST(HttpCallType.GET, OnapModule.SDC_FeProxy,
            "/rest/v1/catalog/services/${" + OnapRequestParameters.DESIGN_SERVICE_MODEL_UUID + "}/distribution", 200,
            HttpResponseType.JSONObject),
    SDC_SERVICE_MODEL_DISTRIBUTE_DETAIL(HttpCallType.GET, OnapModule.SDC_FeProxy,
            "/rest/v1/catalog/services/distribution/${" + OnapRequestParameters.DESIGN_SERVICE_MODEL_DISTRIBUTION_ID
                    + "}",
            200, HttpResponseType.JSONObject),
    AAI_SERVICE_MODEL_SUBSCRIPTIONS(HttpCallType.GET, OnapModule.AAI,
            "/service-design-and-creation/services/service/${"
                    + OnapRequestParameters.DESIGN_SERVICE_MODEL_InvariantUUID + "}",
            200, HttpResponseType.JSONObject),
    AAI_SERVICE_MODEL_SUBSCRIPTION_CREATE(HttpCallType.PUT, OnapModule.AAI,
            "/service-design-and-creation/services/service/${"
                    + OnapRequestParameters.DESIGN_SERVICE_MODEL_InvariantUUID + "}",
            201, HttpResponseType.STRING, "payloads/business/subscription/serviceCreate.json", "application/json"),
    /* RUNTIME */
    RUNTIME_ACTION_STATUS(HttpCallType.GET, OnapModule.SO_NOTIFICATION, "${NOTIFICATION_URL}", 200,
            HttpResponseType.JSONObject),
    RUNTIME_SERVICE_INSTANCES(HttpCallType.GET, OnapModule.NBI,
            "/service?relatedParty.id=${" + OnapRequestParameters.BUSINESS_CUSTOMER_ID + "}", 200,
            HttpResponseType.JSONArray),
    RUNTIME_SERVICE_INSTANCE_CREATE(HttpCallType.POST, OnapModule.SO, "/infra/serviceInstantiation/v7/serviceInstances",
            202, HttpResponseType.JSONObject, "payloads/runtime/serviceInstance/create.json", "application/json"),
    RUNTIME_SERVICE_INSTANCE_DELETE(HttpCallType.POST, OnapModule.SO,
            "/infra/serviceInstantiation/v7/serviceInstances/${" + OnapRequestParameters.RUNTIME_SERVICE_INSTANCE_ID
                    + "}",
            202, HttpResponseType.JSONObject, "payloads/runtime/serviceInstance/delete.json", "application/json"),
    RUNTIME_SERVICE_INSTANCE_DETAIL(HttpCallType.GET, OnapModule.NBI,
            "/service/${" + OnapRequestParameters.RUNTIME_SERVICE_INSTANCE_ID + "}", 200, HttpResponseType.JSONObject),
    RUNTIME_VNFS(HttpCallType.GET, OnapModule.AAI, "/network/generic-vnfs", 200, HttpResponseType.JSONObject),
    RUNTIME_VNF_CREATE(HttpCallType.POST, OnapModule.SO,
            "/infra/serviceInstantiation/v7/serviceInstances/${" + OnapRequestParameters.RUNTIME_SERVICE_INSTANCE_ID
                    + "}/vnfs",
            202, HttpResponseType.JSONObject, "payloads/runtime/vnf/create.json", "application/json"),
    RUNTIME_VNF_DELETE(HttpCallType.DELETE, OnapModule.SO,
            "/infra/serviceInstantiation/v7/serviceInstances/${" + OnapRequestParameters.RUNTIME_SERVICE_INSTANCE_ID
                    + "}/vnfs/${" + OnapRequestParameters.RUNTIME_VNF_ID + "}",
            202, HttpResponseType.JSONObject, "payloads/runtime/vnf/delete.json", "application/json"),
    RUNTIME_VNFS_DETAIL_BY_SERVICE(HttpCallType.GET, OnapModule.AAI,
            "/network/generic-vnfs?${" + OnapRequestParameters.REQUEST_PARAMETERS + "}", 200,
            HttpResponseType.JSONObject),
    RUNTIME_VNF_DETAIL(HttpCallType.GET, OnapModule.AAI,
            "/network/generic-vnfs/generic-vnf/${" + OnapRequestParameters.RUNTIME_VNF_ID + "}", 200,
            HttpResponseType.JSONObject),
    RUNTIME_VFMODULES(HttpCallType.GET, OnapModule.AAI,
            "/network/generic-vnfs/generic-vnf/${" + OnapRequestParameters.RUNTIME_VNF_ID + "}/vf-modules", 200,
            HttpResponseType.JSONObject),
    RUNTIME_VFMODULE_CREATE(HttpCallType.POST, OnapModule.SO,
            "/infra/serviceInstantiation/v7/serviceInstances/${" + OnapRequestParameters.RUNTIME_SERVICE_INSTANCE_ID
                    + "}/vnfs/${" + OnapRequestParameters.RUNTIME_VNF_ID + "}/vfModules",
            202, HttpResponseType.JSONObject, "payloads/runtime/vfModule/create.json", "application/json"),
    RUNTIME_VFMODULE_DELETE(HttpCallType.DELETE, OnapModule.SO,
            "/infra/serviceInstantiation/v7/serviceInstances/${" + OnapRequestParameters.RUNTIME_SERVICE_INSTANCE_ID
                    + "}/vnfs/${" + OnapRequestParameters.RUNTIME_VNF_ID + "}/vfModules/${"
                    + OnapRequestParameters.RUNTIME_VF_MODULE_ID + "}",
            202, HttpResponseType.JSONObject, "payloads/runtime/vfModule/delete.json", "application/json"),
    RUNTIME_VFMODULE_PRELOAD(HttpCallType.POST, OnapModule.SDNC,
            "/restconf/operations/GENERIC-RESOURCE-API:preload-vf-module-topology-operation", 200,
            HttpResponseType.JSONObject, "payloads/runtime/vfModule/preload.json", "application/json"),
    RUNTIME_VFMODULE_DETAIL(HttpCallType.GET, OnapModule.AAI,
            "/network/generic-vnfs/generic-vnf/${" + OnapRequestParameters.RUNTIME_VNF_ID + "}/vf-modules/vf-module/${"
                    + OnapRequestParameters.RUNTIME_VF_MODULE_ID + "}",
            200, HttpResponseType.JSONObject),
    RUNTIME_VFMODULE_INSTANTIATE_DETAIL(HttpCallType.GET, OnapModule.SO,
            "/infra/orchestrationRequests/v7?filter=vfModuleInstanceId:EQUALS:${"
                    + OnapRequestParameters.RUNTIME_VF_MODULE_ID + "}",
            200, HttpResponseType.JSONObject),
    RUNTIME_VFMODULE_TOPOLOGY(HttpCallType.GET, OnapModule.SDNC,
            "/restconf/config/GENERIC-RESOURCE-API:services/service/${"
                    + OnapRequestParameters.RUNTIME_SERVICE_INSTANCE_ID + "}/service-data/vnfs/vnf/${"
                    + OnapRequestParameters.RUNTIME_VNF_ID + "}/vnf-data/vf-modules/vf-module/${"
                    + OnapRequestParameters.RUNTIME_VF_MODULE_ID + "}/vf-module-data/vf-module-topology/",
            200, HttpResponseType.JSONObject),;

    private OnapRequest(HttpCallType callType, OnapModule onapModule, String url, int validReturnCode,
            HttpResponseType responseType) {
        this.callType = callType;
        this.onapModule = onapModule;
        this.url = url;
        this.validReturnCode = validReturnCode;
        this.responseType = responseType;
    }

    private OnapRequest(HttpCallType callType, OnapModule onapModule, String url, int validReturnCode,
            HttpResponseType responseType, String payloadFilePath, String payloadFileType) {
        this.callType = callType;
        this.onapModule = onapModule;
        this.url = url;
        this.validReturnCode = validReturnCode;
        this.responseType = responseType;
        this.payloadFilePath = payloadFilePath;
        this.payloadFileType = payloadFileType;
    }

    HttpCallType callType;
    OnapModule onapModule;
    String url;
    int validReturnCode;
    HttpResponseType responseType;
    String payloadFilePath;
    String payloadFileType;

    public String getEndpoint(Map<String, String> parameters) {
        String mainURL = getOnapModule().getApiURL() + getUrl();
        for (String key : parameters.keySet()) {
            mainURL = mainURL.replaceAll("\\$\\{" + key + "\\}", parameters.get(key));
        }
        return mainURL;
    }

    @Override
    public String toString() {
        return "[" + this.name() + "]";
    }

}
