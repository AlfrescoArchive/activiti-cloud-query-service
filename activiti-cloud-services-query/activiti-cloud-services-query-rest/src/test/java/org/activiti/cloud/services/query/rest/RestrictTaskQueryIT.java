package org.activiti.cloud.services.query.rest;

import com.querydsl.core.types.Predicate;
import org.activiti.cloud.services.query.app.repository.TaskCandidateGroupRepository;
import org.activiti.cloud.services.query.app.repository.TaskCandidateUserRepository;
import org.activiti.cloud.services.query.app.repository.TaskRepository;
import org.activiti.cloud.services.query.model.Task;
import org.activiti.cloud.services.query.model.TaskCandidateGroup;
import org.activiti.cloud.services.query.model.TaskCandidateUser;
import org.activiti.cloud.services.security.AuthenticationWrapper;
import org.activiti.cloud.services.security.TaskLookupRestrictionService;
import org.activiti.engine.UserGroupLookupProxy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { TaskRepository.class, Task.class, TaskCandidateUserRepository.class, TaskCandidateUser.class, TaskCandidateGroupRepository.class, TaskCandidateGroup.class, TaskLookupRestrictionService.class})
@EnableConfigurationProperties
@EnableJpaRepositories(basePackages = "org.activiti")
@EntityScan("org.activiti")
@EnableAutoConfiguration
public class RestrictTaskQueryIT {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskCandidateUserRepository taskCandidateUserRepository;

    @Autowired
    private TaskLookupRestrictionService taskLookupRestrictionService;

    @MockBean
    private AuthenticationWrapper authenticationWrapper;

    @MockBean
    private UserGroupLookupProxy userGroupLookupProxy;

    @Test
    public void shouldGetTasksWhenCandidate() throws Exception {

        Task task = new Task();
        task.setId("1");
        taskRepository.save(task);

        TaskCandidateUser taskCandidateUser = new TaskCandidateUser("1","testuser");
        taskCandidateUserRepository.save(taskCandidateUser);

        when(authenticationWrapper.getAuthenticatedUserId()).thenReturn("testuser");

        Predicate predicate = taskLookupRestrictionService.restrictTaskQuery(null);

        Iterable<Task> iterable = taskRepository.findAll(predicate);
        assertThat(iterable.iterator().hasNext()).isTrue();
    }

    @Test
    public void shouldNotGetTasksWhenNotCandidate() throws Exception {

        Task task = new Task();
        task.setId("1");
        taskRepository.save(task);

        TaskCandidateUser taskCandidateUser = new TaskCandidateUser("1","testuser");
        taskCandidateUserRepository.save(taskCandidateUser);

        when(authenticationWrapper.getAuthenticatedUserId()).thenReturn("fred");

        Predicate predicate = taskLookupRestrictionService.restrictTaskQuery(null);

        Iterable<Task> iterable = taskRepository.findAll(predicate);
        assertThat(iterable.iterator().hasNext()).isFalse();
    }

}
