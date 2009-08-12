package module.metaWorkflow.domain;

import module.metaWorkflow.util.WorkflowQueueBean;
import module.organization.domain.Party;
import module.organization.domain.Person;
import module.organization.domain.Unit;
import myorg.domain.User;
import pt.ist.fenixWebFramework.services.Service;

public class WorkflowUnitQueue extends WorkflowUnitQueue_Base {

    public WorkflowUnitQueue(String name, WorkflowMetaType metaType) {
	super();
	init(metaType, name);
    }

    public WorkflowUnitQueue(Unit unit, WorkflowMetaType metaType, String name) {
	super();
	init(metaType, name);
	setUnit(unit);
    }

    @Override
    protected void fillNonDefaultFields(WorkflowQueueBean bean) {
	WorkflowUnitQueueBean unitQueueBean = (WorkflowUnitQueueBean) bean;
	setUnit(unitQueueBean.getUnit());
	getAccountabilityTypes().clear();
	getAccountabilityTypes().addAll(unitQueueBean.getAccountabilityTypes());
    }

    @Override
    @Service
    public void edit(WorkflowQueueBean bean) {
	WorkflowUnitQueueBean unitQueueBean = (WorkflowUnitQueueBean) bean;
	setName(bean.getName());
	getAccountabilityTypes().clear();
	getAccountabilityTypes().addAll(unitQueueBean.getAccountabilityTypes());
    }

    @Override
    public boolean isUserAbleToAccessQueue(User user) {
	if (user == null) {
	    return false;
	}

	Person person = user.getPerson();
	for (Party party : getUnit().getChildren(getAccountabilityTypes())) {
	    if (person == party) {
		return true;
	    }
	}

	return false;
    }

}
