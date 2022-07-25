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

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import tr.com.argela.nfv.onap.service.constant.EntityStatus;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@Getter
@Setter
public class VF {

    String uuid;
    String uniqueId;
    String invariantUUID;
    String name, description, versionName, modelName;
    VSP vsp;
    @JsonIgnore
    Service service;
    EntityStatus versionStatus;

    Map<String, VFModel> vfModelMapByType = new LinkedHashMap<>();

    public VFModel getVfModelType(String type) {
        VFModel vfModel = getVfModelMapByType().get(type);
        if (vfModel != null) {
            return vfModel;
        }
        vfModel = new VFModel();
        vfModel.setModelType(type);
        getVfModelMapByType().put(type, vfModel);
        return vfModel;
    }

    @Override
    public String toString() {
        return "VF{" + "uuid=" + uuid + ", uniqueId=" + uniqueId + ", invariantUUID=" + invariantUUID + ", name=" + name
                + ", versionName=" + versionName + '}';
    }

}
