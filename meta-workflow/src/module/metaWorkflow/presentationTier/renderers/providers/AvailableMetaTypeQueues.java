package module.metaWorkflow.presentationTier.renderers.providers;

import module.metaWorkflow.domain.WorkflowMetaProcess;
import module.workflow.activities.ChangeQueueInformation;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AvailableMetaTypeQueues implements DataProvider {

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
	return process.getMetaType().getQueues();
    }
}