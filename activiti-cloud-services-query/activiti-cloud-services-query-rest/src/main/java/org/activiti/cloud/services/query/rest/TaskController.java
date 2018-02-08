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

import java.util.Optional;

import com.querydsl.core.types.Predicate;
import org.activiti.cloud.services.query.app.repository.TaskRepository;
import org.activiti.cloud.services.query.model.Task;
import org.activiti.cloud.services.query.resources.TaskResource;
import org.activiti.cloud.services.query.rest.assembler.TaskResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/" + TaskRelProvider.COLLECTION_RESOURCE_REL)
public class TaskController {

    private final TaskRepository taskRepository;

    private TaskResourceAssembler taskResourceAssembler;

    private PagedResourcesAssembler<Task> pagedResourcesAssembler;

    @Autowired
    public TaskController(TaskRepository taskRepository,
                          TaskResourceAssembler taskResourceAssembler,
                          PagedResourcesAssembler<Task> pagedResourcesAssembler) {
        this.taskRepository = taskRepository;
        this.taskResourceAssembler = taskResourceAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @RequestMapping(method = RequestMethod.GET)
    public PagedResources<TaskResource> findAll(@QuerydslPredicate(root = Task.class) Predicate predicate,
                                                Pageable pageable) {
        return pagedResourcesAssembler.toResource(taskRepository.findAll(predicate,
                                                                           pageable),
                                                  taskResourceAssembler);
    }

    @RequestMapping(value = "/{taskId}", method = RequestMethod.GET)
    public TaskResource findById(@PathVariable String taskId) {
        Optional<Task> findResult = taskRepository.findById(taskId);
        if (!findResult.isPresent()) {
            throw new RuntimeException("Unable to find task for the given id:'" + taskId + "'");
        }
        return taskResourceAssembler.toResource(findResult.get());
    }

}
