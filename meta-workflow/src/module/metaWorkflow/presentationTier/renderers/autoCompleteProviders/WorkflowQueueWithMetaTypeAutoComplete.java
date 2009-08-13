package module.metaWorkflow.presentationTier.renderers.autoCompleteProviders;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import module.metaWorkflow.domain.WorkflowMetaType;
import module.workflow.domain.WorkflowQueue;
import myorg.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class WorkflowQueueWithMetaTypeAutoComplete implements AutoCompleteProvider {

    @Override
    public Collection getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
	Set<WorkflowQueue> queues = new HashSet<WorkflowQueue>();
	WorkflowMetaType type = getWorkflowMetaType(argsMap.get("metaTypeId"));

	Collection<WorkflowQueue> lookUpQueues = type.getQueues();
	String[] values = StringNormalizer.normalize(value).toLowerCase().split(" ");
	for (WorkflowQueue queue : lookUpQueues) {
	    final String normalizedQueueName = StringNormalizer.normalize(queue.getName()).toLowerCase();

	    if (hasMatch(values, normalizedQueueName)) {
		queues.add(queue);
	    }
	}
	return queues;
    }

    private WorkflowMetaType getWorkflowMetaType(String oid) {
	return AbstractDomainObject.fromExternalId(oid);
    }

    private boolean hasMatch(String[] input, String queueNameParts) {
	for (final String namePart : input) {
	    if (queueNameParts.indexOf(namePart) == -1) {
		return false;
	    }
	}
	return true;
    }

}
