package module.metaWorkflow.activities;

import module.metaWorkflow.domain.FieldValue;
import module.metaWorkflow.domain.FieldValue.FieldValueBean;
import module.metaWorkflow.domain.WorkflowMetaProcess;
import module.workflow.activities.ActivityInformation;

public class EditFieldValueInfo extends ActivityInformation<WorkflowMetaProcess> {

    private static final long serialVersionUID = 1L;
    private FieldValue field;
    private FieldValueBean fieldBean;

    public EditFieldValueInfo(WorkflowMetaProcess process, EditFieldValue activity) {
	super(process, activity);
    }

    public void setField(FieldValue fieldValue) {
	setFieldBean(fieldValue.createFieldValueBean());
    }

    public void setFieldBean(FieldValueBean fieldBean) {
	this.fieldBean = fieldBean;
    }

    public FieldValueBean getFieldBean() {
	return fieldBean;
    }

    @Override
    public boolean hasAllneededInfo() {
	return super.hasAllneededInfo() && isForwardedFromInput();
    }
}
