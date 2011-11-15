package module.metaWorkflow.domain;

public class StringFieldValue extends StringFieldValue_Base {

    public class StringFieldValueBean extends FieldValueBean {
	private String value;

	public StringFieldValueBean(String value) {
	    this.value = value;
	}

	public String getStringValue() {
	    return value;
	}

	public void setStringValue(String value) {
	    this.value = value;
	}
    }

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
    public StringFieldValueBean createFieldValueBean() {
	return new StringFieldValueBean(getStringValue());
    }

    @Override
    public void writeValueFromBean(FieldValueBean bean) {
	setStringValue(((StringFieldValueBean) bean).getStringValue());
    }
}
