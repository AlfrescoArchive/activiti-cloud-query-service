package org.activiti.cloud.services.query.model.elastic;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.activiti.cloud.api.process.model.CloudProcessInstance;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

@Document(indexName = "process_instance", type = "_doc")
public class ProcessInstance extends ActivitiEntityMetadata implements CloudProcessInstance {

	@Id
	private String id;
	private String name;
	private String description;
	private String processDefinitionId;
	private String processDefinitionKey;
	private String initiator;
	private Date startDate;
	private String businessKey;

	@Field(type = FieldType.Text)
	private ProcessInstanceStatus status;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date lastModified;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date lastModifiedTo;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date lastModifiedFrom;

//    @JsonIgnore
//    @OneToMany(mappedBy = "processInstance")
//    @org.hibernate.annotations.ForeignKey(name = "none")
//    private Set<TaskEntity> tasks;

	private Map<String, Set<Variable>> variables;

	public ProcessInstance() {
	}

	public ProcessInstance(
			String serviceName,
			String serviceFullName,
			String serviceVersion,
			String appName,
			String appVersion,
			String processInstanceId,
			String processDefinitionId,
			ProcessInstanceStatus status,
			Date lastModified) {
		super(serviceName, serviceFullName, serviceVersion, appName, appVersion);
		this.id = processInstanceId;
		this.processDefinitionId = processDefinitionId;
		this.status = status;
		this.lastModified = lastModified;
	}

	@Override
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	@Override
	public ProcessInstanceStatus getStatus() {
		return status;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public void setStatus(ProcessInstanceStatus status) {
		this.status = status;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
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

	@Override
	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}

	public void setProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getInitiator() {
		return initiator;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	@Override
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Override
	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public Map<String, Set<Variable>> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Set<Variable>> variables) {
		this.variables = variables;
	}

}
