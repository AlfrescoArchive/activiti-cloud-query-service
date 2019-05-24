/*
 * Copyright 2018 Alfresco, Inc. and/or its affiliates.
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

package org.activiti.cloud.services.query.rest;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.activiti.alfresco.rest.docs.AlfrescoDocumentation.pageRequestParameters;
import static org.activiti.alfresco.rest.docs.AlfrescoDocumentation.pagedResourcesResponseFields;
import static org.activiti.alfresco.rest.docs.AlfrescoDocumentation.taskFields;
import static org.activiti.alfresco.rest.docs.AlfrescoDocumentation.taskIdParameter;
import static org.activiti.alfresco.rest.docs.HALDocumentation.pageLinks;
import static org.activiti.alfresco.rest.docs.HALDocumentation.pagedTasksFields;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import com.querydsl.core.types.Predicate;
import org.activiti.api.runtime.conf.impl.CommonModelAutoConfiguration;
import org.activiti.api.runtime.shared.security.SecurityManager;
import org.activiti.api.task.model.Task;
import org.activiti.cloud.alfresco.argument.resolver.AlfrescoPageRequest;
import org.activiti.cloud.conf.QueryRestAutoConfiguration;
import org.activiti.cloud.services.query.app.repository.EntityFinder;
import org.activiti.cloud.services.query.app.repository.ProcessDefinitionRepository;
import org.activiti.cloud.services.query.app.repository.TaskRepository;
import org.activiti.cloud.services.query.model.TaskEntity;
import org.activiti.cloud.services.security.TaskLookupRestrictionService;
import org.activiti.core.common.spring.security.policies.SecurityPoliciesManager;
import org.activiti.core.common.spring.security.policies.conf.SecurityPoliciesProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskAdminController.class)
@Import({
        QueryRestAutoConfiguration.class,
        CommonModelAutoConfiguration.class,
})
@EnableSpringDataWebSupport
@AutoConfigureMockMvc(secure = false)
@AutoConfigureRestDocs(outputDir = "target/snippets")
@ComponentScan(basePackages = {"org.activiti.cloud.services.query.rest.assembler", "org.activiti.cloud.alfresco"})
public class TaskEntityAdminControllerIT {

    private static final String TASK_ADMIN_ALFRESCO_IDENTIFIER = "task-admin-alfresco";
    private static final String TASK_ADMIN_IDENTIFIER = "task-admin";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private EntityFinder entityFinder;

    @MockBean
    private SecurityManager securityManager;

    @MockBean
    private SecurityPoliciesManager securityPoliciesManager;

    @MockBean
    private ProcessDefinitionRepository processDefinitionRepository;

    @MockBean
    private SecurityPoliciesProperties securityPoliciesProperties;

    @MockBean
    private TaskLookupRestrictionService taskLookupRestrictionService;

    @Before
    public void setUp() {
        assertThat(securityManager).isNotNull();
        assertThat(securityPoliciesManager).isNotNull();
        assertThat(processDefinitionRepository).isNotNull();
        assertThat(securityPoliciesProperties).isNotNull();
        assertThat(taskLookupRestrictionService).isNotNull();
    }

    @Test
    public void allTasksShouldReturnAllResultsUsingAlfrescoMetadataWhenMediaTypeIsApplicationJson() throws Exception {
        //given
        AlfrescoPageRequest pageRequest = new AlfrescoPageRequest(11,
                                                                  10,
                                                                  PageRequest.of(0,
                                                                                 20));

        given(taskRepository.findAll(any(),
                                     eq(pageRequest)))
                .willReturn(new PageImpl<>(Collections.singletonList(buildDefaultTask()),
                                           pageRequest,
                                           12));

        //when
        MvcResult result = mockMvc.perform(get("/admin/v1/tasks?skipCount=11&maxItems=10")
                                                   .accept(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andDo(document(TASK_ADMIN_ALFRESCO_IDENTIFIER + "/list",
                                pageRequestParameters(),
                                pagedResourcesResponseFields()

                ))
                .andReturn();

        assertThatJson(result.getResponse().getContentAsString())
                .node("list.pagination.skipCount").isEqualTo(11)
                .node("list.pagination.maxItems").isEqualTo(10)
                .node("list.pagination.count").isEqualTo(1)
                .node("list.pagination.hasMoreItems").isEqualTo(false)
                .node("list.pagination.totalItems").isEqualTo(12);
    }

    @Test
    public void allTasksShouldReturnAllResultsUsingHalWhenMediaTypeIsApplicationHalJson() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(1,
                                                 10);

        given(taskRepository.findAll(any(),
                                     eq(pageRequest)))
                .willReturn(new PageImpl<>(Collections.singletonList(buildDefaultTask()),
                                           pageRequest,
                                           11));

        //when
        mockMvc.perform(get("/admin/v1/tasks?page=1&size=10")
                                                   .accept(MediaTypes.HAL_JSON_VALUE))
                //then
                .andExpect(status().isOk())
                .andDo(document(TASK_ADMIN_IDENTIFIER + "/list",
                                pageLinks(),
                                pagedTasksFields()

                ));

    }

    private TaskEntity buildDefaultTask() {
        return new TaskEntity(UUID.randomUUID().toString(),
                              "john",
                              "Review",
                              "Review the report",
                              new Date(),
                              new Date(),
                              20,
                              UUID.randomUUID().toString(),
                              UUID.randomUUID().toString(),
                              "My app",
                              "My app",
                              "1",
                              null,
                              null,
                              Task.TaskStatus.ASSIGNED,
                              new Date(),
                              new Date(),
                              "peter",
                              null,
                              "aFormKey",
                              10,
                              "businessKey"
        );
    }

    @Test
    public void findByIdShouldUseAlfrescoMetadataWhenMediaTypeIsApplicationJson() throws Exception {
        //given
        TaskEntity taskEntity = buildDefaultTask();
        given(entityFinder.findById(eq(taskRepository),
                                    eq(taskEntity.getId()),
                                    anyString()))
                .willReturn(taskEntity);

        Predicate restrictionPredicate = mock(Predicate.class);
        given(taskRepository.findAll(restrictionPredicate)).willReturn(Collections.singletonList(taskEntity));

        //when
        this.mockMvc.perform(get("/admin/v1/tasks/{taskId}",
                                 taskEntity.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
                //then
                .andExpect(status().isOk())
                .andDo(document(TASK_ADMIN_ALFRESCO_IDENTIFIER + "/get",
                                taskIdParameter(),
                                taskFields()
                       )
                );
    }
}