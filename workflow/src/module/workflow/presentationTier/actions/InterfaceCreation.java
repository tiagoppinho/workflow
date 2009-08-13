package module.workflow.presentationTier.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myorg.domain.VirtualHost;
import myorg.domain.contents.ActionNode;
import myorg.domain.contents.Node;
import myorg.domain.groups.AnyoneGroup;
import myorg.presentationTier.actions.ContextBaseAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.servlets.functionalities.CreateNodeAction;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/queuesInterfaceCreationAction")
public class InterfaceCreation extends ContextBaseAction {

    @CreateNodeAction(bundle = "WORKFLOW_RESOURCES", key = "add.node.workflow.queueInterface", groupKey = "label.module.workflow")
    public final ActionForward createWorkflowNode(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final VirtualHost virtualHost = getDomainObject(request, "virtualHostToManageId");
	final Node node = getDomainObject(request, "parentOfNodesToManageId");

	final Node homeNode = ActionNode.createActionNode(virtualHost, node, "/workflowQueueManagement", "manageQueues",
		"resources.WorkflowResources", "link.topBar.QueueManagement", AnyoneGroup.getInstance());

	return forwardToMuneConfiguration(request, virtualHost, node);
    }

}
