package module.workflow.activities;

import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowQueue;

public class ChangeQueueInformation<T extends WorkflowProcess> extends ActivityInformation<T> {

    WorkflowQueue queue;

    public ChangeQueueInformation(T process, ChangeQueue activity) {
	super(process, activity);
	setQueue(null);
    }

    public WorkflowQueue getQueue() {
	return queue;
    }

    public void setQueue(WorkflowQueue queue) {
	this.queue = queue;
    }

    @Override
    public boolean hasAllneededInfo() {
	return getQueue() != null;
    }
}
