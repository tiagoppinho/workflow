package module.metaWorkflow.domain;

public class DateTimeMetaField extends DateTimeMetaField_Base {

    public DateTimeMetaField() {
	super();
    }

    @Override
    public FieldValue createFieldValue() {
	return new DateTimeFieldValue(this);
    }

    @Override
    public void delete() {
	removeParentFieldSet();
	if (!hasAnyFieldValues()) {
	    deleteDomainObject();
	}
    }
}
