package org.activiti.cloud.services.query.rest;

import com.querydsl.core.types.Predicate;
import org.activiti.cloud.alfresco.data.domain.AlfrescoPagedResourcesAssembler;
import org.activiti.cloud.services.query.app.repository.EntityFinder;
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

    private EntityFinder entityFinder;

    @Autowired
    public ProcessInstanceCleanUpController(ProcessInstanceRepository processInstanceRepository,
                                          ProcessInstanceResourceAssembler processInstanceResourceAssembler,
                                          AlfrescoPagedResourcesAssembler<ProcessInstanceEntity> pagedResourcesAssembler,
                                          EntityFinder entityFinder) {
        this.processInstanceRepository = processInstanceRepository;
        this.processInstanceResourceAssembler = processInstanceResourceAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.entityFinder=entityFinder;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public PagedResources<ProcessInstanceResource> deleteProcessInstances (@QuerydslPredicate(root = ProcessInstanceEntity.class) Predicate predicate,
                                                                           Pageable pageable) {

        PagedResources<ProcessInstanceResource> result = pagedResourcesAssembler.toResource(pageable,
                processInstanceRepository.findAll(predicate,
                        pageable),
                processInstanceResourceAssembler);

        processInstanceRepository.deleteAll();

        return result;
    }







}
