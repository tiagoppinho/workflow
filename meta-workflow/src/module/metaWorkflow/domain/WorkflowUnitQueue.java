package module.metaWorkflow.domain;

import module.organization.domain.Unit;
import myorg.domain.User;

public class WorkflowUnitQueue extends WorkflowUnitQueue_Base {

    public WorkflowUnitQueue(Unit unit, WorkflowMetaType metaType) {
	super();
	setMetaType(metaType);
	setUnit(unit);
    }

    @Override
    public boolean isUserAbleToAccessQueue(User user) {
	// TODO: This should go to the user, get it's person
	// and understand if the available accountability types
	// in metaType matches any accountability type in relation
	// person <=> unit

	return true;
    }
}
