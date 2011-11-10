package module.metaWorkflow.domain;


public class StringsFieldValue extends StringsFieldValue_Base {

    public StringsFieldValue() {
	super();
    }

    public StringsFieldValue(StringsMetaField metaField) {
	this();
	setMetaField(metaField);
    }
}
