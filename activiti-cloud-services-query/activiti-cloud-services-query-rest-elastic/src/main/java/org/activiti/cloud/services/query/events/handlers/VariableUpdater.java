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

import org.activiti.cloud.services.query.app.repository.elastic.EntityFinder;
import org.activiti.cloud.services.query.app.repository.elastic.ProcessInstanceRepository;
import org.activiti.cloud.services.query.model.elastic.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.stereotype.Component;

@Component
public class VariableUpdater {

	private final EntityFinder entityFinder;

	private ProcessInstanceRepository processInstanceRepository;
	private ElasticsearchOperations elasticsearchTemplate;

	@Autowired
	public VariableUpdater(EntityFinder entityFinder, ProcessInstanceRepository processInstanceRepository,
			ElasticsearchOperations elasticsearchTemplate) {
		this.entityFinder = entityFinder;
		this.processInstanceRepository = processInstanceRepository;
		this.elasticsearchTemplate = elasticsearchTemplate;
	}

	public void update(Variable updatedVariableEntity, GetQuery query, String notFoundMessage) {
		Variable variableEntity = entityFinder.findOne(elasticsearchTemplate, query, notFoundMessage, Variable.class);
		variableEntity.setLastUpdatedTime(updatedVariableEntity.getLastUpdatedTime());
		variableEntity.setType(updatedVariableEntity.getType());
		variableEntity.setValue(updatedVariableEntity.getValue());

		// TODO implement this PLEASE!!!
//		variableRepository.save(variableEntity);
	}

}
