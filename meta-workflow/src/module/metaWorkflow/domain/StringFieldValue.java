package module.metaWorkflow.domain;

public class StringFieldValue extends StringFieldValue_Base {

    protected StringFieldValue() {
	super();
    }

    public StringFieldValue(MetaStringField metaField) {
	this();
	setMetaField(metaField);
    }

    public StringFieldValue(FieldSetValue parentFieldSet, MetaStringField metaField) {
	this(metaField);
	setParentFieldSet(parentFieldSet);
    }

    public StringFieldValue(String value, FieldSetValue parentFieldSet, MetaStringField metaField) {
	this(parentFieldSet, metaField);
	setValue(value);
    }

    @Override
    public String getSchemaName() {
	return "fieldValue.StringFieldValue";
    }
}
