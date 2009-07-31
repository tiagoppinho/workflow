package module.metaWorkflow.presentationTier.renderers.providers;

import java.util.Collections;

import module.metaWorkflow.domain.WorkflowMetaType;
import module.metaWorkflow.domain.WorkflowUnitQueueBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AccountabilityTypeForGivenMetaType implements DataProvider {

    @Override
    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	WorkflowUnitQueueBean bean = (WorkflowUnitQueueBean) source;

	WorkflowMetaType metaType = bean.getMetaType();
	return metaType == null ? Collections.EMPTY_LIST : metaType.getOrganizationalModel().getAccountabilityTypes();
    }
}
