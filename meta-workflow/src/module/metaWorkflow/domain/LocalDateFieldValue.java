package module.metaWorkflow.domain;

import org.joda.time.LocalDate;

public class LocalDateFieldValue extends LocalDateFieldValue_Base {

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
}
