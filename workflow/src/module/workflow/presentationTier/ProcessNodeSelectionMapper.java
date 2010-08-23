package module.workflow.presentationTier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import module.workflow.domain.NodeMapping;
import module.workflow.domain.ProcessSelectionMapper;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowSystem;
import myorg.domain.contents.Node;

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
