package module.workflow.domain;

import dml.runtime.RelationAdapter;
import module.organization.domain.Party;
import module.organization.domain.PartyType;
import module.organizationIst.domain.IstPartyType;
import myorg.domain.MyOrg;
import myorg.domain.contents.Node;
import pt.ist.expenditureTrackingSystem.domain.organization.CostCenter;
import pt.ist.fenixWebFramework.services.Service;

public class NodeMapping extends NodeMapping_Base implements Comparable<NodeMapping> {

    public static class NodeListenner extends RelationAdapter<Node, MyOrg> {

	@Override
	public void afterRemove(final Node node, final MyOrg myorg) {
	    if (node != null && myorg != null) {
		for (; !node.getNodeMapping().isEmpty(); node.getNodeMapping().get(0).delete())
		    ;
	    }
	}

    }

    static {
	Node.MyOrgNode.addListener(new NodeListenner());
    }

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
