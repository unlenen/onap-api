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
package unlenen.cloud.onap.api.client.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import unlenen.cloud.onap.api.client.model.OnapRequest;
import unlenen.cloud.onap.api.exception.OnapRequestFailedException;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@Component
@Slf4j
public class URLConnectionService {

    @Autowired
    SSLSocketFactory socketFactory;
    @Autowired
    X509TrustManager x509TrustManager;

    public ResponseEntity get(String url, Map<String, String> headerMap) {
        HttpHeaders headers = new HttpHeaders();

        for (String header : headerMap.keySet()) {
            headers.add(header, headerMap.get(header));
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        return response;
    }

    public ResponseEntity delete(String url, Map<String, String> headerMap) {
        HttpHeaders headers = new HttpHeaders();

        for (String header : headerMap.keySet()) {
            headers.add(header, headerMap.get(header));
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String.class);
        return response;
    }

    public ResponseEntity push(String methodType, String url, Map<String, String> headerMap, String data, String type) {

        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;
        HttpEntity<String> requestEntity;
        for (String header : headerMap.keySet()) {
            headers.add(header, headerMap.get(header));
        }
        if (type != null && data != null) {
            headers.setContentType(org.springframework.http.MediaType.parseMediaType(type));
            requestEntity = new HttpEntity<>(data, headers);
        } else {
            requestEntity = new HttpEntity<>(headers);
        }
        response = restTemplate.exchange(url, HttpMethod.valueOf(methodType), requestEntity, String.class);
        return response;
    }

    public ResponseEntity postFile(OnapRequest onapRequest, String url, Map<String, Object> fileMapByParamName) {
        HttpHeaders headers = new HttpHeaders();

        for (String header : onapRequest.getOnapModule().getHeaders().keySet()) {
            headers.add(header, onapRequest.getOnapModule().getHeaders().get(header));
        }
        headers.setContentType(org.springframework.http.MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        for (String paramName : fileMapByParamName.keySet()) {
            Object val = fileMapByParamName.get(paramName);
            if (val instanceof File) {
                body.add(paramName, new FileSystemResource((File) (val)));
            } else if (val instanceof String) {
                body.add(paramName, (String) val);
            }
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        return response;
    }

    public Object call(OnapRequest onapRequest, String url, Map<String, String> parameters, Map<String, Object> files,
            String payloadFilePath) throws IOException {

        String data = "";
        if (payloadFilePath != null) {
            switch (onapRequest.getCallType()) {
                case DELETE:
                case PUT:
                case POST: {
                    data = enrichPayloadData(readResourceFileToString(payloadFilePath), parameters);
                }
            }
        }
        if (log.isInfoEnabled())
            log.info("[ONAP][APICALL][" + onapRequest.getCallType() + "][Request] url : " + url + " , payload: "
                    + data);
        ResponseEntity<String> response = null;
        switch (onapRequest.getCallType()) {
            default:
            case GET: {
                response = get(url, onapRequest.getOnapModule().getHeaders());
                break;
            }
            case DELETE: {
                if (data == null) {
                    response = delete(url, onapRequest.getOnapModule().getHeaders());
                } else {
                    response = push(onapRequest.getCallType().name(), url, onapRequest.getOnapModule().getHeaders(),
                            data, onapRequest.getPayloadFileType());
                }
                break;
            }
            case PUT:
            case POST: {
                response = push(onapRequest.getCallType().name(), url, onapRequest.getOnapModule().getHeaders(), data,
                        onapRequest.getPayloadFileType());
                break;
            }
            case POST_FILE: {
                response = postFile(onapRequest, url, files);
            }
        }

        int responseCode = response.getStatusCodeValue();

        if (responseCode != onapRequest.getValidReturnCode()) {
            log.error("[ONAP][APICALL][" + onapRequest.getCallType() + "][Response][" + responseCode + "] url : "
                    + url);

            throw new OnapRequestFailedException(onapRequest, url, responseCode, response.getBody());
        }
        String responseBody = response.getBody();

        if (log.isInfoEnabled())
            log.info("[ONAP][APICALL][" + onapRequest.getCallType() + "][Response][" + responseCode + "] url : " + url
                    + " , response: "
                    + responseBody);

        switch (onapRequest.getResponseType()) {
            case JSONObject: {
                return new JSONObject(responseBody);
            }
            case JSONArray: {
                return new JSONArray(responseBody);
            }
            case STRING: {
                return responseBody;
            }
        }
        return null;
    }

    private String enrichPayloadData(String payloadData, Map<String, String> parameters) {
        for (String key : parameters.keySet()) {
            payloadData = payloadData.replaceAll("\\$\\{" + key + "\\}", parameters.get(key));
        }
        return payloadData;
    }

    private String readResourceFileToString(String path) throws IOException {
        if (path == null) {
            return null;
        }
        return copyStreamToString(getResourceStream(path));
    }

    private InputStream getResourceStream(String payloadFilePath) {
        InputStream is = URLConnectionService.class.getClassLoader().getResourceAsStream(payloadFilePath);
        return is;
    }

    private String copyStreamToString(InputStream stream) throws IOException {
        StringBuilder str = new StringBuilder();
        while (true) {
            byte[] data = new byte[4096];

            int count = stream.read(data);
            if (count < 0) {
                break;
            }
            str.append(new String(data, 0, count));
        }
        return str.toString();
    }

}
