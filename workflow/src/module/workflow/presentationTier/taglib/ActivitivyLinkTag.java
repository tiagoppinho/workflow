package module.workflow.presentationTier.taglib;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;

public class ActivitivyLinkTag extends BodyTagSupport {

    String processName;
    String activityName;
    String linkName;
    String scope;
    String id;

    @Override
    public String getId() {
	return id;
    }

    @Override
    public void setId(String id) {
	this.id = id;
    }

    Map<String, String> parameterMap = new HashMap<String, String>();

    public String getLinkName() {
	return linkName;
    }

    public void setLinkName(String linkName) {
	this.linkName = linkName;
    }

    @Override
    public int doStartTag() throws JspException {
	return EVAL_BODY_INCLUDE;
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

    @Override
    public int doEndTag() throws JspException {
	WorkflowProcess process = getWorkflowProcess();
	WorkflowActivity<WorkflowProcess, ActivityInformation<WorkflowProcess>> activity = process.getActivity(getActivityName());
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
		if (getLinkName() != null) {
		    pageContext.getOut().write(getLinkName());
		} else {
		    pageContext.getOut().write(activity.getLocalizedName());
		}
		pageContext.getOut().write("</a>");
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

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

    public String getScope() {
	return (this.scope);
    }

    public void setScope(String scope) {
	this.scope = scope;
    }

    public static int getPageScope(final String scope) {
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

    public static Object getObject(final String name, final PageContext pageContext, final String scope) {
	final int pageScope = getPageScope(scope);
	return pageScope == -1 ? pageContext.getAttribute(name) : pageContext.getAttribute(name, pageScope);
    }

    public WorkflowProcess getWorkflowProcess() {
	return (WorkflowProcess) getObject(getProcessName(), pageContext, getScope());
    }
}
