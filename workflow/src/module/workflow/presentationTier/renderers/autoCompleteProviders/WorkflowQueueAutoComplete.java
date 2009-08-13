package module.workflow.presentationTier.renderers.autoCompleteProviders;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import module.workflow.domain.WorkflowQueue;
import module.workflow.domain.WorkflowSystem;
import myorg.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class WorkflowQueueAutoComplete implements AutoCompleteProvider {

    @Override
    public Collection getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
	Set<WorkflowQueue> queues = new HashSet<WorkflowQueue>();

	String[] values = StringNormalizer.normalize(value).toLowerCase().split(" ");
	for (WorkflowQueue queue : WorkflowSystem.getInstance().getWorkflowQueues()) {
	    final String normalizedQueueName = StringNormalizer.normalize(queue.getName()).toLowerCase();

	    if (hasMatch(values, normalizedQueueName)) {
		queues.add(queue);
	    }
	}
	return queues;
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
