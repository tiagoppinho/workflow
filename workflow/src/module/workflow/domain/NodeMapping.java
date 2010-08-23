package module.workflow.domain;

import myorg.domain.contents.Node;
import pt.ist.fenixWebFramework.services.Service;

public class NodeMapping extends NodeMapping_Base implements Comparable<NodeMapping> {

    protected NodeMapping(Node node, int order) {
	super();
	setNodeOrder(order);
	setNode(node);
	setWorkflowSystem(WorkflowSystem.getInstance());
    }

    @Service
    public static NodeMapping createNodeMapping(Node node, int order) {
	return new NodeMapping(node, order);
    }

    public void delete() {
	removeWorkflowSystem();
	removeProcessMapping();
	removeNode();
	super.deleteDomainObject();
    }

    @Override
    public int compareTo(NodeMapping o) {
	return getNodeOrder() - o.getNodeOrder();
    }
}
