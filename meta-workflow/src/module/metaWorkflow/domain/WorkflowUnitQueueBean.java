package module.metaWorkflow.domain;

import java.util.ArrayList;
import java.util.List;

import module.metaWorkflow.util.WorkflowQueueBean;
import module.organization.domain.AccountabilityType;
import module.organization.domain.Unit;
import pt.ist.fenixWebFramework.util.DomainReference;

public class WorkflowUnitQueueBean extends WorkflowQueueBean {

    private DomainReference<Unit> unit;
    private List<DomainReference<AccountabilityType>> accountabilityTypes;

    public WorkflowUnitQueueBean() {
	setAccountabilityTypes(new ArrayList<AccountabilityType>());
	setUnit(null);
    }

    public Unit getUnit() {
	return unit.getObject();
    }

    public void setUnit(Unit unit) {
	this.unit = new DomainReference<Unit>(unit);
    }

    public void setAccountabilityTypes(List<AccountabilityType> accountabilityTypes) {
	this.accountabilityTypes = new ArrayList<DomainReference<AccountabilityType>>();
	for (AccountabilityType type : accountabilityTypes) {
	    this.accountabilityTypes.add(new DomainReference<AccountabilityType>(type));
	}
    }

    public List<AccountabilityType> getAccountabilityTypes() {
	List<AccountabilityType> types = new ArrayList<AccountabilityType>();
	for (DomainReference<AccountabilityType> domainReference : this.accountabilityTypes) {
	    types.add(domainReference.getObject());
	}
	return types;
    }

    @Override
    protected void fillQueueFields(WorkflowQueue queue) {
	setAccountabilityTypes(((WorkflowUnitQueue) queue).getAccountabilityTypes());
    }
}
