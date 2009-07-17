package module.metaWorkflow.domain;

import module.metaWorkflow.util.WorkflowQueueBean;
import module.organization.domain.Unit;
import myorg.domain.User;
import myorg.util.ClassNameResolver;

public class WorkflowUnitQueue extends WorkflowUnitQueue_Base {

    static {
	ClassNameResolver.registerType(WorkflowUnitQueue.class, "resources/MetaWorkflowResources",
		"label.module.metaWorkflow.domain.WorkflowUnitQueue");
    }

    public WorkflowUnitQueue(String name, WorkflowMetaType metaType) {
	super();
	init(metaType, name);
    }

    public WorkflowUnitQueue(Unit unit, WorkflowMetaType metaType, String name) {
	super();
	init(metaType, name);
	setUnit(unit);
    }

    @Override
    protected void fillNonDefaultFields(WorkflowQueueBean bean) {
	setUnit(((WorkflowUnitQueueBean) bean).getUnit());
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
