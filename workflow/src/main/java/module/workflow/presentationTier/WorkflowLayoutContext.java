/*
 * @(#)WorkflowLayoutContext.java
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
package module.workflow.presentationTier;

import module.workflow.domain.WorkflowProcess;
import pt.ist.bennu.core.presentationTier.LayoutContext;

/**
 * 
 * @author Anil Kassamali
 * @author Paulo Abrantes
 * 
 */
public class WorkflowLayoutContext extends LayoutContext {

    public final static String DEFAULT_BODY = "body.jsp";
    public final static String DEFAULT_SHORT_BODY = "shortBody.jsp";
    public final static String DEFAULT_HEAD = "header.jsp";

    protected String workflowBody;
    protected String workflowShortBody;
    protected String workflowHead;

    public WorkflowLayoutContext() {
	super();
    }

    public WorkflowLayoutContext(final String path) {
	super(path);
    }

    public String getWorkflowShortBody() {
	return workflowShortBody;
    }

    public void setWorkflowShortBody(String workflowShortBody) {
	this.workflowShortBody = workflowShortBody;
    }

    public String getWorkflowBody() {
	return workflowBody;
    }

    public void setWorkflowBody(String workflowBody) {
	this.workflowBody = workflowBody;
    }

    public String getWorkflowHead() {
	return workflowHead;
    }

    public void setWorkflowHead(String workflowHead) {
	this.workflowHead = workflowHead;
    }

    public static WorkflowLayoutContext getDefaultWorkflowLayoutContext(Class<? extends WorkflowProcess> processClass) {
	WorkflowLayoutContext context = new WorkflowLayoutContext();
	String folder = processClass.getName().replace(".", "/");
	context.setWorkflowBody("/" + folder + "/" + DEFAULT_BODY);
	context.setWorkflowHead("/" + folder + "/" + DEFAULT_HEAD);
	context.setWorkflowShortBody("/" + folder + "/" + DEFAULT_SHORT_BODY);

	return context;
    }
}
