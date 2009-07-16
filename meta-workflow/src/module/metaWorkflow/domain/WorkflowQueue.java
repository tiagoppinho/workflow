package module.metaWorkflow.domain;

import myorg.domain.MyOrg;
import myorg.domain.User;
import myorg.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.services.Service;

public class WorkflowQueue extends WorkflowQueue_Base {

    public WorkflowQueue() {
	super();
	setMyOrg(MyOrg.getInstance());
	setOjbConcreteClass(this.getClass().getName());
    }

    public WorkflowQueue(String name, WorkflowMetaType metaType) {
	this();
	setName(name);
	setMetaType(metaType);
    }

    public boolean isUserAbleToAccessQueue(User user) {
	return true;
    }

    @Override
    public void addMetaProcess(WorkflowMetaProcess metaProcess) {
	if (metaProcess.getMetaType() != getMetaType()) {
	    throw new DomainException("error.queue.addingProcessWithInvalidMetaType");
	}
	super.addMetaProcess(metaProcess);
    }

    @Service
    public static void createQueue(String name, WorkflowMetaType metaType) {
	new WorkflowQueue(name, metaType);
    }
}
