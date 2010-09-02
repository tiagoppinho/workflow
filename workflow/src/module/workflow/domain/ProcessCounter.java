package module.workflow.domain;

import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;

public class ProcessCounter {

    private final Class clazz;

    public ProcessCounter(final Class clazz) {
	this.clazz = clazz;
    }

    public Class getProcessClass() {
	return clazz;
    }

    public Class getProcessClassForForwarding() {
	return getProcessClass();
    }

    public int getCount() {
	int result = 0;
	final User requestingUser = UserView.getCurrentUser();
	for (final WorkflowProcess process : WorkflowSystem.getInstance().getProcesses()) {
	    if (shouldCountProcess(process, requestingUser)) {
		result++;
	    }
	}
        return result;
    }

    protected boolean shouldCountProcess(final WorkflowProcess process, final User requestingUser) {
	return clazz.isAssignableFrom(process.getClass())
		&& process.isAccessible(requestingUser)
		&& process.hasAnyAvailableActivity(requestingUser, true);
    }

}
