package org.activiti.cloud.services.query.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.exceptions.XMLException;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.cloud.services.query.ProcessDiagramGeneratorWrapper;
import org.activiti.cloud.services.query.app.repository.EntityFinder;
import org.activiti.cloud.services.query.app.repository.ProcessInstanceRepository;
import org.activiti.cloud.services.query.app.repository.ProcessModelRepository;
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

    private final ProcessDiagramGeneratorWrapper processDiagramGenerator;

    @Autowired
    public ProcessInstanceDiagramController(ProcessModelRepository processModelRepository,
                                            ProcessDiagramGeneratorWrapper processDiagramGenerator,
                                            ProcessInstanceRepository processInstanceRepository,
                                            EntityFinder entityFinder) {
        
        this.processInstanceRepository = processInstanceRepository;
        this.processModelRepository = processModelRepository;
        this.entityFinder = entityFinder;
        this.processDiagramGenerator = processDiagramGenerator;

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
        List<String> highLightedFlows = resolveCompletedActivities(bpmnModel, processInstanceId);

        return new String(processDiagramGenerator.generateDiagram(bpmnModel,
                                                                  highLightedActivities,
                                                                  highLightedFlows),
                          StandardCharsets.UTF_8);

    }

    protected List<String> resolveCompletedActivities(BpmnModel bpmnModel, String processInstanceId) {
        List<String> completedFlows = new LinkedList<String>();
        List<String> activeAndHistorcActivityIds = processInstanceRepository.findActivitiIds(processInstanceId);

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
        List<String> activeActivitiesIds = new LinkedList<>();
        try {
            activeActivitiesIds.addAll(processInstanceRepository.findByActiveActivityIds(processInstanceId));
        } catch (Exception ignore) {
        }

        return activeActivitiesIds;
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

        InputStreamReader in = null;

        try {
            in = new InputStreamReader(new ByteArrayInputStream(processModelContent.getBytes()), "utf-8");

            XMLInputFactory xif = XMLInputFactory.newInstance();

            if (xif.isPropertySupported(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES)) {
                xif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
            }

            if (xif.isPropertySupported(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES)) {
                xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            }

            if (xif.isPropertySupported(XMLInputFactory.SUPPORT_DTD)) {
                xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            }

            XMLStreamReader xtr = xif.createXMLStreamReader(in);

            BpmnXMLConverter converter = new BpmnXMLConverter();

            BpmnModel bpmnModel = converter.convertToBpmnModel(xtr);

            return bpmnModel;
            
        } catch (UnsupportedEncodingException e) {
            throw new XMLException("The bpmn 2.0 xml is not UTF8 encoded", e);
        } catch (XMLStreamException e) {
            throw new XMLException("Error while reading the BPMN 2.0 XML", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // LOGGER.debug("Problem closing BPMN input stream", e);
                }
            }
        }

    }

}
