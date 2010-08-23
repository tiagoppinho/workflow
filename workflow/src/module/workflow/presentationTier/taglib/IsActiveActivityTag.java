package module.workflow.presentationTier.taglib;

import javax.servlet.jsp.JspException;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;

public class IsActiveActivityTag extends WorkflowBodyTag {

    @Override
    public int doStartTag() throws JspException {
	WorkflowActivity<WorkflowProcess, ActivityInformation<WorkflowProcess>> activity = getActivity();
	return activity != null && activity.isActive(getWorkflowProcess()) ? EVAL_BODY_INCLUDE : SKIP_BODY;
    }
}
