/*
 * Copyright 2017 Alfresco, Inc. and/or its affiliates.
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

package org.activiti.cloud.services.query.elastic.repository;

import java.util.List;

import org.activiti.cloud.services.query.elastic.model.Variable;
import org.activiti.cloud.services.query.model.QVariable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(exported = false)
public interface VariableRepository extends ElasticsearchRepository<Variable, Long>,
QuerydslPredicateExecutor<Variable>, QuerydslBinderCustomizer<QVariable> 
{
	@RestResource(exported = false)
    String findVariableById();
	
	@RestResource(exported = false)
    String findVariableByType();
	
	@RestResource(exported = false)
    String findVariableByName();
	
	@RestResource(exported = false)
    List<Variable> findVariableByProcessInstanceId();

    @RestResource(exported = false)
    List<Variable> findVariableByTaskId();
    
    @RestResource(exported = false)
    List<Variable> findVariableByCreateTime();

    @RestResource(exported = false)
    List<Variable> findVariableByLastUpdatedTime();
    
    @RestResource(exported = false)
    List<Variable> findVariableByExecutionId();
}