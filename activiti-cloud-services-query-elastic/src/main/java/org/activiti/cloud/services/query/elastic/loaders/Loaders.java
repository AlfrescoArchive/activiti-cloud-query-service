package org.activiti.cloud.services.query.elastic.loaders;

import java.util.List;

import javax.annotation.PostConstruct;

import org.activiti.cloud.services.query.elastic.model.Task;
import org.activiti.cloud.services.query.elastic.repository.ProcessInstanceRepository;
import org.activiti.cloud.services.query.elastic.repository.TaskRepository;
import org.activiti.cloud.services.query.elastic.repository.VariableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Loaders 
{
    @Autowired
    ElasticsearchOperations operations;

    @Autowired
    TaskRepository taskRepository;
    
    @Autowired
    VariableRepository variableRepository;
    
    @Autowired
    ProcessInstanceRepository processInstanceRepository;

    @PostConstruct
    @Transactional
    public void loadAll()
    {

    }

    /*private List<Task> getData() 
    {
    	
    }*/
}
