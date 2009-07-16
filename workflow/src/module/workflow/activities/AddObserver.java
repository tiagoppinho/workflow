package module.workflow.activities;

import module.workflow.domain.WorkflowProcess;
import myorg.domain.User;

public class AddObserver<T extends WorkflowProcess> extends WorkflowActivity<T, UserInformation<T>> {

    @Override
    public boolean isActive(T process, User user) {
	return process.isObserverSupportAvailable() && !process.isUserObserver(user)
		&& (process.getCurrentOwner() == null || process.getCurrentOwner() == user);
    }

    @Override
    protected void process(UserInformation<T> information) {
	information.getProcess().addObservers(information.getUser());
    }

    public boolean isUserAwarenessNeeded(T process, User user) {
	return false;
    }

    @Override
    public boolean isVisible() {
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
}
