package org.activiti.cloud.services.security;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.activiti.cloud.services.query.model.QVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VariableLookupRestrictionService {

    @Autowired
    private TaskLookupRestrictionService taskLookupRestrictionService;

    @Autowired
    private SecurityPoliciesApplicationService securityPoliciesApplicationService;

    public Predicate restrictVariableQuery(Predicate predicate){
        //TODO: need to construct OR differently, prob by constructing expression together and exposing more from the collaborators
        Predicate predicateTask = restrictTaskVariableQuery(predicate);
        Predicate predicateProc = restrictProcessInstanceVariableQuery(predicate);
        return QVariable.variable.name.isNotNull().andAnyOf(predicateTask,predicateProc);
    }

    public Predicate restrictTaskVariableQuery(Predicate predicate){
        return taskLookupRestrictionService.restrictTaskVariableQuery(predicate);
    }

    public Predicate restrictProcessInstanceVariableQuery(Predicate predicate){
        return securityPoliciesApplicationService.restrictProcessInstanceVariableQuery(predicate, SecurityPolicy.READ);
    }

}
