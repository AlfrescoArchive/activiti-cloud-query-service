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
import org.activiti.cloud.services.query.app.repository.TaskRepository;
import org.activiti.cloud.services.query.app.repository.VariableRepository;
import org.activiti.cloud.services.query.model.Task;
import org.activiti.cloud.services.query.model.Variable;
import org.activiti.cloud.services.query.resources.TaskResource;
import org.activiti.cloud.services.query.resources.VariableResource;
import org.activiti.cloud.services.query.rest.assembler.TaskResourceAssembler;
import org.activiti.cloud.services.query.rest.assembler.VariableResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

;

@RestController
@RequestMapping(value = "/admin/v1", produces = MediaTypes.HAL_JSON_VALUE)
public class AdminController {

    private final TaskRepository taskRepository;

    private TaskResourceAssembler taskResourceAssembler;

    private PagedResourcesAssembler<Task> pagedResourcesAssembler;

    private PagedResourcesAssembler<Variable> pagedVariablesResourcesAssembler;

    private VariableRepository variableRepository;

    private VariableResourceAssembler variableResourceAssembler;


    @Autowired
    public AdminController(TaskRepository taskRepository,
                           TaskResourceAssembler taskResourceAssembler,
                           PagedResourcesAssembler<Task> pagedResourcesAssembler,
                            VariableRepository variableRepository,
                           VariableResourceAssembler variableResourceAssembler,
                            PagedResourcesAssembler<Variable> pagedVariablesResourcesAssembler) {
        this.taskRepository = taskRepository;
        this.taskResourceAssembler = taskResourceAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.variableRepository = variableRepository;
        this.variableResourceAssembler = variableResourceAssembler;
        this.pagedVariablesResourcesAssembler = pagedVariablesResourcesAssembler;
    }
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleAppException(IllegalStateException ex) {
        return ex.getMessage();
    }

    @RequestMapping(value="/tasks",method = RequestMethod.GET)
    public PagedResources<TaskResource> allTasks(@QuerydslPredicate(root = Task.class) Predicate predicate,
                                                Pageable pageable) {

        Page<Task> page = taskRepository.findAll(predicate,
                pageable);

        return pagedResourcesAssembler.toResource(page,
                taskResourceAssembler);
    }

    @RequestMapping(value="/variables",method = RequestMethod.GET)
    public PagedResources<VariableResource> findAll(@QuerydslPredicate(root = Variable.class) Predicate predicate,
                                                    Pageable pageable) {

        return pagedVariablesResourcesAssembler.toResource(variableRepository.findAll(predicate,
                pageable),
                variableResourceAssembler);
    }
}
