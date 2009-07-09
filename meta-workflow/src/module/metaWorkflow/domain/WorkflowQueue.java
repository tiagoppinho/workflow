package module.metaWorkflow.domain;

import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.MyOrg;
import myorg.domain.User;

public class WorkflowQueue extends WorkflowQueue_Base {

    public WorkflowQueue() {
	super();
	setMyOrg(MyOrg.getInstance());
    }

    public boolean isCurrentUserAbleToAccessQueue() {
	return isUserAbleToAccessQueue(UserView.getCurrentUser());
    }

    public boolean isUserAbleToAccessQueue(User user) {
	return true;
    }
}
