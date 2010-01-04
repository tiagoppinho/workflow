package module.workflow.presentationTier.renderers.providers;

import module.workflow.domain.WorkflowProcess;
import module.workflow.util.WorkflowFileUploadBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AvailableFileClassesProvider implements DataProvider {

    @Override
    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(Object source, Object current) {

	WorkflowFileUploadBean bean = (WorkflowFileUploadBean) source;
	WorkflowProcess process = bean.getProcess();
	return process.getUploadableFileTypes();
    }

}
