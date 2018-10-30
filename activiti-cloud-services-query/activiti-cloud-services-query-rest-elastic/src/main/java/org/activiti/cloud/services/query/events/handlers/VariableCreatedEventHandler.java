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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.activiti.api.model.shared.event.VariableEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.model.shared.events.CloudVariableCreatedEvent;
import org.activiti.cloud.services.query.app.repository.elastic.ProcessInstanceRepository;
import org.activiti.cloud.services.query.app.repository.elastic.TaskRepository;
import org.activiti.cloud.services.query.model.elastic.ProcessInstance;
import org.activiti.cloud.services.query.model.elastic.QueryException;
import org.activiti.cloud.services.query.model.elastic.Variable;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class VariableCreatedEventHandler implements QueryEventHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(VariableCreatedEventHandler.class);

	private final ProcessInstanceRepository processInstanceRepository;
	private final TaskRepository taskRepository;
	private ElasticsearchTemplate esTemplate;
	private Client esClient;
	private ObjectMapper objectMapper;

	@Autowired
	public VariableCreatedEventHandler(ProcessInstanceRepository processInstanceRepository,
			TaskRepository taskRepository, ElasticsearchTemplate esTemplate, Client esClient,
			ObjectMapper objectMapper) {
		this.processInstanceRepository = processInstanceRepository;
		this.taskRepository = taskRepository;
		this.esTemplate = esTemplate;
		this.esClient = esClient;
		this.objectMapper = objectMapper;
	}

	@Override
	public void handle(CloudRuntimeEvent<?, ?> event) {
		CloudVariableCreatedEvent variableCreatedEvent = (CloudVariableCreatedEvent) event;
		LOGGER.debug("Handling variableEntity created event: " + variableCreatedEvent.getEntity().getName());
		Variable variableEntity = new Variable(
				null,
				variableCreatedEvent.getEntity().getType(),
				variableCreatedEvent.getEntity().getName(), 
				variableCreatedEvent.getEntity().getProcessInstanceId(),
				variableCreatedEvent.getServiceName(),
				variableCreatedEvent.getServiceFullName(),
				variableCreatedEvent.getServiceVersion(),
				variableCreatedEvent.getAppName(),
				variableCreatedEvent.getAppVersion(),
				variableCreatedEvent.getEntity().getTaskId(),
				new Date(variableCreatedEvent.getTimestamp()),
				new Date(variableCreatedEvent.getTimestamp()),
				null);
		variableEntity.setValue(variableCreatedEvent.getEntity().getValue());

		setProcessInstance(variableCreatedEvent, variableEntity);

		setTask(variableCreatedEvent, variableEntity);

		persist(event, variableEntity);
	}

	private void persist(CloudRuntimeEvent<?, ?> event, Variable variable) {
		try {
			if (variable.isTaskVariable()) {
				// TODO implement please
				return;
			}

			ProcessInstance processInstance = variable.getProcessInstance();
			String variableType = variable.getType();
			if (processInstance.getVariables() == null) {
				processInstance.setVariables(new HashMap<>());
			}
			if (processInstance.getVariables().get(variableType) == null) {
				processInstance.getVariables().put(variableType, new HashSet<>());
			}

			Set<Variable> currentVariables = processInstance.getVariables().get(variableType);
			currentVariables.remove(variable);
			currentVariables.add(variable);

			UpdateRequest updateRequest = new UpdateRequest();
			updateRequest.index("process_instance");
			updateRequest.type("_doc");
			updateRequest.id(processInstance.getId());

			ObjectNode jo = objectMapper.createObjectNode();
			jo.set("variables", objectMapper.valueToTree(processInstance.getVariables()));

			System.out.println("TO UPDATE: " + jo.toString());
			updateRequest.doc(objectMapper.writeValueAsString(jo), XContentType.JSON);

			esClient.update(updateRequest).get();

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
