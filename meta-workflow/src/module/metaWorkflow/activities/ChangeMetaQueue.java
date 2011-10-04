package module.metaWorkflow.activities;

import java.util.Iterator;

import module.metaWorkflow.domain.WorkflowMetaProcess;
import module.workflow.activities.ChangeQueue;
import module.workflow.activities.ChangeQueueInformation;
import module.workflow.domain.WorkflowQueue;
import myorg.util.BundleUtil;

public class ChangeMetaQueue extends ChangeQueue<WorkflowMetaProcess> {

    @Override
    public String getUsedBundle() {
	return "resources/MetaWorkflowResources";
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
	return false;
    }

    @Override
    protected String[] getArgumentsDescription(ChangeQueueInformation<WorkflowMetaProcess> activityInformation) {
	String queuesDescription = "";
	if (activityInformation.hasAnyQueuesToRemove()) {
	    if (activityInformation.hasAnyQueuesToAdd()) {
		queuesDescription += BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label.from");
	    } else {
		queuesDescription += BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label.removedFrom");
	    }

	    queuesDescription += " ";
	    Iterator<WorkflowQueue> queuesToRemove = activityInformation.getQueuesToRemove().iterator();
	    if (queuesToRemove.hasNext()) {
		queuesDescription += queuesToRemove.next().getName();
	    }
	    while (queuesToRemove.hasNext()) {
		queuesDescription += ", " + queuesToRemove.next().getName();
	    }
	}

	if (activityInformation.hasAnyQueuesToAdd()) {
	    if (activityInformation.hasAnyQueuesToRemove()) {
		queuesDescription += " | ";
	    }
	    queuesDescription += BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label.to");

	    queuesDescription += " ";
	    Iterator<WorkflowQueue> queuesToAdd = activityInformation.getQueuesToAdd().iterator();
	    if (queuesToAdd.hasNext()) {
		queuesDescription += queuesToAdd.next().getName();
	    }
	    while (queuesToAdd.hasNext()) {
		queuesDescription += ", " + queuesToAdd.next().getName();
	    }
	}

	return new String[] { queuesDescription };
    }
}
