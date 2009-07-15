package module.workflow.activities;

import module.workflow.domain.WorkflowProcess;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class StealProcess<T extends WorkflowProcess> extends WorkflowActivity<T, ActivityInformation<T>> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(T process, User user) {
	return process.isTicketSupportAvailable() && !process.isUserObserver(user) && process.getCurrentOwner() != null && process.getCurrentOwner() != user;
    }

    @Override
    protected void process(ActivityInformation<T> information) {
	information.getProcess().stealProcess();
    }

    @Override
    public boolean isUserAwarenessNeeded(T process, User user) {
	return false;
    }

}
