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
package tr.com.argela.nfv.onap;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.event.EventListener;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import lombok.extern.slf4j.Slf4j;
import tr.com.argela.nfv.onap.api.config.OnapAPIConfig;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */

@SpringBootApplication
@Slf4j
@EnableWebMvc
public class Application extends SpringBootServletInitializer {

    @Autowired
    OnapAPIConfig config;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onAppRun() {
        log.warn("System starts at " + new Date());
        log.warn("ONAP IP Address : " + config.getOnapIPAddress());
    }
}
