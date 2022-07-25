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
package  tr.com.argela.nfv.onap.service.model.serverInstance.k8s;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import  tr.com.argela.nfv.onap.service.model.serverInstance.Server;
import  tr.com.argela.nfv.onap.service.model.serverInstance.ServerType;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */

@Getter
@Setter
public class HelmInstance extends Server {

    public HelmInstance() {
        super(ServerType.POD);
    }

    ResourceBundle resourceBundle;
    String namespace, profileName, releaseName;
    Map<String, List<String>> k8sObjects = new HashMap<>();

}
