package module.metaWorkflow.presentationTier.renderers.providers;

import module.workflow.domain.WorkflowSystem;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AvailableWorkflowMetaTypes implements DataProvider {

    @Override
    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(Object arg0, Object arg1) {
	return WorkflowSystem.getInstance().getMetaTypesSet();
    }

}
