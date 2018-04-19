package org.activiti.cloud.services.query.elastic.rest.resource;


import org.activiti.cloud.services.query.elastic.builders.SearchQueryBuilder;
import org.activiti.cloud.services.query.elastic.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/manual/search")
public class ManualSearchResource {

    @Autowired
    private SearchQueryBuilder searchQueryBuilder;

    @GetMapping(value = "/{text}")
    public List<Task> getAll(@PathVariable final String text) {
        return searchQueryBuilder.getAll(text);
    }
}
