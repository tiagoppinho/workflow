package module.metaWorkflow.activities;

import module.metaWorkflow.domain.Requestor;
import module.metaWorkflow.domain.UserRequestor;
import module.metaWorkflow.domain.WorkflowMetaProcess;
import module.workflow.activities.UserInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;

public class ChangeRequestor extends WorkflowActivity<WorkflowMetaProcess, UserInformation<WorkflowMetaProcess>> {

    @Override
    public boolean isActive(WorkflowMetaProcess process, User user) {
	return process.isAccessible(user) && !process.isUserObserver(user);
    }

    @Override
    protected void process(UserInformation<WorkflowMetaProcess> activityInformation) {
	User user = activityInformation.getUser();
	Requestor requestor = user.getRequestor();
	if (requestor == null) {
	    requestor = new UserRequestor(user);
	}
	activityInformation.getProcess().setRequestor(requestor);
    }

    @Override
    public String getUsedBundle() {
	return "resources/MetaWorkflowResources";
    }

    @Override
    public UserInformation<WorkflowMetaProcess> getActivityInformation(WorkflowMetaProcess process) {
	return new UserInformation<WorkflowMetaProcess>(process, this);
    }

    @Override
    protected String[] getArgumentsDescription(UserInformation<WorkflowMetaProcess> activityInformation) {
	User user = activityInformation.getUser();
	Requestor currentRequestor = activityInformation.getProcess().getRequestor();
	return new String[] { currentRequestor != null ? currentRequestor.getName() : "", user.getPresentationName() };
    }
}