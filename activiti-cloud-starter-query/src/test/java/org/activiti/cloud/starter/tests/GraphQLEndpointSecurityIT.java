/*
 * Copyright 2017 Alfresco, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.cloud.starter.tests;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class GraphQLEndpointSecurityIT {

    private static final String GRAPHQL_URL = "/graphql";

    @Autowired
    private KeycloakTokenProducer keycloakTokenProducer;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void shouldNotSeeGraphQLEndpoint() throws Exception {
        await().atMost(15, TimeUnit.SECONDS).untilAsserted(() -> {

            //when
            ResponseEntity<String> responseEntity = testRestTemplate.exchange(GRAPHQL_URL,
                    HttpMethod.GET,
                    getHeaderEntity(),String.class);

            //then
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        });
    }

    private HttpEntity getHeaderEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", keycloakTokenProducer.getTokenString());
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        return entity;
    }
}
