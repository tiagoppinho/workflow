package module.metaWorkflow.domain;

public class StringFieldValue extends StringFieldValue_Base {

    protected StringFieldValue() {
	super();
    }

    public StringFieldValue(StringMetaField metaField) {
	this();
	setMetaField(metaField);
    }

    public StringFieldValue(FieldSetValue parentFieldSet, StringMetaField metaField) {
	this(metaField);
	setParentFieldSet(parentFieldSet);
    }

    public StringFieldValue(String value, FieldSetValue parentFieldSet, StringMetaField metaField) {
	this(parentFieldSet, metaField);
	setStringValue(value);
    }

    @Override
    public String getValueSlotName() {
	return "stringValue";
    }
}
