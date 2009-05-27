package module.workflow.activities;

import pt.ist.fenixWebFramework.util.DomainReference;
import module.workflow.domain.WorkflowProcess;
import myorg.domain.User;

public class GiveProcessInformation extends ActivityInformation<WorkflowProcess> {

    private static final long serialVersionUID = 1L;
    private DomainReference<User> user;

    public GiveProcessInformation(WorkflowProcess process,
	    WorkflowActivity<? extends WorkflowProcess, GiveProcessInformation> activity) {
	super(process, activity);
	setUser(null);
    }

    public void setUser(User user) {
	this.user = new DomainReference<User>(user);
    }

    public User getUser() {
	return this.user.getObject();
    }

    public boolean hasAllneededInfo() {
	return getUser() != null;
    }
}
