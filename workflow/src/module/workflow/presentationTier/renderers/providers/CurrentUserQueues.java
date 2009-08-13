package module.workflow.presentationTier.renderers.providers;

import java.util.Collections;

import module.workflow.domain.WorkflowQueue;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CurrentUserQueues implements DataProvider {

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

	return WorkflowQueue.getQueuesForUser(user);
    }
}
