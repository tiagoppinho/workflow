package module.metaWorkflow.domain;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import module.metaWorkflow.presentationTier.WorkflowQueueLayoutContext;
import module.metaWorkflow.util.WorkflowQueueBean;
import module.organization.domain.Unit;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.MyOrg;
import myorg.domain.User;
import myorg.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.IllegalWriteException;

public abstract class WorkflowQueue extends WorkflowQueue_Base {

    public WorkflowQueue() {
	super();
	setMyOrg(MyOrg.getInstance());
	setOjbConcreteClass(this.getClass().getName());
    }

    public WorkflowQueue(String name, WorkflowMetaType metaType) {
	this();
	init(metaType, name);
    }

    protected void init(WorkflowMetaType metaType, String name) {
	setName(name);
	setMetaType(metaType);
    }

    public boolean isCurrentUserAbleToAccessQueue() {
	return isUserAbleToAccessQueue(UserView.getCurrentUser());
    }

    public abstract boolean isUserAbleToAccessQueue(User user);

    @Override
    public void addMetaProcess(WorkflowMetaProcess metaProcess) {
	if (metaProcess.getMetaType() != getMetaType()) {
	    throw new DomainException("error.queue.addingProcessWithInvalidMetaType");
	}
	super.addMetaProcess(metaProcess);
    }

    private List<WorkflowMetaProcess> filterProcesses(boolean active) {
	List<WorkflowMetaProcess> processes = new ArrayList<WorkflowMetaProcess>();
	for (WorkflowMetaProcess process : getMetaProcess()) {
	    if (process.isOpen() == active) {
		processes.add(process);
	    }
	}
	return processes;
    }

    public List<WorkflowMetaProcess> getActiveMetaProcesses() {
	return filterProcesses(true);
    }

    public List<WorkflowMetaProcess> getNotActiveMetaProcesses() {
	return filterProcesses(false);
    }

    public int getActiveProcessCount() {
	return getActiveMetaProcesses().size();
    }

    public int getNotActiveProcessCount() {
	return getNotActiveMetaProcesses().size();
    }

    protected void fillNonDefaultFields(WorkflowQueueBean bean) {
	// do nothing
    }

    public void edit(WorkflowQueueBean bean) {
	// do nothing
    }

    @Service
    public static WorkflowQueue createQueue(Class<? extends WorkflowQueue> queueType, WorkflowQueueBean bean) {
	WorkflowQueue queue = null;
	try {
	    Class<? extends WorkflowQueue> queueClass = (Class<? extends WorkflowQueue>) Class.forName(queueType.getName());
	    queue = queueClass.getConstructor(new Class[] { String.class, WorkflowMetaType.class }).newInstance(
		    new Object[] { bean.getName(), bean.getMetaType() });

	} catch (InvocationTargetException e) {
	    if (e.getCause() instanceof IllegalWriteException) {
		throw new IllegalWriteException();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
	queue.fillNonDefaultFields(bean);
	return queue;
    }

    public static Set<WorkflowQueue> getQueuesForUser(User user) {
	Set<WorkflowQueue> queues = new HashSet<WorkflowQueue>();

	if (user != null) {
	    queues.addAll(user.getQueues());

	    for (WorkflowUnitQueue queue : getQueuesFor(user.getPerson().getParentUnits())) {
		if (queue.isUserAbleToAccessQueue(user)) {
		    queues.add(queue);
		}
	    }
	}
	return queues;

    }

    public WorkflowQueueLayoutContext getDefaultContext() {
	return WorkflowQueueLayoutContext.getDefaultLayout(this);
    }

    public static Set<WorkflowUnitQueue> getQueuesFor(Collection<Unit> units) {
	Set<WorkflowUnitQueue> queues = new HashSet<WorkflowUnitQueue>();
	for (Unit unit : units) {
	    queues.addAll(unit.getQueues());
	}
	return queues;
    }
}
