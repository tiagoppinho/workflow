package module.workflow.domain;

import java.util.List;

import module.workflow.util.WorkflowQueueBean;
import myorg.domain.User;
import myorg.util.ClassNameBundle;
import pt.ist.fenixWebFramework.services.Service;

@ClassNameBundle(bundle = "resources/WorkflowResources")
public class WorkflowUserGroupQueue extends WorkflowUserGroupQueue_Base {

    public WorkflowUserGroupQueue(String name) {
	super();
	setName(name);
    }

    public WorkflowUserGroupQueue(List<User> baseUsers, String name) {
	super();
	setName(name);
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

    @Override
    @Service
    public void edit(WorkflowQueueBean bean) {
	setName(bean.getName());
	fillNonDefaultFields(bean);
    }

    @Override
    @Service
    public void removeUsers(User users) {
	super.removeUsers(users);
    }

}
