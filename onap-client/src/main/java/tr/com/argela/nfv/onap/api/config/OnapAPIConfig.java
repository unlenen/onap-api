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
package tr.com.argela.nfv.onap.api.config;

import java.security.GeneralSecurityException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */

@Configuration
@Getter
public class OnapAPIConfig {

    @Value("${onap.ip}")
    String onapIPAddress;

    @Bean
    public X509TrustManager getX509TrustManager() {
        return new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[0];
            }

            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }
        };

    }

    @Bean
    public SSLSocketFactory getSSLSocketFactory() throws GeneralSecurityException {
        TrustManager[] trustAllCerts = new TrustManager[] {
                getX509TrustManager()
        };

        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        SSLSocketFactory socketFactory = sc.getSocketFactory();
        HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);
        return socketFactory;
    }

}
