package module.workflow.presentationTier.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import module.workflow.domain.WorkflowProcess;
import myorg.domain.User;

public class CommentBean implements Serializable {

    private WorkflowProcess process;
    private String comment;
    private List<UserNotificationBean> peopleToNotify;

    public CommentBean(WorkflowProcess process) {
	this.process = process;
	this.peopleToNotify = Collections.emptyList();
    }

    public WorkflowProcess getProcess() {
	return process;
    }

    public void setProcess(WorkflowProcess process) {
	this.process = process;
    }

    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }

    public List<User> getUsersToNotify() {
	List<User> users = new ArrayList<User>();
	for (UserNotificationBean bean : this.peopleToNotify) {
	    if (bean.isAbleToNotify()) {
		users.add(bean.getUser());
	    }
	}
	return users;
    }

    public List<UserNotificationBean> getPeopleToNotify() {
	return this.peopleToNotify;
    }

    public void setPeopleToNotify(List<UserNotificationBean> peopleToNotify) {
	this.peopleToNotify = peopleToNotify;
    }
}
