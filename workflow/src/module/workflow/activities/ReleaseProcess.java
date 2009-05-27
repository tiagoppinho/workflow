package module.workflow.activities;

import module.workflow.domain.WorkflowProcess;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class ReleaseProcess extends WorkflowActivity<WorkflowProcess, ActivityInformation<WorkflowProcess>> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkflowResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(WorkflowProcess process, User user) {
	return process.isTicketSupportAvailable() && process.getCurrentOwner() != null && process.getCurrentOwner() == user;
    }

    @Override
    protected void process(ActivityInformation<WorkflowProcess> information) {
	information.getProcess().releaseProcess();
    }

    public boolean isUserAwarenessNeeded(WorkflowProcess process, User user) {
	return false;
    }

}
