package module.workflow.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import myorg.domain.VirtualHost;
import myorg.domain.contents.Node;
import pt.ist.fenixWebFramework.services.Service;

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

    @Service
    public void addMapping(Node node) {
	List<NodeMapping> nodeMappings = getNodeMappings();
	int nextOrder = 0;
	if (!nodeMappings.isEmpty()) {
	    NodeMapping maxNode = Collections.max(nodeMappings);
	    nextOrder = maxNode.getNodeOrder() + 1;
	}

	super.addNodeMappings(NodeMapping.createNodeMapping(node, nextOrder));
    }

    @Service
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

    @Service
    public static ProcessSelectionMapper createNewMapper(String classname) {
	return new ProcessSelectionMapper(classname);
    }

    @Service
    public void delete() {
	removeWorkflowSystem();
	for (; !getNodeMappings().isEmpty(); getNodeMappings().get(0).delete()) {

	}
	super.deleteDomainObject();
    }

    @Override
    public boolean isConnectedToCurrentHost() {
	final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
	return virtualHost != null && getWorkflowSystem() == virtualHost.getWorkflowSystem();
    }

}
