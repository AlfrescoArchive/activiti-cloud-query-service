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

package org.activiti.cloud.services.query.model.elastic;

import java.util.Date;
import java.util.Set;

import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.activiti.cloud.api.model.shared.CloudVariableInstance;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Variable extends ActivitiEntityMetadata implements CloudVariableInstance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	private String type;

	private String name;

	private String processInstanceId;

	private String taskId;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date createTime;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date lastUpdatedTime;

	private String executionId;

	@Convert(converter = VariableValueJsonConverter.class)
	private VariableValue<?> value;

	private Boolean markedAsDeleted = false;	

	@JsonIgnore
	private Task task;

	@JsonIgnore
	private ProcessInstance processInstance;

	public Variable() {
	}

	public Variable(String id,
			String type,
			String name,
			String processInstanceId,
			String serviceName,
			String serviceFullName,
			String serviceVersion,
			String appName,
			String appVersion,
			String taskId,
			Date createTime,
			Date lastUpdatedTime,
			String executionId) {
		super(serviceName, serviceFullName, serviceVersion, appName, appVersion);
		this.id = id;
		this.type = type;
		this.name = name;
		this.processInstanceId = processInstanceId;
		this.taskId = taskId;
		this.createTime = createTime;
		this.lastUpdatedTime = lastUpdatedTime;
		this.executionId = executionId;
	}

	public String getId() {
		return id;
	}

	@Override
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	@Override
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(Date lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public <T> void setValue(T value) {
		this.value = new VariableValue<>(value);
	}

	@Override
	public <T> T getValue() {
		return (T) value.getValue();
	}

	public ProcessInstance getProcessInstance() {
		return this.processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	public Task getTask() {
		return this.task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Boolean getMarkedAsDeleted() {
		return markedAsDeleted;
	}

	public void setMarkedAsDeleted(Boolean markedAsDeleted) {
		this.markedAsDeleted = markedAsDeleted;
	}

	@Override
	public boolean isTaskVariable() {
		return taskId != null;
	}
		
}