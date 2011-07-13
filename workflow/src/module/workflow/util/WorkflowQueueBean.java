package module.workflow.util;

import java.io.Serializable;

import module.workflow.domain.WorkflowQueue;

public class WorkflowQueueBean implements Serializable {

    private String name;
    private Class<? extends WorkflowQueue> queueType;
    private WorkflowQueue queue;

    public WorkflowQueueBean(WorkflowQueue queue) {
	setQueue(queue);
    }

    public WorkflowQueueBean() {
	setQueue(null);
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    //TODO probably put this method abstract and override it on its subclasses
    public Class<? extends WorkflowQueue> getQueueType() {
	return queueType;
    }

    public void setQueueType(Class<? extends WorkflowQueue> queueType) {
	this.queueType = queueType;
    }

    public WorkflowQueue getQueue() {
	return queue;
    }

    public void setQueue(WorkflowQueue queue) {
	this.queue = queue;
	if (queue != null) {
	    setName(queue.getName());
	    fillQueueFields(queue);
	}
    }

    protected void fillQueueFields(WorkflowQueue queue) {

    }
}
