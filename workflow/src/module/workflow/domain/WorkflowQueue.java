package module.workflow.domain;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import module.workflow.presentationTier.WorkflowQueueLayoutContext;
import module.workflow.util.WorkflowQueueBean;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;

import org.apache.commons.collections.Predicate;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.IllegalWriteException;

public abstract class WorkflowQueue extends WorkflowQueue_Base {

    public static final Comparator<WorkflowQueue> COMPARATOR_BY_NAME = new Comparator<WorkflowQueue>() {

	@Override
	public int compare(final WorkflowQueue o1, final WorkflowQueue o2) {
	    int n = o1.getName().compareTo(o2.getName());
	    return n == 0 ? o1.getExternalId().compareTo(o2.getExternalId()) : n;
	}

    };

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
	List<WorkflowProcess> filteredProcesses = new ArrayList<WorkflowProcess>();
	for (WorkflowProcess process : getProcesses()) {
	    if (process.isActive() == active) {
		filteredProcesses.add(process);
	    }
	}
	return filteredProcesses;
    }

    public List<WorkflowProcess> getActiveProcesses() {
	return filterProcesses(true);
    }

    public List<WorkflowProcess> getNotActiveProcesses() {
	return filterProcesses(false);
    }

    public int getActiveProcessCount() {
	return getActiveProcesses().size();
    }

    public int getNotActiveProcessCount() {
	return getNotActiveProcesses().size();
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
