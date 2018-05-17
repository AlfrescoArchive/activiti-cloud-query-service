package org.activiti.cloud.starter.query.configuration;

import org.activiti.cloud.services.security.AuthenticationWrapper;
import org.activiti.cloud.services.security.SecurityPoliciesApplicationService;
import org.activiti.cloud.services.security.TaskLookupRestrictionService;
import org.activiti.cloud.services.security.VariableLookupRestrictionService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"org.activiti.cloud.services.query"})
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

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationWrapper authenticationWrapper(){
        return new AuthenticationWrapper();
    }

}
