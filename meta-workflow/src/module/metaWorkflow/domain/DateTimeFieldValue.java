package module.metaWorkflow.domain;

import org.joda.time.DateTime;

public class DateTimeFieldValue extends DateTimeFieldValue_Base {

    public class DateTimeFieldValueBean extends FieldValueBean {
	private DateTime value;

	public DateTimeFieldValueBean(DateTime value) {
	    this.value = value;
	}

	public DateTime getDateTimeValue() {
	    return value;
	}

	public void setDateTimeValue(DateTime value) {
	    this.value = value;
	}
    }

    public DateTimeFieldValue() {
	super();
    }

    public DateTimeFieldValue(DateTimeMetaField metaField) {
	this();
	setMetaField(metaField);
    }

    @Override
    public FieldValueBean createFieldValueBean() {
	return new DateTimeFieldValueBean(getDateTimeValue());
    }

    @Override
    public void writeValueFromBean(FieldValueBean bean) {
	setDateTimeValue(((DateTimeFieldValueBean) bean).getDateTimeValue());
    }
}
