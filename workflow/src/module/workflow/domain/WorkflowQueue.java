package module.workflow.domain;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.Predicate;

import module.workflow.presentationTier.WorkflowQueueLayoutContext;
import module.workflow.util.WorkflowQueueBean;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.IllegalWriteException;

public abstract class WorkflowQueue extends WorkflowQueue_Base {

    public WorkflowQueue() {
	super();
	super.setWorkflowSystem(WorkflowSystem.getInstance());
	super.setOjbConcreteClass(this.getClass().getName());
    }

    public WorkflowQueue(String name) {
	this();
	setName(name);
    }

    public boolean isCurrentUserAbleToAccessQueue() {
	return isUserAbleToAccessQueue(UserView.getCurrentUser());
    }

    public abstract boolean isUserAbleToAccessQueue(User user);

    private List<WorkflowProcess> filterProcesses(boolean active) {
	List<WorkflowProcess> processes = new ArrayList<WorkflowProcess>();
	for (WorkflowProcess process : getProcess()) {
	    if (process.isActive() == active) {
		processes.add(process);
	    }
	}
	return processes;
    }

    public List<WorkflowProcess> getActiveMetaProcesses() {
	return filterProcesses(true);
    }

    public List<WorkflowProcess> getNotActiveMetaProcesses() {
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
	    queue = queueClass.getConstructor(new Class[] { String.class, }).newInstance(new Object[] { bean.getName() });

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
	for (WorkflowQueue queue : WorkflowSystem.getInstance().getWorkflowQueues()) {
	    if (queue.isUserAbleToAccessQueue(user)) {
		queues.add(queue);
	    }
	}
	return queues;

    }

    public WorkflowQueueLayoutContext getDefaultContext() {
	return WorkflowQueueLayoutContext.getDefaultLayout(this);
    }

    public static Set<WorkflowQueue> getQueues(Predicate predicate) {
	Set<WorkflowQueue> filteredQueues = new HashSet<WorkflowQueue>();
	for (WorkflowQueue queue : WorkflowSystem.getInstance().getWorkflowQueues()) {
	    if (predicate.evaluate(queue)) {
		filteredQueues.add(queue);
	    }
	}
	return filteredQueues;
    }
}
