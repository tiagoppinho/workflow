package module.workflow.activities;

import module.workflow.domain.WorkflowProcess;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class TakeProcess<T extends WorkflowProcess> extends WorkflowActivity<T, ActivityInformation<T>> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(T process, User user) {
	return process.isTicketSupportAvailable() && process.getCurrentOwner() == null;
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
