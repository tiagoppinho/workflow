package module.metaWorkflow.activities;

import module.metaWorkflow.domain.WorkflowMetaProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;

public class OpenMetaProcess extends WorkflowActivity<WorkflowMetaProcess, ActivityInformation<WorkflowMetaProcess>> {

    @Override
    public boolean isActive(WorkflowMetaProcess process, User user) {
	return !process.isOpen() && (process.getCurrentQueues().contains(user.getQueues()) || user.getUserProcesses().contains(process));
    }
    
    @Override
    public String getUsedBundle() {
	return "resources/MetaWorkflowResources";
    }

    @Override
    protected void process(ActivityInformation<WorkflowMetaProcess> activityInformation) {
	activityInformation.getProcess().open();
    }

    @Override
    public boolean isUserAwarenessNeeded(WorkflowMetaProcess process, User user) {
	return false;
    }
}
