/*
 * @(#)MetaWorkflowInterfaceCreation.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Meta-Workflow Module.
 *
 *   The Meta-Workflow Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Meta-Workflow Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Meta-Workflow Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.metaWorkflow.presentationTier.actions;

/*
 * @(#)MetaWorkflowInterfaceCreation.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: João Figueiredo, Luis Cruz, Paulo Abrantes, Susana Fernandes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the MyOrg web application infrastructure.
 *
 *   MyOrg is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.*
 *
 *   MyOrg is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with MyOrg. If not, see <http://www.gnu.org/licenses/>.
 * 
 */


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myorg.domain.RoleType;
import myorg.domain.VirtualHost;
import myorg.domain.contents.ActionNode;
import myorg.domain.contents.Node;
import myorg.domain.groups.UserGroup;
import myorg.presentationTier.actions.ContextBaseAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.bennu.vaadin.domain.contents.VaadinNode;
import pt.ist.fenixWebFramework.servlets.functionalities.CreateNodeAction;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/metaProcessInterfaceCreationAction")
/**
 * 
 * @author João Antunes
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class MetaWorkflowInterfaceCreation extends ContextBaseAction {

    @CreateNodeAction(bundle = "META_WORKFLOW_RESOURCES", key = "add.node.workflow.metaInterface", groupKey = "label.module.metaWorkflow")
    public final ActionForward createWorkflowNode(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final VirtualHost virtualHost = getDomainObject(request, "virtualHostToManageId");
	final Node node = getDomainObject(request, "parentOfNodesToManageId");

	final Node homeNode = ActionNode.createActionNode(virtualHost, node, "/metaWorkflow", "viewOpenProcessesInMyQueues",
		"resources.MetaWorkflowResources", "link.topBar.MetaWorkflow", UserGroup.getInstance());
	    

	ActionNode.createActionNode(virtualHost, homeNode, "/metaWorkflow", "prepareCreateProcess",
		"resources.MetaWorkflowResources", "link.sideBar.metaWorkflow.createProcess", UserGroup.getInstance());
	
	ActionNode.createActionNode(virtualHost, homeNode, "/metaWorkflow", "viewOpenProcessesInMyQueues",
		"resources.MetaWorkflowResources", "link.sideBar.metaWorkflow.dashBoard", UserGroup.getInstance());
	
	ActionNode.createActionNode(virtualHost, homeNode, "/metaWorkflow", "search",
 "resources.MetaWorkflowResources",
		"link.sideBar.metaWorkflow.search", UserGroup.getInstance());

	ActionNode.createActionNode(virtualHost, homeNode, "/metaTypeManagement", "manageMetaType",
		"resources.MetaWorkflowResources", "link.sideBar.metaWorkflow.manageMetaType",
		myorg.domain.groups.Role.getRole(RoleType.MANAGER));
	
	
	
	VaadinNode.createVaadinNode(virtualHost, homeNode, "resources.MetaWorkflowResources",
		"link.sideBar.metaWorkflow.manageMetaType", "metaTypeManagement",
		myorg.domain.groups.Role.getRole(RoleType.MANAGER));

	return forwardToMuneConfiguration(request, virtualHost, node);
    }

}
