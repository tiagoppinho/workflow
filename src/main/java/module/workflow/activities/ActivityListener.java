package module.workflow.activities;

import module.workflow.domain.WorkflowProcess;

public interface ActivityListener {

    public void beforeExecute(ActivityInformation<? extends WorkflowProcess> activityInformation);

    public void afterExecute(ActivityInformation<? extends WorkflowProcess> activityInformation);

}
