package module.metaWorkflow.activities;

import module.metaWorkflow.domain.FieldValue;
import module.metaWorkflow.domain.WorkflowMetaProcess;
import module.workflow.activities.ActivityInformation;

public class EditFieldValueInfo extends ActivityInformation<WorkflowMetaProcess> {

    private static final long serialVersionUID = 1L;
    private FieldValue field;

    public EditFieldValueInfo(WorkflowMetaProcess process, EditFieldValue activity) {
	super(process, activity);
    }

    public void setField(FieldValue field) {
	this.field = field;
    }

    public FieldValue getField() {
	return field;
    }

    @Override
    public boolean hasAllneededInfo() {
	return super.hasAllneededInfo() && isForwardedFromInput();
    }
}
