package module.metaWorkflow.util;

import java.io.Serializable;

import module.workflow.domain.WorkflowQueue;
import myorg.domain.User;

public class WorkflowMetaProcessBean implements Serializable {

    String instanceDescription;
    String subject;

    WorkflowQueue queue;
    User requestor;

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
	return queue;
    }

    public void setQueue(WorkflowQueue queue) {
	this.queue = queue;
    }

    public User getRequestor() {
	return requestor;
    }

    public void setRequestor(User user) {
	this.requestor = user;
    }
}
