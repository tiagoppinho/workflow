package module.workflow.domain;

import java.util.HashSet;
import java.util.Set;

import module.workflow.util.WorkflowQueueBean;
import myorg.domain.User;

public class WorkflowUserGroupQueueBean extends WorkflowQueueBean {

    private Set<User> users;
    private User userToAdd;

    public WorkflowUserGroupQueueBean() {
	super();
	users = new HashSet<User>();
	setUserToAdd(null);
    }

    public Set<User> getUsers() {
	return users;
    }

    public void addUser(User user) {
	this.users.add(user);
    }

    public User getUserToAdd() {
	return userToAdd;
    }

    public void setUserToAdd(User user) {
	this.userToAdd = user;
    }

    @Override
    protected void fillQueueFields(WorkflowQueue queue) {
	for (User user : ((WorkflowUserGroupQueue) queue).getUsers()) {
	    users.add(user);
	}
    }
}
