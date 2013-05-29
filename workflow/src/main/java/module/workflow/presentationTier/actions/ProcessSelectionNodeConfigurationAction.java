/*
 * @(#)ProcessSelectionNodeConfigurationAction.java
 *
 * Copyright 2010 Instituto Superior Tecnico
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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.workflow.domain.NodeMapping;
import module.workflow.domain.ProcessSelectionMapper;
import module.workflow.domain.WorkflowSystem;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.bennu.core.domain.RoleType;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.domain.contents.ActionNode;
import pt.ist.bennu.core.domain.contents.Node;
import pt.ist.bennu.core.domain.groups.Role;
import pt.ist.bennu.core.presentationTier.actions.ContextBaseAction;
import pt.ist.bennu.core.util.VariantBean;
import pt.ist.fenixWebFramework.servlets.functionalities.CreateNodeAction;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;

@Mapping(path = "/processSelectionNodeConfiguration")
/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class ProcessSelectionNodeConfigurationAction extends ContextBaseAction {

    @CreateNodeAction(bundle = "WORKFLOW_RESOURCES", key = "add.node.workflow..nodeSelectionConfiguration",
            groupKey = "label.module.workflow")
    public final ActionForward createWorkflowNode(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final VirtualHost virtualHost = getDomainObject(request, "virtualHostToManageId");
        final Node node = getDomainObject(request, "parentOfNodesToManageId");

        createActionNode(virtualHost, node);

        return forwardToMuneConfiguration(request, virtualHost, node);
    }

    @Atomic
    private void createActionNode(final VirtualHost virtualHost, final Node node) {
        ActionNode.createActionNode(virtualHost, node, "/processSelectionNodeConfiguration", "manageNodeSelection",
                "resources.WorkflowResources", "link.topBar.nodeSelectionConfiguration", Role.getRole(RoleType.MANAGER));
    }

    public final ActionForward newMapper(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        VariantBean bean = getRenderedObject("newMapper");
        ProcessSelectionMapper.createNewMapper(((Class) bean.getObject()).getName());
        return manageNodeSelection(mapping, form, request, response);
    }

    public final ActionForward deleteMapper(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        ProcessSelectionMapper mapper = getDomainObject(request, "mapperToDeleteId");
        mapper.delete();
        return manageNodeSelection(mapping, form, request, response);
    }

    public final ActionForward manageNodeSelection(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        ProcessSelectionMapper mapper = getDomainObject(request, "mapperId");
        if (mapper == null) {
            request.setAttribute("mappers", WorkflowSystem.getInstance().getProcessMappings());
        } else {
            request.setAttribute("mapper", mapper);
            request.setAttribute("nodes", VirtualHost.getVirtualHostForThread().getOrderedTopLevelNodes());
        }
        request.setAttribute("newMapper", new VariantBean());
        return forward(request, "/module/workflow/manageNodeSelection.jsp");
    }

    public final ActionForward addNodeSelection(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        Node node = getDomainObject(request, "nodeId");
        ProcessSelectionMapper mapper = getDomainObject(request, "mapperId");
        mapper.addMapping(node);

        request.setAttribute("mapper", mapper);
        request.setAttribute("nodes", node.getChildNodes());
        request.setAttribute("newMapper", new VariantBean());
        return forward(request, "/module/workflow/manageNodeSelection.jsp");
    }

    public final ActionForward removeNodeSelection(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        ProcessSelectionMapper mapper = getDomainObject(request, "mapperId");
        mapper.removeMapping(Integer.valueOf(request.getParameter("index")));

        request.setAttribute("mapper", mapper);
        List<NodeMapping> nodeMappings = new ArrayList<>(mapper.getNodeMappings());
        int size = nodeMappings.size();
        request.setAttribute("nodes", size == 0 ? VirtualHost.getVirtualHostForThread().getOrderedTopLevelNodes() : nodeMappings
                .get(size - 1).getNode().getChildNodes());
        request.setAttribute("newMapper", new VariantBean());
        return forward(request, "/module/workflow/manageNodeSelection.jsp");
    }

}
