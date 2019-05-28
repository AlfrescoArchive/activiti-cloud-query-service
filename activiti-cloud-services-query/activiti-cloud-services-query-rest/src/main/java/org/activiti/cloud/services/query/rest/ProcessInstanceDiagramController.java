package org.activiti.cloud.services.query.rest;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.cloud.services.query.ProcessDiagramGeneratorWrapper;
import org.activiti.cloud.services.query.app.repository.BPMNActivityRepository;
import org.activiti.cloud.services.query.app.repository.EntityFinder;
import org.activiti.cloud.services.query.app.repository.ProcessInstanceRepository;
import org.activiti.cloud.services.query.app.repository.ProcessModelRepository;
import org.activiti.cloud.services.query.model.BPMNActivityEntity;
import org.activiti.cloud.services.query.model.BPMNActivityEntity.BPMNActivityStatus;
import org.activiti.cloud.services.query.model.ProcessInstanceEntity;
import org.activiti.cloud.services.query.model.ProcessModelEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/process-instances/{processInstanceId}/diagram")
public class ProcessInstanceDiagramController {

    private final ProcessModelRepository processModelRepository;

    private final EntityFinder entityFinder;

    private final ProcessInstanceRepository processInstanceRepository;
    
    private final BPMNActivityRepository bpmnActivityRepository;

    private final ProcessDiagramGeneratorWrapper processDiagramGenerator;

    @Autowired
    public ProcessInstanceDiagramController(ProcessModelRepository processModelRepository,
                                            ProcessDiagramGeneratorWrapper processDiagramGenerator,
                                            ProcessInstanceRepository processInstanceRepository,
                                            BPMNActivityRepository bpmnActivityRepository,
                                            EntityFinder entityFinder) {
        
        this.processInstanceRepository = processInstanceRepository;
        this.processModelRepository = processModelRepository;
        this.entityFinder = entityFinder;
        this.processDiagramGenerator = processDiagramGenerator;
        this.bpmnActivityRepository = bpmnActivityRepository;

    }

    @RequestMapping(method = RequestMethod.GET, produces = "image/svg+xml")
    @ResponseBody
    public String getProcessDiagram(@PathVariable String processInstanceId) {
        return generateDiagram(processInstanceId);
    }

    public String generateDiagram(String processInstanceId) {
        String processDefinitionId = resolveProcessDefinitionId(processInstanceId);
        BpmnModel bpmnModel = getBpmnModel(processDefinitionId);

        List<String> highLightedActivities = resolveActiveActivitiesIds(processInstanceId);
        List<String> highLightedFlows = resolveCompletedFlows(bpmnModel, processInstanceId);

        return new String(processDiagramGenerator.generateDiagram(bpmnModel,
                                                                  highLightedActivities,
                                                                  highLightedFlows),
                          StandardCharsets.UTF_8);

    }

    protected List<String> resolveCompletedFlows(BpmnModel bpmnModel, String processInstanceId) {
        List<String> completedFlows = new LinkedList<String>();
        List<String> activeAndHistorcActivityIds = bpmnActivityRepository.findByProcessInstanceId(processInstanceId)
                                                                         .stream()
                                                                         .map(BPMNActivityEntity::getId)
                                                                         .collect(Collectors.toList());

        for (org.activiti.bpmn.model.Process process : bpmnModel.getProcesses()) {
            for (FlowElement flowElement : process.getFlowElements()) {
                if (flowElement instanceof SequenceFlow) {
                    SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
                    String from = sequenceFlow.getSourceRef();
                    String to = sequenceFlow.getTargetRef();
                    if (activeAndHistorcActivityIds.contains(from) && activeAndHistorcActivityIds.contains(to)) {
                        completedFlows.add(sequenceFlow.getId());
                    }
                }
            }
        }

        return completedFlows;

    }

    protected List<String> resolveActiveActivitiesIds(String processInstanceId) {
        return bpmnActivityRepository.findByProcessInstanceIdAndStatus(processInstanceId, BPMNActivityStatus.COMPLETED)
                                     .stream()
                                     .map(BPMNActivityEntity::getId)
                                     .collect(Collectors.toList());
    }

    protected String resolveProcessDefinitionId(String processInstanceId) {

        ProcessInstanceEntity processInstanceEntity = entityFinder.findById(processInstanceRepository,
                                                                            processInstanceId,
                                                                            "Unable to find process instance for the given id:'" + processInstanceId + "'");

        return processInstanceEntity.getProcessDefinitionId();
    }

    protected BpmnModel getBpmnModel(String processDefinitionId) {
        ProcessModelEntity processModelEntity = entityFinder.findById(processModelRepository,
                                                                      processDefinitionId,
                                                                      "Unable to find process model for the given id:'" + processDefinitionId + "`");

        String processModelContent = processModelEntity.getProcessModelContent();

        return processDiagramGenerator.parseBpmnModelXml(new ByteArrayInputStream(processModelContent.getBytes()));
    }

}
