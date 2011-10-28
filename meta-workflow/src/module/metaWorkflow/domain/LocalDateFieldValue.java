package module.metaWorkflow.domain;

public class LocalDateFieldValue extends LocalDateFieldValue_Base {

    public LocalDateFieldValue() {
	super();
    }

    public LocalDateFieldValue(LocalDateMetaField metaField) {
	this();
	setMetaField(metaField);
    }

    @Override
    public String getValueSlotName() {
	return "localDateValue";
    }

}
