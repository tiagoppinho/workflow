package module.workflow.presentationTier.actions;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import module.workflow.domain.WorkflowProcess;
import myorg.domain.User;

public class CommentBean implements Serializable {

    private WorkflowProcess process;
    private String comment;
    private List<UserNotificationBean> peopleToNotify;
    private List<QueueNotificationBean> queuesToNotify;

    public CommentBean(WorkflowProcess process) {
	this.process = process;
	this.peopleToNotify = Collections.emptyList();
	this.queuesToNotify = Collections.emptyList();
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

    public Set<User> getUsersToNotify() {
	Set<User> users = new HashSet<User>();
	for (UserNotificationBean bean : this.peopleToNotify) {
	    if (bean.isAbleToNotify()) {
		users.add(bean.getUser());
	    }
	}
	for (QueueNotificationBean bean : this.getQueuesToNotify()) {
	    if (bean.isAbleToNotify()) {
		users.addAll(bean.getUsers());
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

    public List<QueueNotificationBean> getQueuesToNotify() {
	return queuesToNotify;
    }

    public void setQueuesToNotify(List<QueueNotificationBean> queuesToNotify) {
	this.queuesToNotify = queuesToNotify;
    }
}
