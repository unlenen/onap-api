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
package unlenen.cloud.onap.api.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
public class OnapUtil {

    public static File writeStringToTmpFile(String content, String tmpPrefix, String tmpSuffix)
            throws FileNotFoundException, IOException {
        File tmpFile = File.createTempFile(tmpPrefix, tmpSuffix);
        PrintStream ps = null;
        try {
            ps = new PrintStream(tmpFile);
            ps.print(content);
            ps.flush();
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return tmpFile;
    }
}
