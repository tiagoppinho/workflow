package module.workflow.activities;

import java.util.ArrayList;
import java.util.List;

import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowQueue;
import myorg.applicationTier.Authenticate.UserView;

public class ChangeQueueInformation<T extends WorkflowProcess> extends ActivityInformation<T> {

    private List<WorkflowQueue> queuesToRemove;
    private List<WorkflowQueue> queuesToAdd;

    public ChangeQueueInformation(T process, ChangeQueue<T> activity) {
	super(process, activity);
	queuesToRemove = new ArrayList<WorkflowQueue>(process.getCurrentQueuesSet());
	queuesToRemove.retainAll(UserView.getCurrentUser().getQueues());
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput();
    }

    public void setQueuesToRemove(List<WorkflowQueue> queuesToRemove) {
	this.queuesToRemove = queuesToRemove;
    }

    public List<WorkflowQueue> getQueuesToRemove() {
	return queuesToRemove;
    }

    public void setQueuesToAdd(List<WorkflowQueue> queuesToAdd) {
	this.queuesToAdd = queuesToAdd;
    }

    public List<WorkflowQueue> getQueuesToAdd() {
	return queuesToAdd;
    }
}
