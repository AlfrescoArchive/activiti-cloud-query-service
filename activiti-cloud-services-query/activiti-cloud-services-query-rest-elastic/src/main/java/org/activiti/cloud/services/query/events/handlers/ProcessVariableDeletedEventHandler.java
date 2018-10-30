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

import org.activiti.cloud.api.model.shared.events.CloudVariableDeletedEvent;
import org.activiti.cloud.services.query.app.repository.elastic.EntityFinder;
import org.activiti.cloud.services.query.app.repository.elastic.ProcessInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessVariableDeletedEventHandler {

	private final ProcessInstanceRepository processInstanceRepository;

	private final EntityFinder entityFinder;

	@Autowired
	public ProcessVariableDeletedEventHandler(ProcessInstanceRepository processInstanceRepository,
			EntityFinder entityFinder) {
		this.processInstanceRepository = processInstanceRepository;
		this.entityFinder = entityFinder;
	}

	public void handle(CloudVariableDeletedEvent event) {
		String variableName = event.getEntity().getName();
		String processInstanceId = event.getEntity().getProcessInstanceId();
		// TODO complete this method
//        BooleanExpression predicate = QVariableEntity.variableEntity.processInstanceId.eq(processInstanceId)
//                .and(
//                        QVariableEntity.variableEntity.name.eq(variableName)
//
//                ).and(QVariableEntity.variableEntity.markedAsDeleted.eq(Boolean.FALSE));
//        VariableEntity variableEntity = entityFinder.findOne(variableRepository,
//                                                             predicate,
//                                                             "Unable to find variableEntity with name '" + variableName + "' for process instance '" + processInstanceId + "'");
//        variableEntity.setMarkedAsDeleted(true);
//        variableRepository.save(variableEntity);
	}
}
