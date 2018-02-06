package org.activiti.cloud.services.query.elastic.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document(indexName = "activiti", type = "task")
public class Task {

	@Id
	private String id;
	private String assignee;
	private String name;
	private String description;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date createTime;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date dueDate;
	private String priority;
	private String category;
	private String processDefinitionId;
	private String processInstanceId;
	private String status;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date lastModified;

	// only for querying on status (getter is transient, has to be getter so that
	// included in QProcessInstance)
	private String nameLike;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date lastModifiedTo;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date lastModifiedFrom;

	@OneToMany
	private List<Variable> variables;

	public Task() {
	}

	@JsonCreator
	public Task(@JsonProperty("id") String id, @JsonProperty("assignee") String assignee,
			@JsonProperty("name") String name, @JsonProperty("description") String description,
			@JsonProperty("createTime") Date createTime, @JsonProperty("dueDate") Date dueDate,
			@JsonProperty("priority") String priority, @JsonProperty("category") String category, // TODO: what is this?
																									// it doesn't get
																									// populated
			@JsonProperty("processDefinitionId") String processDefinitionId,
			@JsonProperty("processInstanceId") String processInstanceId, @JsonProperty("status") String status,
			@JsonProperty("lastModified") Date lastModified, @JsonProperty("variables") List<Variable> variables) {
		this.id = id;
		this.assignee = assignee;
		this.name = name;
		this.description = description;
		this.createTime = createTime;
		this.dueDate = dueDate;
		this.priority = priority;
		this.category = category;
		this.processDefinitionId = processDefinitionId;
		this.processInstanceId = processInstanceId;
		this.status = status;
		this.lastModified = lastModified;
		this.variables = variables;
	}

	public String getId() {
		return id;
	}

	public String getAssignee() {
		return assignee;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public String getPriority() {
		return priority;
	}

	public String getCategory() {
		return category;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public String getStatus() {
		return status;
	}

	@JsonIgnore // TODO: without ignore keep getting Could not write JSON: java.sql.Timestamp
				// cannot be cast to java.lang.String error - why?
	public Date getLastModified() {
		return lastModified;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	@JsonIgnore
	public List<Variable> getVariables() {
		return variables;
	}

	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}

	@Transient
	public Date getLastModifiedTo() {
		return lastModifiedTo;
	}

	public void setLastModifiedTo(Date lastModifiedTo) {
		this.lastModifiedTo = lastModifiedTo;
	}

	@Transient
	public Date getLastModifiedFrom() {
		return lastModifiedFrom;
	}

	public void setLastModifiedFrom(Date lastModifiedFrom) {
		this.lastModifiedFrom = lastModifiedFrom;
	}

	@Transient
	public String getNameLike() {
		return nameLike;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}
}
