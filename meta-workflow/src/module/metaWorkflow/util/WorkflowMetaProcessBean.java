package module.metaWorkflow.util;

import java.io.Serializable;

import module.metaWorkflow.domain.WorkflowMetaType;
import module.metaWorkflow.domain.WorkflowQueue;
import myorg.domain.User;
import pt.ist.fenixWebFramework.util.DomainReference;

public class WorkflowMetaProcessBean implements Serializable {

    String instanceDescription;
    String subject;

    DomainReference<WorkflowQueue> queue;
    DomainReference<User> requestor;

    public WorkflowMetaProcessBean() {
	setQueue(null);
	setRequestor(null);
    }

    public String getSubject() {
	return subject;
    }

    public void setSubject(String subject) {
	this.subject = subject;
    }

    public String getInstanceDescription() {
	return instanceDescription;
    }

    public void setInstanceDescription(String instanceDescription) {
	this.instanceDescription = instanceDescription;
    }

    public WorkflowQueue getQueue() {
	return queue.getObject();
    }

    public void setQueue(WorkflowQueue queue) {
	this.queue = new DomainReference<WorkflowQueue>(queue);
    }

    public User getRequestor() {
	return requestor.getObject();
    }

    public void setRequestor(User user) {
	this.requestor = new DomainReference<User>(user);
    }
}
