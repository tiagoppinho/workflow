package module.workflow.domain;

import java.util.Comparator;

import myorg.domain.User;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.Strings;

public abstract class WorkflowLog extends WorkflowLog_Base {

    public static final Comparator<WorkflowLog> COMPARATOR_BY_WHEN = new Comparator<WorkflowLog>() {

	@Override
	public int compare(final WorkflowLog log1, final WorkflowLog log2) {
	    final DateTime when1 = log1.getWhenOperationWasRan();
	    final DateTime when2 = log2.getWhenOperationWasRan();
	    final int result = when1.compareTo(when2);
	    return result == 0 ? log1.getExternalId().compareTo(log2.getExternalId()) : result;
	}

    };

    public static final Comparator<WorkflowLog> COMPARATOR_BY_WHEN_REVERSED = new Comparator<WorkflowLog>() {

	@Override
	public int compare(final WorkflowLog log1, final WorkflowLog log2) {
	    return COMPARATOR_BY_WHEN.compare(log2, log1);
	}

    };

    public WorkflowLog() {
	super.setWorkflowSystem(WorkflowSystem.getInstance());
	super.setOjbConcreteClass(this.getClass().getName());
	super.setWhenOperationWasRan(new DateTime());
    }

    public void init(WorkflowProcess process, User person, String... argumentsDescription) {
	super.setProcess(process);
	super.setActivityExecutor(person);
	if (argumentsDescription != null) {
	    super.setDescriptionArguments(new Strings(argumentsDescription));
	}
    }

    public abstract String getDescription();
}
