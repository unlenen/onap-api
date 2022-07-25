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
package  tr.com.argela.nfv.onap.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import  tr.com.argela.nfv.onap.service.model.serverInstance.Server;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@Getter
@Setter
public class VFModule {

    String id;
    String name;
    String type;
    String reqId;
    String reqUrl;
    String availabilityZone;
    Server server;

    VFModel vfModel;

    VFModuleProfile profile;

    @JsonIgnore
    VNF vnf;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("VFModule{id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", availabilityZone=").append(availabilityZone);
        sb.append('}');
        return sb.toString();
    }

}
