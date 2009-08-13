package module.metaWorkflow.presentationTier.renderers.providers;

import java.util.Collections;

import module.metaWorkflow.domain.WorkflowMetaType;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AvailableUserQueues implements DataProvider {

    @Override
    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(Object source, Object value) {
	User user = UserView.getCurrentUser();
	if (user == null) {
	    return Collections.EMPTY_LIST;
	}

	return WorkflowMetaType.getAllQueuesForUser(user);
    }
}