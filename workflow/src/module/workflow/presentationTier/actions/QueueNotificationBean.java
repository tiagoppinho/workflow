package module.workflow.presentationTier.actions;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import module.organization.domain.Person;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowQueue;
import module.workflow.presentationTier.renderers.providers.QueuesForProcess;
import myorg.domain.User;

/**
 * Bean used to wrap the queues for the comment system
 * 
 * @see QueuesForProcess
 * @see ProcessManagement
 * @see viewComments.jsp
 * @author João Antunes (joao.antunes@tagus.ist.utl.pt) - 7 de Nov de 2011
 * 
 * 
 */
public class QueueNotificationBean implements Serializable {

    private boolean ableToNotify;
    private final WorkflowQueue queue;

    public QueueNotificationBean(WorkflowQueue queue, WorkflowProcess process) {
	this.queue = queue;
	this.ableToNotify = process.isSystemAbleToNotifyQueue(queue);
    }

    public boolean isAbleToNotify() {
	return ableToNotify;
    }

    public void setAbleToNotify(boolean ableToNotify) {
	this.ableToNotify = ableToNotify;
    }

    public Set<User> getUsers() {
	HashSet<User> usersSetToReturn = new HashSet<User>();
	for (Person person : getQueue().getPersons()) {
	    usersSetToReturn.add(person.getUser());
	}
	return usersSetToReturn;
    }

    //    public void setUser(User user) {
    //	this.user = user;
    //    }

    @Override
    public int hashCode() {
	return getQueue().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof QueueNotificationBean && ((QueueNotificationBean) obj).getQueue() == getQueue();
    }

    public WorkflowQueue getQueue() {
	return queue;
    }
}
