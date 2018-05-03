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

package org.activiti.cloud.services.query.events;

import org.activiti.cloud.services.query.model.Task;

public class TaskCompletedEvent extends AbstractProcessEngineEvent {

    private Task task;

    public TaskCompletedEvent() {
    }

    public TaskCompletedEvent(Long timestamp,
                              String eventType,
                              String executionId,
                              String processDefinitionId,
                              String serviceName,
                              String serviceFullName,
                              String serviceType,
                              String serviceVersion,
                              String appName,
                              String appVersion,
                              String processInstanceId,
                              Task task) {
        super(timestamp,
              eventType,
              executionId,
              processDefinitionId,
              processInstanceId,
                serviceName,
                serviceFullName,
                serviceType,
                serviceVersion,
                appName,
                appVersion);
        this.task = task;
    }

    public Task getTask() {
        return task;
    }
}