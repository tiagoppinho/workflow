package module.metaWorkflow.domain;

import java.util.ArrayList;
import java.util.List;

import module.organization.domain.AccountabilityType;
import module.organization.domain.Unit;
import module.workflow.domain.WorkflowQueue;
import module.workflow.util.WorkflowQueueBean;
import pt.ist.fenixWebFramework.services.Service;

public class WorkflowUnitQueueBean extends WorkflowQueueBean {

    private static final long serialVersionUID = 1L;

    private Unit unit;
    private List<AccountabilityType> accountabilityTypes;

    public WorkflowUnitQueueBean() {
	setAccountabilityTypes(new ArrayList<AccountabilityType>());
	setUnit(null);
    }

    public Unit getUnit() {
	return unit;
    }

    public void setUnit(Unit unit) {
	this.unit = unit;
    }

    public void setAccountabilityTypes(List<AccountabilityType> accountabilityTypes) {
	this.accountabilityTypes = new ArrayList<AccountabilityType>();
	for (AccountabilityType type : accountabilityTypes) {
	    this.accountabilityTypes.add(type);
	}
    }

    public List<AccountabilityType> getAccountabilityTypes() {
	return accountabilityTypes;
    }

    @Override
    protected void fillQueueFields(WorkflowQueue queue) {
	setAccountabilityTypes(((WorkflowUnitQueue) queue).getAccountabilityTypes());
    }

    @Override
    @Service
    public WorkflowUnitQueue createWorkflowQueue() {
	return new WorkflowUnitQueue(getUnit(), getName(), getAccountabilityTypes());
    }

}
