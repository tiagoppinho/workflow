package module.metaWorkflow.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import module.organization.domain.AccountabilityType;
import module.organization.domain.Person;
import module.organization.domain.Unit;
import module.workflow.domain.WorkflowQueue;
import module.workflow.domain.WorkflowSystem;
import module.workflow.util.WorkflowQueueBean;
import myorg.domain.User;
import myorg.domain.VirtualHost;
import myorg.util.ClassNameBundle;
import pt.ist.fenixWebFramework.services.Service;

@ClassNameBundle(bundle="resources/MetaWorkflowResources")
public class WorkflowUnitQueue extends WorkflowUnitQueue_Base {

    protected WorkflowUnitQueue() {
	super();
    }

    WorkflowUnitQueue(Unit unit, String name, List<AccountabilityType> accountabilityTypes) {
	this();
	init(unit, name, accountabilityTypes);
    }

    @Override
    protected void init(String name) {
	throw new RuntimeException("invalid init invocation");
    }

    protected void init(Unit unit, String name, List<AccountabilityType> accountabilityTypes) {
	super.init(name);

	setName(name);
	setUnit(unit);

	for (AccountabilityType accountabilityType : accountabilityTypes) {
	    addAccountabilityTypes(accountabilityType);
	}
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

	final Person person = user.getPerson();
	final Unit unit = getUnit();
	return unit.hasChildAccountabilityIncludingAncestry(getAccountabilityTypes(), person);
    }

    public static Set<WorkflowUnitQueue> getQueuesFor(Collection<Unit> units) {
	Set<WorkflowUnitQueue> queues = new HashSet<WorkflowUnitQueue>();
	for (Unit unit : units) {
	    queues.addAll(unit.getQueues());
	}
	return queues;
    }
    
    public static Collection<WorkflowUnitQueue> readAll() {
	List<WorkflowUnitQueue> queues = new ArrayList<WorkflowUnitQueue>();
	for (WorkflowQueue queue : WorkflowSystem.getInstance().getWorkflowQueues()) {
	    if (queue instanceof WorkflowUnitQueue) {
		queues.add((WorkflowUnitQueue) queue);
	    }
	}
	return queues;
    }

    @Override
    public boolean isConnectedToCurrentHost() {
	final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
	return virtualHost != null && getWorkflowSystem() == virtualHost.getWorkflowSystem();
    }

    public Collection<Person> getPersons() {
	return getUnit().getChildPersons(getAccountabilityTypes());
    }
}
