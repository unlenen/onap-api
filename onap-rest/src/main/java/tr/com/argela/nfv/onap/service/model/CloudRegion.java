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

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import tr.com.argela.nfv.onap.service.constant.CloudType;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */

@Getter
@Setter
public class CloudRegion {

    String name;
    String cloudOwner;
    String complexName;
    String regionName;

    CloudType cloudType;
    String domain, defaultProject, authServiceURL, authUser, authPassword, configParameters;
    List<Tenant> tenants;
    List<AvailabilityZone> availabilityZones;

    @Override
    public String toString() {
        return "CloudRegion{" + "cloudOwner=" + cloudOwner + ", name=" + name + ", cloudType=" + cloudType + '}';
    }

}
