/*
 * @(#)ProcessSelectionMapper.java
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.domain.contents.Node;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class ProcessSelectionMapper extends ProcessSelectionMapper_Base {

    protected ProcessSelectionMapper(String classname) {
        super();
        setWorkflowSystem(WorkflowSystem.getInstance());
        setClassname(classname);
    }

    public List<NodeMapping> getOrderedNodeMappings() {
        List<NodeMapping> mappings = new ArrayList<NodeMapping>(super.getNodeMappings());
        Collections.sort(mappings);
        return mappings;
    }

    public Class<? extends WorkflowProcess> getMappedClass() {
        try {
            return (Class<? extends WorkflowProcess>) Class.forName(getClassname());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Atomic
    public void addMapping(Node node) {
        Set<NodeMapping> nodeMappings = getNodeMappings();
        int nextOrder = 0;
        if (!nodeMappings.isEmpty()) {
            NodeMapping maxNode = Collections.max(nodeMappings);
            nextOrder = maxNode.getNodeOrder() + 1;
        }

        super.addNodeMappings(NodeMapping.createNodeMapping(node, nextOrder));
    }

    @Atomic
    public void removeMapping(int i) {
        NodeMapping nodeMappingToDelete = null;
        for (NodeMapping mapping : getNodeMappings()) {
            if (mapping.getNodeOrder() == i) {
                nodeMappingToDelete = mapping;
                break;
            }
        }
        if (nodeMappingToDelete != null) {
            nodeMappingToDelete.delete();
        }
    }

    @Atomic
    public static ProcessSelectionMapper createNewMapper(String classname) {
        return new ProcessSelectionMapper(classname);
    }

    @Atomic
    public void delete() {
        removeWorkflowSystem();
        for (; !getNodeMappings().isEmpty(); getNodeMappings().iterator().next().delete()) {

        }
        super.deleteDomainObject();
    }

    @Override
    public boolean isConnectedToCurrentHost() {
        final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
        return virtualHost != null && getWorkflowSystem() == virtualHost.getWorkflowSystem();
    }

}
