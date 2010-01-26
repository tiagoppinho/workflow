package module.workflow.activities;

import module.workflow.domain.WorkflowProcess;
import myorg.domain.User;

public class UserInformation<T extends WorkflowProcess> extends ActivityInformation<T> {

    private static final long serialVersionUID = 1L;
    private User user;

    public UserInformation(T process, WorkflowActivity<T, UserInformation<T>> activity) {
	super(process, activity);
	setUser(null);
    }

    public void setUser(User user) {
	this.user = user;
    }

    public User getUser() {
	return user;
    }

    @Override
    public boolean hasAllneededInfo() {
	return getUser() != null;
    }
}
