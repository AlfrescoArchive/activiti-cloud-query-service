package org.activiti.cloud.services.query.model.elastic;

import java.util.Date;

import javax.persistence.Convert;

import org.springframework.format.annotation.DateTimeFormat;

public class ValuePls {

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

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

	public <T> T getValue() {
		return (T) value.getValue();
	}

	public Boolean getMarkedAsDeleted() {
		return markedAsDeleted;
	}

	public void setMarkedAsDeleted(Boolean markedAsDeleted) {
		this.markedAsDeleted = markedAsDeleted;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
