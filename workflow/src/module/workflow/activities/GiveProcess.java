package module.workflow.activities;

import module.workflow.domain.WorkflowProcess;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class GiveProcess<T extends WorkflowProcess> extends WorkflowActivity<T, UserInformation<T>> {

    public static abstract class NotifyUser {

	public abstract void notifyUser(final User user, final WorkflowProcess process);

    }

    private NotifyUser notifyUser;

    public GiveProcess(final NotifyUser notifyUser) {
	this.notifyUser = notifyUser;
    }

    public GiveProcess() {
	this(null);
    }

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(WorkflowProcess process, User user) {
	return process.isTicketSupportAvailable() && !process.isUserObserver(user)
		&& (process.getCurrentOwner() == null || process.getCurrentOwner() == user);
    }

    @Override
    protected void process(UserInformation<T> information) {
	final User user = information.getUser();
	information.getProcess().giveProcess(user);
	if (notifyUser != null) {
	    notifyUser.notifyUser(user, information.getProcess());
	}
    }

    public boolean isUserAwarenessNeeded(T process, User user) {
	return false;
    }

    @Override
    public ActivityInformation<T> getActivityInformation(T process) {
	return new UserInformation<T>(process, this);
    }

    @Override
    protected String[] getArgumentsDescription(UserInformation<T> activityInformation) {
	return new String[] { activityInformation.getUser().getPresentationName() };
    }

    @Override
    public boolean isVisible() {
	return false;
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
        return false;
    }

}
