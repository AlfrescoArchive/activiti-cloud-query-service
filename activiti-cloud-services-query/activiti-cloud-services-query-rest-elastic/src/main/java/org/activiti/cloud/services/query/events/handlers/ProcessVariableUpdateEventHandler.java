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

import org.activiti.cloud.services.query.model.elastic.Variable;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Component;

@Component
public class ProcessVariableUpdateEventHandler {

	private final VariableUpdater variableUpdater;

	@Autowired
	public ProcessVariableUpdateEventHandler(VariableUpdater variableUpdater) {
		this.variableUpdater = variableUpdater;
	}

	public void handle(Variable updatedVariableEntity) {
		String variableName = updatedVariableEntity.getName();
		String processInstanceId = updatedVariableEntity.getProcessInstanceId();
		// TODO complete this method pls

		// BooleanExpression predicate =
		// QVariableEntity.variableEntity.name.eq(variableName)
//                .and(
//                        QVariableEntity.variableEntity.processInstanceId.eq(String.valueOf(processInstanceId))
//                );
		
//		new QueryStringQueryBuilder().
//		StringQuery stringQuery = new StringQuery(termQuery("name", variableName).termQuery("name", variableName).toString());
//        variableUpdater.update(updatedVariableEntity,
//                               null, 
//                               "Unable to find variable named '" + variableName + "' for process instance '" + processInstanceId + "'");
	}
}
