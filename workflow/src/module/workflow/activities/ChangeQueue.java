package module.workflow.activities;

import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowQueue;
import myorg.domain.User;

public class ChangeQueue<T extends WorkflowProcess> extends WorkflowActivity<T, ChangeQueueInformation<T>> {

    @Override
    public boolean isActive(T process, User user) {
	return process.isAccessible(user) && !process.isUserObserver(user);
    }

    @Override
    protected void process(ChangeQueueInformation activityInformation) {
	activityInformation.getProcess().setCurrentQueue(activityInformation.getQueue());
    }

    @Override
    public String getUsedBundle() {
	return "resources/WorkflowResources";
    }

    @Override
    public ChangeQueueInformation getActivityInformation(T process) {
	return new ChangeQueueInformation<T>(process, this);
    }

    @Override
    protected String[] getArgumentsDescription(ChangeQueueInformation activityInformation) {
	WorkflowQueue currentQueue = activityInformation.getProcess().getCurrentQueue();
	return new String[] { currentQueue != null ? currentQueue.getName() : "", activityInformation.getQueue().getName() };
    }

}
