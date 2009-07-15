package module.metaWorkflow.activities;

import module.metaWorkflow.domain.WorkflowMetaProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;

public class OpenMetaProcess extends WorkflowActivity<WorkflowMetaProcess, ActivityInformation<WorkflowMetaProcess>> {

    @Override
    public boolean isActive(WorkflowMetaProcess process, User user) {
	return !process.isOpen() && process.isAccessible(user) && !process.isUserObserver(user);
    }

    @Override
    public String getUsedBundle() {
	return "resources/MetaWorkflowResources";
    }

    @Override
    protected void process(ActivityInformation<WorkflowMetaProcess> activityInformation) {
	activityInformation.getProcess().open();
    }

}
