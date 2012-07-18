/*
 * @(#)WorkflowWidgetActions.java
 *
 * Copyright 2011 Instituto Superior Tecnico
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
package module.workflow.widgets.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.organization.domain.Person;
import module.workflow.domain.WorkflowProcess;
import module.workflow.widgets.UnreadCommentsWidget;
import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.presentationTier.actions.ContextBaseAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * 
 * @author João Antunes
 * 
 */
@Mapping(path = "/workflowWidgetActions")
public class WorkflowWidgetActions extends ContextBaseAction {

    /**
     * Method used by the UnreadCommentsWidget widget
     * {@link UnreadCommentsWidget}.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward viewListUnreadComments(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	//get the parameter with the class to use
	String processClass = request.getParameter("processClass");

	request.setAttribute("processClass", processClass);

	//get all of the unread comments
	Person loggedPerson = UserView.getCurrentUser().getPerson();
	List<WorkflowProcess> processesWithUnreadComments = UnreadCommentsWidget.getProcessesWithUnreadComments(null,
		loggedPerson, processClass);
	request.setAttribute("processesWithUnreadComments", processesWithUnreadComments);

	//	if (anchor != null) {
	//	    return forward(request, "/module/workflow/widgets/ListOfUnreadComments.jsp#" + anchor);
	//	}
	return forward(request, "/module/workflow/widgets/ListOfUnreadComments.jsp");

    }

    public ActionForward markCommentsAsRead(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	//get the parameter anchor and processId
	String anchor = request.getParameter("anchor");
	WorkflowProcess process = (WorkflowProcess) getDomainObject(request.getParameter("processId"));

	//validate the access to the process
	if (process != null && !process.isAccessibleToCurrentUser())
	    //TODO throw an error message if the user has no permission to set the comments as read
	    return viewListUnreadComments(mapping, form, request, response);

	//mark the comments as read
	process.markCommentsAsReadForUser(UserView.getCurrentUser());
	return viewListUnreadComments(mapping, form, request, response);
    }
}
