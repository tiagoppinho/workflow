package module.workflow.activities;

import module.workflow.domain.WorkflowProcess;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class GiveProcess<T extends WorkflowProcess> extends WorkflowActivity<T, GiveProcessInformation<T>> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(WorkflowProcess process, User user) {
	return process.isTicketSupportAvailable() && (process.getCurrentOwner() == null || process.getCurrentOwner() == user);
    }

    @Override
    protected void process(GiveProcessInformation<T> information) {
	information.getProcess().giveProcess(information.getUser());
    }

    public boolean isUserAwarenessNeeded(T process, User user) {
	return false;
    }

    @Override
    public ActivityInformation<T> getActivityInformation(T process) {
	return new GiveProcessInformation<T>(process, this);
    }

    @Override
    protected String[] getArgumentsDescription(GiveProcessInformation<T> activityInformation) {
	return new String[] { activityInformation.getUser().getPresentationName() };
    }
}
