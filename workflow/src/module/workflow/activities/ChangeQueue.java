package module.workflow.activities;

import java.util.Collection;

import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowQueue;
import myorg.domain.User;
import myorg.domain.exceptions.DomainException;
import myorg.util.BundleUtil;

public class ChangeQueue<T extends WorkflowProcess> extends WorkflowActivity<T, ChangeQueueInformation<T>> {

    @Override
    public boolean isActive(T process, User user) {
	return process.isAccessible(user) && !process.isUserObserver(user);
    }

    @Override
    protected void process(ChangeQueueInformation<T> activityInformation) {
	WorkflowProcess process = activityInformation.getProcess();
	Collection<WorkflowQueue> queuesToAdd = activityInformation.getQueuesToAdd();
	Collection<WorkflowQueue> queuesToRemove = activityInformation.getQueuesToRemove();

	for (WorkflowQueue queueToRemove : queuesToRemove) {
	    if (!queueToRemove.isCurrentUserAbleToAccessQueue()) {
		throw new DomainException(BundleUtil.getFormattedStringFromResourceBundle("resources/MetaWorkflowResources",
			"error.user.cannotRemove.from.queue", queueToRemove.getName()));
	    }
	    process.removeCurrentQueues(queueToRemove);
	}

	for (WorkflowQueue queueToAdd : queuesToAdd) {
	    process.addCurrentQueues(queueToAdd);
	}

	if (!process.hasAnyCurrentQueues()) {
	    throw new DomainException(BundleUtil.getFormattedStringFromResourceBundle("resources/MetaWorkflowResources",
		    "error.process.mustHave.atLeast.one.queue"));
	}
    }

    @Override
    public String getUsedBundle() {
	return "resources/WorkflowResources";
    }

    @Override
    public ChangeQueueInformation<T> getActivityInformation(T process) {
	return new ChangeQueueInformation<T>(process, this);
    }

}
