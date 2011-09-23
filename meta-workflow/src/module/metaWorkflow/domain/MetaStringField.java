package module.metaWorkflow.domain;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class MetaStringField extends MetaStringField_Base {

    protected MetaStringField() {
	super();
    }

    public MetaStringField(MultiLanguageString name, int order, MetaFieldSet parentFieldSet) {
	this();
	setName(name);
	setFieldOrder(order);
	setParentFieldSet(parentFieldSet);
    }

    @Override
    public StringFieldValue createFieldValue() {
	return new StringFieldValue(this);
    }

    @Override
    @Service
    public void delete() {
	removeParentFieldSet();
	if (!hasAnyFieldValues()) {
	    deleteDomainObject();
	}
    }
}
