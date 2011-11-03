package module.workflow.activities;

import module.workflow.domain.WorkflowProcess;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class ReleaseProcess<T extends WorkflowProcess> extends WorkflowActivity<T, ActivityInformation<T>> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(T process, User user) {
	return process.isTicketSupportAvailable() && process.getCurrentOwner() != null && process.getCurrentOwner() == user;
    }

    @Override
    protected void process(ActivityInformation<T> information) {
	information.getProcess().releaseProcess();
    }

    @Override
    public boolean isUserAwarenessNeeded(T process, User user) {
	//joantune: if we own it, that's because we should do something with it, otherwise
	//we should release it
	return true;
    }

    @Override
    public boolean isVisible() {
	return false;
    }

}
