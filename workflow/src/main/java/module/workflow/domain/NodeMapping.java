/*
 * @(#)NodeMapping.java
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
package module.workflow.domain;

import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.domain.contents.Node;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class NodeMapping extends NodeMapping_Base implements Comparable<NodeMapping> {

    public static class NodeListenner extends RelationAdapter<Node, MyOrg> {

        @Override
        public void afterRemove(final Node node, final MyOrg myorg) {
            if (node != null && myorg != null) {
                for (; !node.getNodeMapping().isEmpty(); node.getNodeMapping().iterator().next().delete()) {
                    ;
                }
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

    @Atomic
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

    @Override
    public boolean isConnectedToCurrentHost() {
        final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
        return virtualHost != null && getWorkflowSystem() == virtualHost.getWorkflowSystem();
    }

}
