package module.workflow.presentationTier.taglib;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;

public class ActivitivyLinkTag extends WorkflowBodyTag {

    private String linkName;

    private String id;

    private String paramName0;
    private String paramValue0;

    private String paramName1;
    private String paramValue1;

    private String paramName2;
    private String paramValue2;

    private String paramName3;
    private String paramValue3;

    private Map<String, String> parameterMap = new HashMap<String, String>();

    private StringBuilder content = new StringBuilder();

    @Override
    public String getId() {
	return id;
    }

    @Override
    public void setId(String id) {
	this.id = id;
    }

    public String getLinkName() {
	return linkName;
    }

    public void setLinkName(String linkName) {
	this.linkName = linkName;
    }

    public String getParamName0() {
	return paramName0;
    }

    public void setParamName0(String paramName0) {
	this.paramName0 = paramName0;
    }

    public String getParamValue0() {
	return paramValue0;
    }

    public void setParamValue0(String paramValue0) {
	this.paramValue0 = paramValue0;
    }

    public String getParamName1() {
	return paramName1;
    }

    public void setParamName1(String paramName1) {
	this.paramName1 = paramName1;
    }

    public String getParamValue1() {
	return paramValue1;
    }

    public void setParamValue1(String paramValue1) {
	this.paramValue1 = paramValue1;
    }

    public String getParamName2() {
	return paramName2;
    }

    public void setParamName2(String paramName2) {
	this.paramName2 = paramName2;
    }

    public String getParamValue2() {
	return paramValue2;
    }

    public void setParamValue2(String paramValue2) {
	this.paramValue2 = paramValue2;
    }

    public String getParamName3() {
	return paramName3;
    }

    public void setParamName3(String paramName3) {
	this.paramName3 = paramName3;
    }

    public String getParamValue3() {
	return paramValue3;
    }

    public void setParamValue3(String paramValue3) {
	this.paramValue3 = paramValue3;
    }

    public void setParameter(String parameterName, String value) {
	parameterMap.put(parameterName, value);
    }

    protected void write(final String text) throws IOException {
	pageContext.getOut().write(text);
    }

    protected String getContextPath() {
	final HttpServletRequest httpServletRequest = (HttpServletRequest) pageContext.getRequest();
	return httpServletRequest.getContextPath();
    }

    protected void generateParameterMap() {
	if (paramName0 != null) {
	    setParameter(paramName0, paramValue0);
	}
	if (paramName1 != null) {
	    setParameter(paramName1, paramValue1);
	}
	if (paramName2 != null) {
	    setParameter(paramName2, paramValue2);
	}
	if (paramName3 != null) {
	    setParameter(paramName3, paramValue3);
	}
    }

    @Override
    public int doStartTag() throws JspException {
	generateParameterMap();
	WorkflowProcess process = getWorkflowProcess();
	WorkflowActivity<WorkflowProcess, ActivityInformation<WorkflowProcess>> activity = getActivity();
	if (activity.isActive(process)) {
	    try {
		pageContext.getOut().write("<a ");
		if (getId() != null) {
		    pageContext.getOut().write("id=\"");
		    pageContext.getOut().write(getId());
		    pageContext.getOut().write("\" ");
		}
		pageContext.getOut().write("href=\"");
		pageContext.getOut().write(getContextPath());
		pageContext.getOut().write("/workflowProcessManagement.do?method=actionLink&activity=");
		pageContext.getOut().write(getActivityName());
		pageContext.getOut().write("&processId=");
		pageContext.getOut().write(process.getExternalId());
		pageContext.getOut().write("&parameters=");
		pageContext.getOut().write(getParameters());
		pageContext.getOut().write(getParameterString());
		pageContext.getOut().write("\">");

		if (activity.isConfirmationNeeded()) {
		    pageContext.getOut().write(
			    "<script type=\"text/javascript\">linkConfirmationHook('" + getId() + "', '"
				    + activity.getLocalizedConfirmationMessage() + "','" + activity.getLocalizedName()
				    + "');</script>");

		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    return EVAL_BODY_BUFFERED;
	}

	return SKIP_BODY;
    }

    /*
     * This hack is needed so even if we close the tag on the line below the
     * content no white spaces are added to the output creating a nasty link
     * underline on white spaces.
     */
    @Override
    public int doAfterBody() throws JspException {
	if (bodyContent != null) {
	    String value = bodyContent.getString().trim();
	    if (value.length() > 0)
		content.append(value);
	}
	return (SKIP_BODY);
    }

    @Override
    public int doEndTag() throws JspException {
	WorkflowProcess process = getWorkflowProcess();
	WorkflowActivity<WorkflowProcess, ActivityInformation<WorkflowProcess>> activity = process.getActivity(getActivityName());
	if (activity.isActive(process)) {
	    try {
		content.trimToSize();
		content.append("</a>");
		pageContext.getOut().write(content.toString());
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	release();
	return super.doAfterBody();

    }

    private String getParameters() {
	StringBuffer buffer = new StringBuffer("");
	Iterator<String> iterator = parameterMap.keySet().iterator();
	while (iterator.hasNext()) {
	    buffer.append(iterator.next());
	    if (iterator.hasNext()) {
		buffer.append(",");
	    }
	}
	return buffer.toString();
    }

    private String getParameterString() {
	StringBuffer buffer = new StringBuffer("");
	Iterator<String> iterator = parameterMap.keySet().iterator();
	while (iterator.hasNext()) {
	    String key = iterator.next();
	    buffer.append("&");
	    buffer.append(key);
	    buffer.append("=");
	    buffer.append(parameterMap.get(key));
	}
	return buffer.toString();
    }

    @Override
    public void release() {
	parameterMap.clear();
	content = new StringBuilder();
	paramName0 = null;
	paramValue0 = null;
	paramName1 = null;
	paramValue1 = null;
	paramName2 = null;
	paramValue2 = null;
	paramName3 = null;
	paramValue3 = null;
	linkName = null;
	id = null;
	super.release();
    }
}
