/*
2 * Copyright 2017 Alfresco, Inc. and/or its affiliates.
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

import org.activiti.cloud.services.query.elastic.model.ProcessInstance;
import org.activiti.cloud.services.query.elastic.model.Variable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(exported = false)
public interface ProcessInstanceRepository extends ElasticsearchRepository<ProcessInstance, Long>
{
    @RestResource(exported = false)
    List<ProcessInstance> findProcessInstances();

    @RestResource(exported = false)
    List<ProcessInstance> findLastModified();
    
    @RestResource(exported = false)
    List<ProcessInstance> findLastModifiedFrom();
    
    @RestResource(exported = false)
    List<ProcessInstance> findLastModifiedTo();
    
    @RestResource(exported = false)
    List<ProcessInstance> findProcessInstanceVariables();
   
    @RestResource(exported = false)
    List<ProcessInstance> findByStatus();
    
    @RestResource(exported = false)
    List<ProcessInstance> findCompletedProcessInstances();
    
    @RestResource(exported = false)
    List<Variable> findVariables();
    
    @RestResource(exported = false)
    Long findProcessInstanceById();
    
    @RestResource(exported = false)
    String findProcessDefinitionById();
}