package org.activiti.cloud.services.query.rest;

import com.querydsl.core.types.Predicate;
import org.activiti.cloud.services.query.app.repository.ProcessInstanceRepository;
import org.activiti.cloud.services.query.model.ProcessInstanceEntity;
import org.activiti.cloud.services.query.resources.ProcessInstanceResource;
import org.activiti.cloud.services.query.rest.assembler.ProcessInstanceResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;

@ConditionalOnProperty(name = "activiti.enable-clean-up", havingValue = "true")
@RestController
@RequestMapping(
        value = "/admin/clean-up/v1/process-instances",
        produces = {
                MediaTypes.HAL_JSON_VALUE,
                MediaType.APPLICATION_JSON_VALUE
        })
public class ProcessInstanceCleanUpController {

    private final ProcessInstanceRepository processInstanceRepository;

    private ProcessInstanceResourceAssembler processInstanceResourceAssembler;

    @Autowired
    public ProcessInstanceCleanUpController(ProcessInstanceRepository processInstanceRepository,
                                          ProcessInstanceResourceAssembler processInstanceResourceAssembler) {
        this.processInstanceRepository = processInstanceRepository;
        this.processInstanceResourceAssembler = processInstanceResourceAssembler;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public Resources<ProcessInstanceResource> deleteProcessInstances (@QuerydslPredicate(root = ProcessInstanceEntity.class) Predicate predicate) {

        Collection<ProcessInstanceResource> result = new ArrayList<>();
        Iterable <ProcessInstanceEntity> iterable = processInstanceRepository.findAll(predicate);

        for(ProcessInstanceEntity entity : iterable){
            result.add(processInstanceResourceAssembler.toResource(entity));
        }

        processInstanceRepository.deleteAll(iterable);

        return new Resources<>(result);
    }







}
