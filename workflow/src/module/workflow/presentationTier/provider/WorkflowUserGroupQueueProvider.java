package module.workflow.presentationTier.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import module.workflow.domain.WorkflowQueue;
import module.workflow.domain.WorkflowSystem;
import module.workflow.domain.WorkflowUserGroupQueue;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class WorkflowUserGroupQueueProvider implements DataProvider {

    @Override
    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	final List<WorkflowQueue> result = new ArrayList<WorkflowQueue>();
	for (WorkflowQueue workflowQueue : WorkflowSystem.getInstance().getWorkflowQueuesSet()) {
	    if (workflowQueue instanceof WorkflowUserGroupQueue) {
		result.add(workflowQueue);
	    }
	}

	Collections.sort(result, WorkflowQueue.COMPARATOR_BY_NAME);
	return result;
    }

}
