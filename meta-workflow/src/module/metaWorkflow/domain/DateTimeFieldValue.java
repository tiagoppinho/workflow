package module.metaWorkflow.domain;


public class DateTimeFieldValue extends DateTimeFieldValue_Base {

    public DateTimeFieldValue() {
	super();
    }

    public DateTimeFieldValue(DateTimeMetaField metaField) {
	this();
	setMetaField(metaField);
    }

    @Override
    public String getSchemaName() {
	return "fieldValue.DateTimeFieldValue";
    }
}
