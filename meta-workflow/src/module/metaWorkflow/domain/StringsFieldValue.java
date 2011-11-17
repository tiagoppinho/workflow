package module.metaWorkflow.domain;

import pt.utl.ist.fenix.tools.util.Strings;

public class StringsFieldValue extends StringsFieldValue_Base {

    public class StringsFieldValueBean extends FieldValueBean {
	private Strings value;

	public StringsFieldValueBean(Strings value) {
	    this.value = value;
	}

	public Strings getStringsValue() {
	    return value;
	}

	public void setStringsValue(Strings value) {
	    this.value = value;
	}
    }

    public StringsFieldValue() {
	super();
    }

    public StringsFieldValue(StringsMetaField metaField) {
	this();
	setMetaField(metaField);
    }

    public StringsFieldValue(StringsMetaField metaField, FieldSetValue parent, Strings value) {
	this(metaField);
	setParentFieldSet(parent);
	setStringsValue(value);
    }

    @Override
    public FieldValueBean createFieldValueBean() {
	return new StringsFieldValueBean(getStringsValue());
    }

    @Override
    public void writeValueFromBean(FieldValueBean bean) {
	setStringsValue(((StringsFieldValueBean) bean).getStringsValue());
    }
}
