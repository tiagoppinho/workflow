package module.workflow.presentationTier.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class ActivityLinkParameterTag extends BodyTagSupport {

    private String parameter;
    private String value;

    public String getParameter() {
	return parameter;
    }

    public void setParameter(String parameter) {
	this.parameter = parameter;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    @Override
    public int doEndTag() throws JspException {
	ActivitivyLinkTag tag = (ActivitivyLinkTag) findAncestorWithClass(this, ActivitivyLinkTag.class);
	tag.setParameter(getParameter(), getValue());
	return super.doEndTag();
    }
}
