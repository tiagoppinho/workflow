package module.workflow.presentationTier.taglib;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;

public abstract class WorkflowBodyTag extends BodyTagSupport {

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

    protected static int getPageScope(final String scope) {
	if (scope == null) {
	    return -1;
	} else if (scope.equalsIgnoreCase("page")) {
	    return PageContext.PAGE_SCOPE;
	} else if (scope.equalsIgnoreCase("request")) {
	    return PageContext.REQUEST_SCOPE;
	} else if (scope.equalsIgnoreCase("session")) {
	    return PageContext.SESSION_SCOPE;
	} else {
	    return -1;
	}
    }

    protected Object getObject(final String name, final PageContext pageContext, final String scope) {
	final int pageScope = getPageScope(scope);
	return pageScope == -1 ? pageContext.getAttribute(name) : pageContext.getAttribute(name, pageScope);
    }

    protected WorkflowProcess getWorkflowProcess() {
	return (WorkflowProcess) getObject(getProcessName(), pageContext, getScope());
    }

    protected WorkflowActivity<WorkflowProcess, ActivityInformation<WorkflowProcess>> getActivity() {
	WorkflowProcess process = getWorkflowProcess();
	WorkflowActivity<WorkflowProcess, ActivityInformation<WorkflowProcess>> activity = process.getActivity(getActivityName());
	return activity;
    }

    @Override
    public void release() {
	processName = null;
	activityName = null;
	super.release();
    }
}
