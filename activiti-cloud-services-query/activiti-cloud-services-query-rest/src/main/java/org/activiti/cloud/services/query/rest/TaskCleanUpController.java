package org.activiti.cloud.services.query.rest;

import com.querydsl.core.types.Predicate;
import org.activiti.api.task.model.Task;
import org.activiti.cloud.alfresco.data.domain.AlfrescoPagedResourcesAssembler;
import org.activiti.cloud.api.task.model.CloudTask;
import org.activiti.cloud.services.query.app.repository.EntityFinder;
import org.activiti.cloud.services.query.app.repository.TaskRepository;
import org.activiti.cloud.services.query.model.ProcessInstanceEntity;
import org.activiti.cloud.services.query.model.TaskEntity;
import org.activiti.cloud.services.query.resources.ProcessInstanceResource;
import org.activiti.cloud.services.query.resources.TaskResource;
import org.activiti.cloud.services.query.rest.assembler.TaskResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@ConditionalOnProperty(name = "activiti.enable-clean-up", havingValue = "true")
@RestController
@RequestMapping(
        value = "/admin/v1/tasks",
        produces = {
                MediaTypes.HAL_JSON_VALUE,
                MediaType.APPLICATION_JSON_VALUE
        })
public class TaskCleanUpController {

    private final TaskRepository taskRepository;

    private TaskResourceAssembler taskResourceAssembler;

    private AlfrescoPagedResourcesAssembler<TaskEntity> pagedResourcesAssembler;

    @Autowired
    public TaskCleanUpController(TaskRepository taskRepository,
                               TaskResourceAssembler taskResourceAssembler,
                               AlfrescoPagedResourcesAssembler<TaskEntity> pagedResourcesAssembler) {
        this.taskRepository = taskRepository;
        this.taskResourceAssembler = taskResourceAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/export")
    public PagedResources<TaskResource> exportTasks (@QuerydslPredicate(root = TaskEntity.class) Predicate predicate,
                                             Pageable pageable,
                                             boolean delete) {

        PagedResources<TaskResource> result = pagedResourcesAssembler.toResource(pageable,
                taskRepository.findAll(predicate,
                        pageable),
                taskResourceAssembler);

        if(delete)
            taskRepository.deleteAll();

        return result;
    }

}
