package org.activiti.cloud.services.query.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.activiti.cloud.api.process.model.CloudBPMNActivity;
import org.springframework.format.annotation.DateTimeFormat;

@Entity(name="BPMNActivity")
@Table(name="BPMN_ACTIVITY", indexes={
    @Index(name="bpmn_activity_status_idx", columnList="status", unique=false),
    @Index(name="bpmn_activity_processInstance_idx", columnList="processInstanceId", unique=false),
    @Index(name="bpmn_activity_processInstance_elementId_idx", columnList="processInstanceId,elementId", unique=true)
})
public class BPMNActivityEntity extends ActivitiEntityMetadata implements CloudBPMNActivity {
    
    public static enum BPMNActivityStatus {
        STARTED, COMPLETED, CANCELLED
    }
    
    /** The unique identifier of this historic activity instance. */
    @Id
    private String id;

    /** The unique identifier of the activity in the process */
    private String elementId;       
    
    /** The display name for the activity */
    private String activityName;
    
    /** The XML tag of the activity as in the process file */
    private String activityType;

    /** The associated process instance id */
    private String processInstanceId;

    /** The associated process definition id */
    private String processDefinitionId;
    
    /** The current state of activity */
    @Enumerated(EnumType.STRING)
    private BPMNActivityStatus status;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date startedDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)    
    private Date completedDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)    
    private Date cancelledDate;
    
    public BPMNActivityEntity() {}
    
    public BPMNActivityEntity(String serviceName,
                                   String serviceFullName,
                                   String serviceVersion,
                                   String appName,
                                   String appVersion) {
        super(serviceName,
              serviceFullName,
              serviceVersion,
              appName,
              appVersion);
    }    
    
    public String getId() {
        return id;
    }

    @Override
    public String getElementId() {
        return elementId;
    };

    public String getActivityName() {
        return activityName;
    };

    public String getActivityType() {
        return activityType;
    }; 

    @Override
    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    @Override
    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public BPMNActivityStatus getStatus() {
        return status;
    }

    public void setStatus(BPMNActivityStatus status) {
        this.status = status;
    }

    
    public Date getStartedDate() {
        return startedDate;
    }

    
    public void setStartedDate(Date startedDate) {
        this.startedDate = startedDate;
    }

    
    public Date getCompletedDate() {
        return completedDate;
    }

    
    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    
    public void setId(String id) {
        this.id = id;
    }

    
    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    
    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    
    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    
    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    
    public Date getCancelledDate() {
        return cancelledDate;
    }

    
    public void setCancelledDate(Date cancelledDate) {
        this.cancelledDate = cancelledDate;
    }
}
