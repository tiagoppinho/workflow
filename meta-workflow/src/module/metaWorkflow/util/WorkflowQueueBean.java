package module.metaWorkflow.util;

import java.io.Serializable;

import module.metaWorkflow.domain.WorkflowMetaType;
import module.metaWorkflow.domain.WorkflowQueue;

import pt.ist.fenixWebFramework.util.DomainReference;

public class WorkflowQueueBean implements Serializable {

    private String name;
    private DomainReference<WorkflowMetaType> metaType;
    private Class<? extends WorkflowQueue> queueType;
    private DomainReference<WorkflowQueue> queue;

    public WorkflowQueueBean(WorkflowQueue queue) {
	setQueue(queue);
    }

    public WorkflowQueueBean() {
	setMetaType(null);
	setQueue(null);
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public WorkflowMetaType getMetaType() {
	return metaType.getObject();
    }

    public void setMetaType(WorkflowMetaType metaType) {
	this.metaType = new DomainReference<WorkflowMetaType>(metaType);
    }

    public Class<? extends WorkflowQueue> getQueueType() {
	return queueType;
    }

    public void setQueueType(Class<? extends WorkflowQueue> queueType) {
	this.queueType = queueType;
    }

    public WorkflowQueue getQueue() {
	return queue.getObject();
    }

    public void setQueue(WorkflowQueue queue) {
	this.queue = new DomainReference<WorkflowQueue>(queue);
	if (queue != null) {
	    setMetaType(queue.getMetaType());
	    setName(queue.getName());
	    fillQueueFields(queue);
	}
    }

    protected void fillQueueFields(WorkflowQueue queue) {

    }

}
