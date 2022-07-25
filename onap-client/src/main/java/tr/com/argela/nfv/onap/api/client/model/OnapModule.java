/*
# Copyright © 2021 Argela Technologies
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
 */
package tr.com.argela.nfv.onap.api.client.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@Getter
public enum OnapModule {

    AAI("https://${ONAPIP}:30233/aai/v16", new String[][] {
            { "Authorization", "Basic QUFJOkFBSQ==" },
            { "X-FromAppId", "AAI" },
            { "Accept", "application/json" },
            { "X-TransactionId", "get_aai_subscr" },
            { "Content-Type", "application/json" }
    }),
    MSB("https://${ONAPIP}:30283/api/multicloud/v1", new String[][] {
            { "Accept", "application/json" },
            { "Content-Type", "application/json" }
    }),
    SDC_CATALOG("https://${ONAPIP}:30204/sdc/v1/catalog", new String[][] {
            { "Content-Type", "application/json" },
            { "Accept", "application/json" },
            { "X-TransactionId", "robot-ete-ba84612d-c1c6-4c53-9967-7b1dff276c7a" },
            { "USER_ID", "cs0008" },
            { "X-FromAppId", "ONAP-Test" },
            { "Authorization", "Basic YWFpOktwOGJKNFNYc3pNMFdYbGhhazNlSGxjc2UyZ0F3ODR2YW9HR21KdlV5MlU=" },
            { "x-ecomp-instanceid", "ONAP-Test" }, }),
    SDC_FeProxy("https://${ONAPIP}:30207/sdc1/feProxy", new String[][] {
            { "Content-Type", "application/json" },
            { "Accept", "application/json" },
            { "X-TransactionId", "robot-ete-ba84612d-c1c6-4c53-9967-7b1dff276c7a" },
            { "USER_ID", "cs0008" },
            { "X-FromAppId", "robot-ete" },
            { "Cookie", "JSESSIONID=node01w0clju3fse7818yffifda06aw27.node0" }
    }),
    MULTICLOUD_K8S("http://${ONAPIP}:30280/", new String[][] {
            { "Accept", "application/json" } }),
    NBI("https://${ONAPIP}:30274/nbi/api/v4", new String[][] {
            { "Content-Type", "application/json" },
            { "Accept", "application/json" } }),
    SO("http://${ONAPIP}:30277/onap/so", new String[][] {
            { "Content-Type", "application/json" },
            { "Accept", "application/json" },
            { "Authorization", "Basic SW5mcmFQb3J0YWxDbGllbnQ6cGFzc3dvcmQxJA==" },
            { "-ONAP-PartnerName", "NBI" }, }),
    SO_NOTIFICATION("", new String[][] {
            { "Content-Type", "application/json" },
            { "Accept", "application/json" },
            { "Authorization", "Basic SW5mcmFQb3J0YWxDbGllbnQ6cGFzc3dvcmQxJA==" },
            { "-ONAP-PartnerName", "NBI" }, }),
    SDNC("https://${ONAPIP}:30267", new String[][] {
            { "Content-Type", "application/json" },
            { "Accept", "application/json" },
            { "X-FromAppId", "client" },
            { "X-TransactionId", "0a3f6713-ba96-4971-a6f8-c2da85a3176e" },
            { "Authorization", "Basic YWRtaW46S3A4Yko0U1hzek0wV1hsaGFrM2VIbGNzZTJnQXc4NHZhb0dHbUp2VXkyVQ==" }, }),;

    private OnapModule(String api, String[][] data) {
        this.apiURL = api;
        this.headers = new HashMap<>();
        for (String[] object : data) {
            headers.put(object[0], object[1]);
        }
    }

    String apiURL;
    Map<String, String> headers;

}
