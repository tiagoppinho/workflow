package module.metaWorkflow.activities;

import module.metaWorkflow.domain.WorkflowMetaProcess;
import module.workflow.activities.ChangeQueue;

public class ChangeMetaQueue extends ChangeQueue<WorkflowMetaProcess> {

    @Override
    public String getUsedBundle() {
	return "resources/MetaWorkflowResources";
    }
}
