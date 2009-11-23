package module.workflow.presentationTier.actions;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import module.workflow.domain.WorkflowProcess;
import myorg.domain.User;

public class CommentBean implements Serializable {

    private WorkflowProcess process;
    private String comment;
    private List<User> peopleToNotify;

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

    public List<User> getPeopleToNotify() {
	return peopleToNotify;
    }

    public void setPeopleToNotify(List<User> peopleToNotify) {
	this.peopleToNotify = peopleToNotify;
    }
}
