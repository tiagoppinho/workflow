/*
 * @(#)ProcessNodeSelectionMapper.java
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
package module.workflow.presentationTier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import module.workflow.domain.NodeMapping;
import module.workflow.domain.ProcessSelectionMapper;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowSystem;
import pt.ist.bennu.core.domain.contents.Node;

/**
 * 
 * @author Paulo Abrantes
 * 
 */
public class ProcessNodeSelectionMapper {

    public static List<Node> getForwardFor(Class<? extends WorkflowProcess> processClass) {
        for (ProcessSelectionMapper mapper : WorkflowSystem.getInstance().getProcessMappings()) {
            if (mapper.getClassname().equals(processClass.getName())) {
                List<Node> nodes = new ArrayList<Node>();
                List<NodeMapping> mappings = new ArrayList<NodeMapping>();
                mappings.addAll(mapper.getNodeMappings());
                Collections.sort(mappings, new Comparator<NodeMapping>() {

                    @Override
                    public int compare(NodeMapping o1, NodeMapping o2) {
                        return Integer.valueOf(o1.getNodeOrder()).compareTo(o2.getNodeOrder());
                    }
                });
                for (NodeMapping mapping : mappings) {
                    nodes.add(mapping.getNode());
                }
                return nodes;
            }
        }
        return Collections.emptyList();
    }
}
