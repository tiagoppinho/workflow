package module.metaWorkflow.domain;

public class LocalDateMetaField extends LocalDateMetaField_Base {

    public LocalDateMetaField() {
	super();
    }

    @Override
    public FieldValue createFieldValue() {
	return new LocalDateFieldValue(this);
    }

    @Override
    public void delete() {
	removeParentFieldSet();
	if (!hasAnyFieldValues()) {
	    deleteDomainObject();
	}
    }
}
