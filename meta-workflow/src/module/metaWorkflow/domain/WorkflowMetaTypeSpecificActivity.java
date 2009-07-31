package module.metaWorkflow.domain;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;

public class WorkflowMetaTypeSpecificActivity extends WorkflowMetaTypeSpecificActivity_Base {

    public WorkflowMetaTypeSpecificActivity(WorkflowMetaType type, Class<? extends WorkflowActivity> activity) {
	super();
	setMetaType(type);
	setActivityClass(activity.getClass().getName());
    }

    public <T extends WorkflowProcess> WorkflowActivity<T, ActivityInformation<T>> getActivity() {
	try {
	    return (WorkflowActivity<T, ActivityInformation<T>>) Class.forName(getActivityClass()).newInstance();
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }
}
