package module.workflow.presentationTier.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;

public class ActivityNameTag extends WorkflowBodyTag {

    @Override
    public int doEndTag() throws JspException {
	try {
	    pageContext.getOut().write(getActivity().getLocalizedName());
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return super.doEndTag();
    }

}
