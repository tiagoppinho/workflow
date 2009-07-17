package module.metaWorkflow.domain;

import java.util.List;

import module.metaWorkflow.util.WorkflowQueueBean;
import myorg.domain.User;
import myorg.util.ClassNameResolver;

public class WorkflowUserGroupQueue extends WorkflowUserGroupQueue_Base {

    static {
	ClassNameResolver.registerType(WorkflowUserGroupQueue.class, "resources/MetaWorkflowResources",
		"label.module.metaWorkflow.domain.WorkflowUserGroupQueue");
    }
    
    public WorkflowUserGroupQueue(String name, WorkflowMetaType metaType) {
	init(metaType, name);
    }

    public WorkflowUserGroupQueue(List<User> baseUsers, String name, WorkflowMetaType metaType) {
	super();
	init(metaType, name);
	for (User user : baseUsers) {
	    addUsers(user);
	}
    }

    @Override
    public boolean isUserAbleToAccessQueue(User user) {
	return getUsers().contains(user);
    }

    @Override
    protected void fillNonDefaultFields(WorkflowQueueBean bean) {
	for (User user : ((WorkflowUserGroupQueueBean) bean).getUsers()) {
	    addUsers(user);
	}
    }

}
