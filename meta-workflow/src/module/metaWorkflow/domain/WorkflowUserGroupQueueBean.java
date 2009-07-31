package module.metaWorkflow.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import module.metaWorkflow.util.WorkflowQueueBean;
import myorg.domain.User;
import pt.ist.fenixWebFramework.util.DomainReference;

public class WorkflowUserGroupQueueBean extends WorkflowQueueBean implements Serializable {

    private Set<DomainReference<User>> users;
    private DomainReference<User> userToAdd;

    public WorkflowUserGroupQueueBean() {
	super();
	users = new HashSet<DomainReference<User>>();
	setUserToAdd(null);
    }

    public List<User> getUsers() {
	List<User> users = new ArrayList<User>();
	for (DomainReference<User> user : this.users) {
	    users.add(user.getObject());
	}
	return users;
    }

    public void addUser(User user) {
	this.users.add(new DomainReference<User>(user));
    }

    public User getUserToAdd() {
	return userToAdd.getObject();
    }

    public void setUserToAdd(User user) {
	this.userToAdd = new DomainReference<User>(user);
    }

    @Override
    protected void fillQueueFields(WorkflowQueue queue) {
	for (User user : ((WorkflowUserGroupQueue) queue).getUsers()) {
	    users.add(new DomainReference<User>(user));
	}
    }
}
