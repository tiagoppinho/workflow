package module.workflow.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import module.organization.domain.Person;
import module.workflow.util.WorkflowQueueBean;
import myorg.domain.User;
import myorg.domain.VirtualHost;
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

    @Override
    public boolean isConnectedToCurrentHost() {
	final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
	return virtualHost != null && getWorkflowSystem() == virtualHost.getWorkflowSystem();
    }

    @Override
    public Collection<Person> getPersons() {
	Collection<Person> persons = new ArrayList<Person>();

	List<User> users = getUsers();
	for (User user : users) {
	    persons.add(user.getPerson());
	}

	return persons;
    }

}
