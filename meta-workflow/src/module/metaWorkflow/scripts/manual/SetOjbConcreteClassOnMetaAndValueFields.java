package module.metaWorkflow.scripts.manual;

import java.util.List;

import jvstm.TransactionalCommand;
import module.metaWorkflow.domain.FieldValue;
import module.metaWorkflow.domain.MetaField;
import module.metaWorkflow.domain.MetaFieldSet;
import module.metaWorkflow.domain.WorkflowMetaType;
import module.workflow.domain.WorkflowSystem;
import myorg.domain.scheduler.CustomTask;
import pt.ist.fenixframework.pstm.Transaction;

public class SetOjbConcreteClassOnMetaAndValueFields extends CustomTask implements TransactionalCommand {

    @Override
    public void doIt() {
	WorkflowSystem system = WorkflowSystem.getInstance();

	List<WorkflowMetaType> metaTypes = system.getMetaTypes();

	for (WorkflowMetaType workflowMetaType : metaTypes) {
	    MetaFieldSet fieldSet = workflowMetaType.getFieldSet();
	    setOjbConcteteClassOnMetaField(fieldSet);
	}
    }

    void setOjbConcteteClassOnMetaField(final MetaField metaField) {
	metaField.setOjbConcreteClass(metaField.getClass().getName());

	List<FieldValue> fieldValues = metaField.getFieldValues();
	for (FieldValue fieldValue : fieldValues) {
	    fieldValue.setOjbConcreteClass(fieldValue.getClass().getName());
	}

	if (metaField instanceof MetaFieldSet) {
	    MetaFieldSet metaFieldSet = (MetaFieldSet) metaField;

	    List<MetaField> childFields = metaFieldSet.getChildFields();
	    for (MetaField child : childFields) {
		setOjbConcteteClassOnMetaField(child);
	    }
	}
    }

    @Override
    public void run() {
	Transaction.withTransaction(false, this);
	out.println("Done.");
    }

}
