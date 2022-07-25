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
package tr.com.argela.nfv.onap.service.scenario;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
public class CommonScenario {

    Logger log = LoggerFactory.getLogger(CommonScenario.class);

    protected void validateResponse(String json) throws Exception {
        JSONObject obj = new JSONObject(json);
        if (obj.has("error")) {
            throw new Exception("Unvalid response , msg :" + json);
        }
    }

    protected void validateResponseArray(String json) throws Exception {
        JSONArray obj = new JSONArray(json);
        if (!obj.isEmpty() && obj.getJSONObject(0).has("msg-type")) {
            throw new Exception("Unvalid response , msg :" + json);
        }
    }

    protected String readResponse(ResponseEntity responseEntity) throws Exception {
        return readResponseValidateOption(responseEntity, true);
    }

    protected String readResponseValidateOption(ResponseEntity responseEntity, boolean validate) throws Exception {
        String data = responseEntity.getBody() + "";
        if (validate) {
            validateResponse(data);
        }
        return data;
    }

    protected String readResponse(ResponseEntity responseEntity, boolean isArray) throws Exception {
        String data = responseEntity.getBody() + "";
        if (isArray) {
            validateResponseArray(data);
        } else {
            validateResponse(data);
        }
        return data;
    }

}
