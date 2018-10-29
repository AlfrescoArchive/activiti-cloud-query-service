package org.activiti.cloud.services.query.app.repository.elastic;

import org.activiti.cloud.services.query.model.elastic.Task;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface TaskRepository extends ElasticsearchRepository<Task, String> {
}
