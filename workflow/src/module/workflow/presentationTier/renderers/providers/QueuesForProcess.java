package module.workflow.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowQueue;
import module.workflow.presentationTier.actions.CommentBean;
import module.workflow.presentationTier.actions.QueueNotificationBean;
import module.workflow.presentationTier.actions.UserNotificationBean;
import myorg.domain.User;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * Lists the queues where the process has been and where it is, so that one can
 * notify the users of the queues directly via the commentary interface
 * 
 * @author João Antunes (joao.antunes@tagus.ist.utl.pt) - 7 de Nov de 2011
 * 
 * 
 */
public class QueuesForProcess implements DataProvider {

    @Override
    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	CommentBean bean = (CommentBean) source;
	Set<QueueNotificationBean> availableQueuesToNotify = new HashSet<QueueNotificationBean>();
	WorkflowProcess process = bean.getProcess();

	for (WorkflowQueue queue : process.getQueueHistory()) {
	    availableQueuesToNotify.add(new QueueNotificationBean(queue, process));
	}
	for (WorkflowQueue currentQueue : process.getCurrentQueues()) {
	    availableQueuesToNotify.add(new QueueNotificationBean(currentQueue, process));
	}

	//	return new ArrayList<UserNotificationBean>(availablePeopleToNotify);
	return availableQueuesToNotify;
    }

    private List<UserNotificationBean> getWorkers(WorkflowProcess process) {
	List<UserNotificationBean> moreBeans = new ArrayList<UserNotificationBean>();
	for (User user : process.getProcessWorkers()) {
	    moreBeans.add(new UserNotificationBean(user, process));
	}
	return moreBeans;
    }
}
