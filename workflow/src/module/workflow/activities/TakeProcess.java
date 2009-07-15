package module.workflow.activities;

import module.workflow.domain.WorkflowProcess;
import myorg.domain.User;

public class TakeProcess<T extends WorkflowProcess> extends WorkflowActivity<T, ActivityInformation<T>> {

    @Override
    public boolean isActive(T process, User user) {
	return process.isTicketSupportAvailable() && !process.isUserObserver(user) && process.getCurrentOwner() == null;
    }

    @Override
    protected void process(ActivityInformation<T> information) {
	information.getProcess().takeProcess();
    }

    @Override
    public boolean isUserAwarenessNeeded(T process, User user) {
	return false;
    }
}
