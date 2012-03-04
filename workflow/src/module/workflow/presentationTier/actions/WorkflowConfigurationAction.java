/*
 * @(#)WorkflowConfigurationAction.java
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
package module.workflow.presentationTier.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.workflow.domain.WorkflowSystem;
import myorg.domain.VirtualHost;
import myorg.presentationTier.actions.ContextBaseAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/workflowConfiguration")
/**
 * 
 * @author Luis Cruz
 * 
 */
public class WorkflowConfigurationAction extends ContextBaseAction {

    public ActionForward viewConfiguration(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	return forward(request, "/workflowConfiguration.jsp");
    }

    public ActionForward createNewSystem(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
	WorkflowSystem.createSystem(virtualHost);

	return viewConfiguration(mapping, form, request, response);
    }

    public ActionForward useSystem(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	final WorkflowSystem workflowSystem = getDomainObject(request, "systemId");
	final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
	workflowSystem.setForVirtualHost(virtualHost);

	return viewConfiguration(mapping, form, request, response);
    }

}
