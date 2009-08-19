package module.metaWorkflow.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import module.organization.domain.Party;
import module.organization.domain.Person;
import module.organization.domain.Unit;
import module.workflow.util.WorkflowQueueBean;
import myorg.domain.User;
import myorg.util.ClassNameBundle;
import pt.ist.fenixWebFramework.services.Service;

@ClassNameBundle(bundle="resources/MetaWorkflowResources")
public class WorkflowUnitQueue extends WorkflowUnitQueue_Base {

    public WorkflowUnitQueue(String name) {
	super();
	setName(name);
    }

    public WorkflowUnitQueue(Unit unit, String name) {
	super();
	setName(name);
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

    public static Set<WorkflowUnitQueue> getQueuesFor(Collection<Unit> units) {
	Set<WorkflowUnitQueue> queues = new HashSet<WorkflowUnitQueue>();
	for (Unit unit : units) {
	    queues.addAll(unit.getQueues());
	}
	return queues;
    }
}
