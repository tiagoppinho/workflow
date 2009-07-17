package module.metaWorkflow.util;

import java.io.Serializable;

import module.metaWorkflow.domain.WorkflowMetaType;
import module.metaWorkflow.domain.WorkflowQueue;

import pt.ist.fenixWebFramework.util.DomainReference;

public class WorkflowQueueBean implements Serializable {

    String name;
    DomainReference<WorkflowMetaType> metaType;
    Class<? extends WorkflowQueue> queueType;

    public WorkflowQueueBean() {
	setMetaType(null);
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

}
