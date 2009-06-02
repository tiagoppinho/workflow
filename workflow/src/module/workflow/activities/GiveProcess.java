package module.workflow.activities;

import module.workflow.domain.WorkflowProcess;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class GiveProcess extends WorkflowActivity<WorkflowProcess, GiveProcessInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkflowResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(WorkflowProcess process, User user) {
	return process.isTicketSupportAvailable() && (process.getCurrentOwner() == null || process.getCurrentOwner() == user);
    }

    @Override
    protected void process(GiveProcessInformation information) {
	information.getProcess().giveProcess(information.getUser());
    }

    public boolean isUserAwarenessNeeded(WorkflowProcess process, User user) {
	return false;
    }

    @Override
    public ActivityInformation<WorkflowProcess> getActivityInformation(WorkflowProcess process) {
	return new GiveProcessInformation(process, this);
    }

}
