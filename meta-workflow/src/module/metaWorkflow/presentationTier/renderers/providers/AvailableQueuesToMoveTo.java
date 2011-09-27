package module.metaWorkflow.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import module.metaWorkflow.domain.WorkflowMetaProcess;
import module.workflow.activities.ChangeQueueInformation;
import module.workflow.domain.WorkflowQueue;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AvailableQueuesToMoveTo implements DataProvider {

    @Override
    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(Object source, Object value) {
	ChangeQueueInformation<WorkflowMetaProcess> information = (ChangeQueueInformation<WorkflowMetaProcess>) source;
	return provide(information.getProcess());
    }

    public Object provide(WorkflowMetaProcess process) {
	List<WorkflowQueue> queues = new ArrayList<WorkflowQueue>(process.getMetaType().getQueues());
	queues.removeAll(process.getCurrentQueues());
	Collections.sort(queues, WorkflowQueue.COMPARATOR_BY_NAME);
	return queues;
    }
}