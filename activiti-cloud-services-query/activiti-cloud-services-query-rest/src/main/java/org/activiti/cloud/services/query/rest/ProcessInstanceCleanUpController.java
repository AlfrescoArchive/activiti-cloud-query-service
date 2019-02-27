package org.activiti.cloud.services.query.rest;

import com.querydsl.core.types.Predicate;
import org.activiti.cloud.alfresco.data.domain.AlfrescoPagedResourcesAssembler;
import org.activiti.cloud.services.query.app.repository.ProcessInstanceRepository;
import org.activiti.cloud.services.query.model.ProcessInstanceEntity;
import org.activiti.cloud.services.query.resources.ProcessInstanceResource;
import org.activiti.cloud.services.query.rest.assembler.ProcessInstanceResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        value = "/admin/v1/process-instances",
        produces = {
                MediaTypes.HAL_JSON_VALUE,
                MediaType.APPLICATION_JSON_VALUE
        })
public class ProcessInstanceCleanUpController {

    private final ProcessInstanceRepository processInstanceRepository;

    private ProcessInstanceResourceAssembler processInstanceResourceAssembler;

    private AlfrescoPagedResourcesAssembler<ProcessInstanceEntity> pagedResourcesAssembler;

    @Autowired
    public ProcessInstanceCleanUpController(ProcessInstanceRepository processInstanceRepository,
                                          ProcessInstanceResourceAssembler processInstanceResourceAssembler,
                                          AlfrescoPagedResourcesAssembler<ProcessInstanceEntity> pagedResourcesAssembler) {
        this.processInstanceRepository = processInstanceRepository;
        this.processInstanceResourceAssembler = processInstanceResourceAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/export")
    public PagedResources<ProcessInstanceResource> exportProcessInstances (@QuerydslPredicate(root = ProcessInstanceEntity.class) Predicate predicate,
                                                                           Pageable pageable,
                                                                           boolean delete) {

        PagedResources<ProcessInstanceResource> result = pagedResourcesAssembler.toResource(pageable,
                processInstanceRepository.findAll(predicate,
                        pageable),
                processInstanceResourceAssembler);

        if(delete)
        processInstanceRepository.deleteAll();

        return result;
    }







}
