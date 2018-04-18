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

package org.activiti.cloud.services.query.elastic.repository;

import org.activiti.cloud.services.query.elastic.model.TaskCandidateGroup;
import org.activiti.cloud.services.query.elastic.model.TaskCandidateGroupId;
import org.activiti.cloud.services.query.elastic.model.TaskCandidateUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/*@RepositoryRestResource(exported = false)
public interface TaskCandidateGroupRepository extends ElasticsearchRepository<TaskCandidateGroup, TaskCandidateGroupId>,
		QuerydslPredicateExecutor<TaskCandidateUser>, QuerydslBinderCustomizer<QTaskCandidateUser> {

}*/