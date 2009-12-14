package module.workflow.presentationTier.actions;

import java.io.Serializable;

import myorg.domain.User;
import module.workflow.domain.WorkflowProcess;

public class UserNotificationBean implements Serializable {

    private boolean ableToNotify;
    private User user;

    public UserNotificationBean(User user, WorkflowProcess process) {
	this.user = user;
	this.ableToNotify = process.isSystemAbleToNotifyUser(user);
    }

    public boolean isAbleToNotify() {
	return ableToNotify;
    }

    public void setAbleToNotify(boolean ableToNotify) {
	this.ableToNotify = ableToNotify;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    @Override
    public int hashCode() {
	return user.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof UserNotificationBean && ((UserNotificationBean) obj).user == user;
    }
}
