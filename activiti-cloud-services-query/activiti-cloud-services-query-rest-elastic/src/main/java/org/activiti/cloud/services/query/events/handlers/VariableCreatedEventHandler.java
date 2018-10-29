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

import java.util.Date;

import org.activiti.api.model.shared.event.VariableEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.model.shared.events.CloudVariableCreatedEvent;
import org.activiti.cloud.services.query.app.repository.elastic.ProcessInstanceRepository;
import org.activiti.cloud.services.query.app.repository.elastic.TaskRepository;
import org.activiti.cloud.services.query.app.repository.elastic.VariableRepository;
import org.activiti.cloud.services.query.model.elastic.QueryException;
import org.activiti.cloud.services.query.model.elastic.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VariableCreatedEventHandler implements QueryEventHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(VariableCreatedEventHandler.class);

	private final VariableRepository variableRepository;
	private final ProcessInstanceRepository processInstanceRepository;
	private final TaskRepository taskRepository;

	@Autowired
	public VariableCreatedEventHandler(VariableRepository variableRepository,
			ProcessInstanceRepository processInstanceRepository, TaskRepository taskRepository) {
		this.variableRepository = variableRepository;
		this.processInstanceRepository = processInstanceRepository;
		this.taskRepository = taskRepository;
	}

	@Override
	public void handle(CloudRuntimeEvent<?, ?> event) {
		CloudVariableCreatedEvent variableCreatedEvent = (CloudVariableCreatedEvent) event;
		LOGGER.debug("Handling variableEntity created event: " + variableCreatedEvent.getEntity().getName());
		Variable variableEntity = new Variable(null, variableCreatedEvent.getEntity().getType(),
				variableCreatedEvent.getEntity().getName(), variableCreatedEvent.getEntity().getProcessInstanceId(),
				variableCreatedEvent.getServiceName(), variableCreatedEvent.getServiceFullName(),
				variableCreatedEvent.getServiceVersion(), variableCreatedEvent.getAppName(),
				variableCreatedEvent.getAppVersion(), variableCreatedEvent.getEntity().getTaskId(),
				new Date(variableCreatedEvent.getTimestamp()), new Date(variableCreatedEvent.getTimestamp()), null);
		variableEntity.setValue(variableCreatedEvent.getEntity().getValue());

		setProcessInstance(variableCreatedEvent, variableEntity);

		setTask(variableCreatedEvent, variableEntity);

		persist(event, variableEntity);
	}

	private void persist(CloudRuntimeEvent<?, ?> event, Variable variableEntity) {
		try {
			variableRepository.save(variableEntity);
		} catch (Exception cause) {
			throw new QueryException("Error handling VariableCreatedEvent[" + event + "]", cause);
		}
	}

	private void setTask(CloudVariableCreatedEvent variableCreatedEvent, Variable variableEntity) {
		if (variableCreatedEvent.getEntity().isTaskVariable()) {
			taskRepository.findById(variableCreatedEvent.getEntity().getTaskId())
					.ifPresent(task -> variableEntity.setTask(task));
		}
	}

	private void setProcessInstance(CloudVariableCreatedEvent variableCreatedEvent, Variable variableEntity) {
		processInstanceRepository.findById(variableCreatedEvent.getEntity().getProcessInstanceId())
				.ifPresent(processInstance -> variableEntity.setProcessInstance(processInstance));

	}

	@Override
	public String getHandledEvent() {
		return VariableEvent.VariableEvents.VARIABLE_CREATED.name();
	}
}
