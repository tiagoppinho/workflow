package module.metaWorkflow.activities;

import module.metaWorkflow.domain.WorkflowMetaProcess;
import module.metaWorkflow.domain.WorkflowQueue;
import module.workflow.activities.ActivityInformation;
import pt.ist.fenixWebFramework.util.DomainReference;

public class ChangeQueueInformation extends ActivityInformation<WorkflowMetaProcess> {

    DomainReference<WorkflowQueue> queue;

    public ChangeQueueInformation(WorkflowMetaProcess process, ChangeQueue activity) {
	super(process, activity);
	setQueue(null);
    }

    public WorkflowQueue getQueue() {
	return queue.getObject();
    }

    public void setQueue(WorkflowQueue queue) {
	this.queue = new DomainReference<WorkflowQueue>(queue);
    }

    @Override
    public boolean hasAllneededInfo() {
	return getQueue() != null;
    }
}
