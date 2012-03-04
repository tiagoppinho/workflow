/*
 * @(#)WorkflowBodyTag.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Case Handleing Based Workflow Module.
 *
 *   The Case Handleing Based Workflow Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Workflow Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Workflow Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.workflow.presentationTier.taglib;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;

/**
 * 
 * @author Paulo Abrantes
 * 
 */
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
