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

package org.activiti.cloud.services.query.events.handlers;

import java.util.Optional;

import org.activiti.api.task.model.Task.TaskStatus;
import org.activiti.api.task.model.events.TaskCandidateGroupEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.task.model.events.CloudTaskCandidateGroupRemovedEvent;
import org.activiti.cloud.services.query.app.repository.TaskCandidateGroupRepository;
import org.activiti.cloud.services.query.app.repository.TaskRepository;
import org.activiti.cloud.services.query.model.QueryException;
import org.activiti.cloud.services.query.model.TaskCandidateGroup;
import org.activiti.cloud.services.query.model.TaskEntity;

public class TaskCandidateGroupRemovedEventHandler implements QueryEventHandler {

    private final TaskRepository taskRepository;
    private final TaskCandidateGroupRepository taskCandidateGroupRepository;

    public TaskCandidateGroupRemovedEventHandler(TaskRepository taskRepository,
                                                 TaskCandidateGroupRepository taskCandidateGroupRepository) {
        this.taskRepository = taskRepository;
        this.taskCandidateGroupRepository = taskCandidateGroupRepository;
    }

    @Override
    public void handle(CloudRuntimeEvent<?, ?> event) {

        CloudTaskCandidateGroupRemovedEvent taskCandidateGroupRemovedEvent = (CloudTaskCandidateGroupRemovedEvent) event;
        String taskId = taskCandidateGroupRemovedEvent.getEntity().getTaskId();
        TaskCandidateGroup taskCandidateGroup = new TaskCandidateGroup(taskId,
                                                                       taskCandidateGroupRemovedEvent.getEntity().getGroupId());

        // if a task was cancelled / completed do not handle this event
        Optional<TaskEntity> findResult = taskRepository.findById(taskId);
        if (findResult.isPresent()) {
            TaskStatus taskStatus = findResult.get().getStatus();
            if (taskStatus != TaskStatus.CREATED &&
                taskStatus != TaskStatus.ASSIGNED &&
                taskStatus != TaskStatus.SUSPENDED
                ) {
                return;
            }
        }

        // Persist into database
        try {
            taskCandidateGroupRepository.delete(taskCandidateGroup);
        } catch (Exception cause) {
            throw new QueryException("Error handling TaskCandidateGroupRemovedEvent[" + event + "]",
                                     cause);
        }
    }

    @Override
    public String getHandledEvent() {
        return TaskCandidateGroupEvent.TaskCandidateGroupEvents.TASK_CANDIDATE_GROUP_REMOVED.name();
    }
}
