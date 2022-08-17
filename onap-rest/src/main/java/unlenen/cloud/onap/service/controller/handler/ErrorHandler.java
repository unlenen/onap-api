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
package unlenen.cloud.onap.service.controller.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import net.minidev.json.JSONObject;
import unlenen.cloud.onap.api.exception.OnapException;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<String> handleIOException(IOException exception) {
        JSONObject error = new JSONObject();
        error.put("code", exception.getClass().getSimpleName());
        error.put("message", exception.getMessage());
        return new ResponseEntity<String>(error.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = OnapException.class)
    public ResponseEntity<String> handleOnapException(OnapException exception) {
        JSONObject error = new JSONObject();
        error.put("code", exception.getClass().getSimpleName());
        error.put("message", exception.getMessage());
        return new ResponseEntity<String>(error.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
