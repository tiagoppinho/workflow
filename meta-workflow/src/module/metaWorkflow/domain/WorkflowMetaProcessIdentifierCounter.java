package module.metaWorkflow.domain;

import pt.ist.fenixWebFramework.services.Service;
import myorg.domain.MyOrg;

public class WorkflowMetaProcessIdentifierCounter extends WorkflowMetaProcessIdentifierCounter_Base {

    private WorkflowMetaProcessIdentifierCounter() {
	super();
	setMyOrg(MyOrg.getInstance());
    }

    @Override
    public void setCounter(int counter) {
	throw new UnsupportedOperationException("Should never try to set the counter");
    }

    public int getNextIdentifier() {
	int identifier = getCounter() + 1;
	super.setCounter(identifier);
	return identifier;
    }

    public static WorkflowMetaProcessIdentifierCounter getProcessIdentifierCounter() {
	WorkflowMetaProcessIdentifierCounter workflowMetaProcessIdentifierCounter = MyOrg.getInstance()
		.getWorkflowMetaProcessIdentifierCounter();
	if (workflowMetaProcessIdentifierCounter == null) {
	    workflowMetaProcessIdentifierCounter = createCounter();
	}

	return workflowMetaProcessIdentifierCounter;
    }

    @Service
    private static WorkflowMetaProcessIdentifierCounter createCounter() {
	return new WorkflowMetaProcessIdentifierCounter();
    }
}
