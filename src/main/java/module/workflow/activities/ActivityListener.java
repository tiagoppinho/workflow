package module.workflow.activities;

import module.workflow.domain.WorkflowProcess;

public interface ActivityListener {

    default public void beforeExecute(ActivityInformation<? extends WorkflowProcess> activityInformation) {
    }

    default public void afterExecute(ActivityInformation<? extends WorkflowProcess> activityInformation) {
    }

    default public void beforeProcess(ActivityInformation<? extends WorkflowProcess> activityInformation) {
    }

    default public void afterProcess(ActivityInformation<? extends WorkflowProcess> activityInformation) {
    }

}
