package module.workflow.presentationTier.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;

public class ActivityNameTag extends BodyTagSupport {

    private String processName;
    private String activityName;
    private String scope;

    public String getScope() {
	return scope;
    }

    public void setScope(String scope) {
	this.scope = scope;
    }

    public String getProcessName() {
	return processName;
    }

    public void setProcessName(String processName) {
	this.processName = processName;
    }

    public String getActivityName() {
	return activityName;
    }

    public void setActivityName(String activityName) {
	this.activityName = activityName;
    }

    @Override
    public int doEndTag() throws JspException {
	WorkflowProcess process = getWorkflowProcess();
	WorkflowActivity<WorkflowProcess, ActivityInformation<WorkflowProcess>> activity = process.getActivity(getActivityName());
	try {
	    pageContext.getOut().write(activity.getLocalizedName());
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return super.doEndTag();
    }

    public WorkflowProcess getWorkflowProcess() {
	return (WorkflowProcess) ActivitivyLinkTag.getObject(getProcessName(), pageContext, getScope());
    }

}
