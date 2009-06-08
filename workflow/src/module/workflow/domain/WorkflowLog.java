package module.workflow.domain;

import myorg.domain.MyOrg;
import myorg.domain.User;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.Strings;

public abstract class WorkflowLog extends WorkflowLog_Base {

    public WorkflowLog() {
	setMyOrg(MyOrg.getInstance());
	setOjbConcreteClass(this.getClass().getName());
	setWhenOperationWasRan(new DateTime());
    }

    public void init(WorkflowProcess process, User person, String... argumentsDescription) {
	setProcess(process);
	setActivityExecutor(person);
	if (argumentsDescription != null) {
	    setDescriptionArguments(new Strings(argumentsDescription));
	}
    }

    public abstract String getDescription();
}
