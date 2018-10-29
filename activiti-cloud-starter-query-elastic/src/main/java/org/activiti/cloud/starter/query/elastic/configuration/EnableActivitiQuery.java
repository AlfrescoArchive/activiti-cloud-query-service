package org.activiti.cloud.starter.query.elastic.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
//@EnableJpaRepositories({"org.activiti.cloud.services.query.app.repository"})
//@EntityScan(basePackages = {"org.activiti.cloud.services.query.model"})
@Inherited
@EnableDiscoveryClient
@EnableAutoConfiguration
@EnableWebSecurity
public @interface EnableActivitiQuery {

}
