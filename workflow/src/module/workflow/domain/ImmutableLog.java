package module.workflow.domain;

import myorg.domain.User;

public class ImmutableLog extends ImmutableLog_Base {

    protected ImmutableLog() {
	super();
    }

    public ImmutableLog(WorkflowProcess process, User person, String description) {
	super();
	init(process, person);
	setDescription(description);
    }

}
