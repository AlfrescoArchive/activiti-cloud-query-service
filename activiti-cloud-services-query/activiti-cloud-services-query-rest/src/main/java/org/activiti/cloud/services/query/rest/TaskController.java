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

import com.querydsl.core.types.Predicate;
import org.activiti.cloud.services.query.app.repository.EntityFinder;
import org.activiti.cloud.services.query.app.repository.TaskRepository;;
import org.activiti.cloud.services.query.model.Task;
import org.activiti.cloud.services.query.resources.TaskResource;
import org.activiti.cloud.services.query.rest.assembler.TaskResourceAssembler;
import org.activiti.cloud.services.security.ActivitiForbiddenException;
import org.activiti.cloud.services.security.AuthenticationWrapper;
import org.activiti.cloud.services.security.TaskLookupRestrictionService;
import org.activiti.engine.UserRoleLookupProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/" + TaskRelProvider.COLLECTION_RESOURCE_REL, produces = MediaTypes.HAL_JSON_VALUE)
public class TaskController {

    private final TaskRepository taskRepository;

    private TaskResourceAssembler taskResourceAssembler;

    private PagedResourcesAssembler<Task> pagedResourcesAssembler;

    private EntityFinder entityFinder;

    private TaskLookupRestrictionService taskLookupRestrictionService;

    private UserRoleLookupProxy userRoleLookupProxy;

    private AuthenticationWrapper authenticationWrapper;

    @Autowired
    public TaskController(TaskRepository taskRepository,
                          TaskResourceAssembler taskResourceAssembler,
                          PagedResourcesAssembler<Task> pagedResourcesAssembler,
                          EntityFinder entityFinder,
                          TaskLookupRestrictionService taskLookupRestrictionService,
                          UserRoleLookupProxy userRoleLookupProxy,
                          AuthenticationWrapper authenticationWrapper) {
        this.taskRepository = taskRepository;
        this.taskResourceAssembler = taskResourceAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.entityFinder = entityFinder;
        this.taskLookupRestrictionService = taskLookupRestrictionService;
        this.userRoleLookupProxy = userRoleLookupProxy;
        this.authenticationWrapper = authenticationWrapper;
    }

    @ExceptionHandler(ActivitiForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAppException(ActivitiForbiddenException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleAppException(IllegalStateException ex) {
        return ex.getMessage();
    }

    @RequestMapping(method = RequestMethod.GET)
    public PagedResources<TaskResource> findAll(@QuerydslPredicate(root = Task.class) Predicate predicate,
                                                Pageable pageable) {

        predicate = taskLookupRestrictionService.restrictTaskQuery(predicate);
        Page<Task> page = taskRepository.findAll(predicate,
                pageable);

        return pagedResourcesAssembler.toResource(page,
                                                  taskResourceAssembler);
    }

    @RequestMapping(value="/admin/",method = RequestMethod.GET)
    public PagedResources<TaskResource> adminFindAll(@QuerydslPredicate(root = Task.class) Predicate predicate,
                                                Pageable pageable) {

        if (authenticationWrapper.getAuthenticatedUserId() != null){
            if (!userRoleLookupProxy.isAdmin(authenticationWrapper.getAuthenticatedUserId())){
                throw new ActivitiForbiddenException("Access forbidden");
            }
        }

        Page<Task> page = taskRepository.findAll(predicate,
                pageable);

        return pagedResourcesAssembler.toResource(page,
                taskResourceAssembler);
    }

    @RequestMapping(value = "/{taskId}", method = RequestMethod.GET)
    public TaskResource findById(@PathVariable String taskId) {
        return taskResourceAssembler.toResource(entityFinder.findById(taskRepository,
                                                                      taskId,
                                                                      "Unable to find task for the given id:'" + taskId + "'"));
    }
}
