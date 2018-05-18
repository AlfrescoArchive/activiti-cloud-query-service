package org.activiti.cloud.starter.query.configuration;

import org.activiti.cloud.services.query.app.QueryConsumerChannelHandler;
import org.activiti.cloud.services.query.app.QueryConsumerChannels;
import org.activiti.cloud.services.query.app.repository.EntityFinder;
import org.activiti.cloud.services.query.events.handlers.QueryEventHandler;
import org.activiti.cloud.services.query.rest.ProcessInstanceAdminController;
import org.activiti.cloud.services.query.rest.QueryControllers;
import org.activiti.cloud.services.query.rest.config.QueryRepositoryConfig;
import org.activiti.cloud.services.security.SecurityPoliciesApplicationService;
import org.activiti.cloud.services.security.TaskLookupRestrictionService;
import org.activiti.cloud.services.security.VariableLookupRestrictionService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackageClasses = {QueryEventHandler.class, QueryControllers.class,EntityFinder.class})
@Import({QueryRepositoryConfig.class, QueryConsumerChannelHandler.class})
public class ActivitiQueryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SecurityPoliciesApplicationService securityPoliciesApplicationService(){
        return new SecurityPoliciesApplicationService();
    }

    @Bean
    @ConditionalOnMissingBean
    public TaskLookupRestrictionService taskLookupRestrictionService(){
        return new TaskLookupRestrictionService();
    }

    @Bean
    @ConditionalOnMissingBean
    public VariableLookupRestrictionService variableLookupRestrictionService(){
        return new VariableLookupRestrictionService();
    }

}
