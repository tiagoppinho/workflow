package module.workflow.presentationTier.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.workflow.domain.NodeMapping;
import module.workflow.domain.ProcessSelectionMapper;
import module.workflow.domain.WorkflowSystem;
import myorg.domain.MyOrg;
import myorg.domain.RoleType;
import myorg.domain.VirtualHost;
import myorg.domain.contents.ActionNode;
import myorg.domain.contents.Node;
import myorg.domain.groups.Role;
import myorg.presentationTier.actions.ContextBaseAction;
import myorg.util.VariantBean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.servlets.functionalities.CreateNodeAction;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/processSelectionNodeConfiguration")
public class ProcessSelectionNodeConfigurationAction extends ContextBaseAction {

    @CreateNodeAction(bundle = "WORKFLOW_RESOURCES", key = "add.node.workflow..nodeSelectionConfiguration", groupKey = "label.module.workflow")
    public final ActionForward createWorkflowNode(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final VirtualHost virtualHost = getDomainObject(request, "virtualHostToManageId");
	final Node node = getDomainObject(request, "parentOfNodesToManageId");

	createActionNode(virtualHost, node);

	return forwardToMuneConfiguration(request, virtualHost, node);
    }

    @Service
    private void createActionNode(final VirtualHost virtualHost, final Node node) {
	ActionNode.createActionNode(virtualHost, node, "/processSelectionNodeConfiguration", "manageNodeSelection",
		"resources.WorkflowResources", "link.topBar.nodeSelectionConfiguration", new Role(RoleType.MANAGER));
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
	List<NodeMapping> nodeMappings = mapper.getNodeMappings();
	int size = nodeMappings.size();
	request.setAttribute("nodes", size == 0 ? VirtualHost.getVirtualHostForThread().getOrderedTopLevelNodes() : nodeMappings
		.get(size - 1).getNode().getChildNodes());
	request.setAttribute("newMapper", new VariantBean());
	return forward(request, "/module/workflow/manageNodeSelection.jsp");
    }

}
