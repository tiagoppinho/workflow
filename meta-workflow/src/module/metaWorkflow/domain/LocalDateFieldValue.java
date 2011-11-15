package module.metaWorkflow.domain;

import org.joda.time.LocalDate;

public class LocalDateFieldValue extends LocalDateFieldValue_Base {

    public class LocalDateFieldValueBean extends FieldValueBean {
	private LocalDate value;

	public LocalDateFieldValueBean(LocalDate value) {
	    this.value = value;
	}

	public LocalDate getLocalDateValue() {
	    return value;
	}

	public void setLocalDateValue(LocalDate value) {
	    this.value = value;
	}
    }

    public LocalDateFieldValue() {
	super();
    }

    public LocalDateFieldValue(LocalDateMetaField metaField) {
	this();
	setMetaField(metaField);
    }

    public LocalDateFieldValue(LocalDateMetaField metaField, FieldSetValue parentFieldSet, LocalDate value) {
	this(metaField);
	setParentFieldSet(parentFieldSet);
	setLocalDateValue(value);
    }

    @Override
    public FieldValueBean createFieldValueBean() {
	return new LocalDateFieldValueBean(getLocalDateValue());
    }

    @Override
    public void writeValueFromBean(FieldValueBean bean) {
	setLocalDateValue(((LocalDateFieldValueBean) bean).getLocalDateValue());
    }
}
