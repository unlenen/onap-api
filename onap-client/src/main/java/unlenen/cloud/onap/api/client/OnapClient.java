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
package unlenen.cloud.onap.api.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import lombok.Getter;
import unlenen.cloud.onap.api.client.http.URLConnectionService;
import unlenen.cloud.onap.api.client.model.OnapRequest;
import unlenen.cloud.onap.api.config.OnapAPIConfig;
import unlenen.cloud.onap.api.exception.OnapRequestFailedException;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@Service
@Getter
public class OnapClient {

    @Autowired
    OnapAPIConfig config;

    @Autowired
    URLConnectionService urlConnectionService;

    Logger log = LoggerFactory.getLogger(OnapClient.class);

    public Object call(OnapRequest request) throws IOException {
        return call(request, new HashMap<>());
    }

    public Object call(OnapRequest request, Map<String, String> parameters) {
        return call(request, parameters, null);
    }

    public Object call(OnapRequest request, Map<String, String> parameters, Map<String, Object> files) {
        return call(request, parameters, files, request.getPayloadFilePath());
    }

    public Object call(OnapRequest request, Map<String, String> parameters, Map<String, Object> files, String payload) {
        parameters.put("ONAPIP", config.getOnapIPAddress());
        String url = request.getEndpoint(parameters);
        try {
            return urlConnectionService.call(request, url, parameters, files, payload);
        } catch (Throwable ex) {
            return handleError(request, ex, url, parameters);
        }
    }

    private Object handleError(OnapRequest request, Throwable ex, String url, Map<String, String> parameters)
            throws JSONException {

        String responseCode = "<NONE>";
        JSONObject detailObj = new JSONObject();
        if (ex instanceof OnapRequestFailedException) {
            OnapRequestFailedException onapEx = (OnapRequestFailedException) ex;
            url = onapEx.getUrl();
            responseCode = onapEx.getResponseCode() + "";
        } else if (ex instanceof org.springframework.web.client.HttpClientErrorException) {
            org.springframework.web.client.HttpClientErrorException httpEx = (org.springframework.web.client.HttpClientErrorException) ex;
            responseCode = httpEx.getStatusCode() + "";
            try {
                detailObj = new JSONObject(httpEx.getResponseBodyAsString());
            } catch (Exception e) {
            }
        } else if (ex instanceof HttpServerErrorException) {
            HttpServerErrorException httpEx = (HttpServerErrorException) ex;
            responseCode = httpEx.getStatusCode() + "";
            detailObj = new JSONObject(httpEx.getResponseBodyAsString());
        }

        JSONObject error = new JSONObject();
        error.put("msg-type", request.name());
        error.put("url", url);
        error.put("responseCode", responseCode);
        error.put("msg", ex.getMessage());
        error.put("details", detailObj);

        JSONObject reqParameters = new JSONObject();
        error.put("request-parameters", reqParameters);
        for (String key : parameters.keySet()) {
            reqParameters.put(key, parameters.get(key));
        }

        if (!"404 NOT_FOUND".equals(responseCode)) {
            log.error("[Error][CallingOnapAPI] " + request + " , msg : " + ex.getMessage(), ex);
        }
        switch (request.getResponseType()) {
            case JSONObject: {
                JSONObject root = new JSONObject();
                root.put("error", error);
                return root;
            }
            case JSONArray: {
                JSONArray root = new JSONArray();
                root.put(error);
                return root;
            }
            default:
            case STRING: {
                return error.toString();
            }
        }
    }

    public Object getResponseItem(JSONObject data, String item) {
        if (data == null || !data.has(item)) {
            return null;
        }
        return data.get(item);
    }

    public int getResponseSize(JSONObject data, String item) {
        if (data == null || !data.has(item)) {
            return 0;
        }
        return data.getJSONArray(item).length();
    }

}
