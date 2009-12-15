package module.workflow.presentationTier.taglib;

import javax.servlet.jsp.JspException;

public class IsActiveActivityTag extends WorkflowBodyTag {

    @Override
    public int doStartTag() throws JspException {
	return getActivity().isActive(getWorkflowProcess()) ? EVAL_BODY_INCLUDE : SKIP_BODY;
    }
}
