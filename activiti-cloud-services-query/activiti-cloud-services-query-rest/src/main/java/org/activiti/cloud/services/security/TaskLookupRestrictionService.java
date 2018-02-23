package org.activiti.cloud.services.security;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.activiti.cloud.services.query.model.QTask;
import org.activiti.engine.UserGroupLookupProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskLookupRestrictionService {

    @Autowired(required = false)
    private UserGroupLookupProxy userGroupLookupProxy;

    @Autowired
    private AuthenticationWrapper authenticationWrapper;

    public Predicate restrictTaskQuery(Predicate predicate){

        BooleanExpression securityExpression = null;

        //get authenticated user
        String userId = authenticationWrapper.getAuthenticatedUserId();

        BooleanExpression restriction = null;

        if(userId!=null) {


            QTask task = QTask.task;

            //user is assignee
            restriction = addOrConditionToExpression(restriction,task.assignee.eq(userId));

            //or user is a candidate
            restriction = addOrConditionToExpression(restriction,task.taskCandidateUsers.any().userId.eq(userId));

            //or one of user's group is candidate

            List<String> groups = null;
            if (userGroupLookupProxy != null) {
                groups = userGroupLookupProxy.getGroupsForCandidateUser(userId);
            }
            BooleanExpression userInACandidateGroupForTask = null;
            if(groups!=null && groups.size()>0) {
                restriction = addOrConditionToExpression(restriction,task.taskCandidateGroups.any().groupId.in(groups));
            }

            //or there are no candidates set
            restriction = addOrConditionToExpression(restriction,task.taskCandidateUsers.isEmpty().and(task.taskCandidateGroups.isEmpty()));

        }

        return addAndConditionToPredicate(predicate,restriction);
    }

    Predicate addAndConditionToPredicate(Predicate predicate, BooleanExpression expression){
        if(expression != null && predicate !=null){
            return expression.and(predicate);
        }
        if(expression == null){
            return predicate;
        }
        return expression;
    }

    BooleanExpression addOrConditionToExpression(BooleanExpression predicate, BooleanExpression expression){
        if(expression != null && predicate !=null){
            return expression.or(predicate);
        }
        if(expression == null){
            return predicate;
        }
        return expression;
    }

}
