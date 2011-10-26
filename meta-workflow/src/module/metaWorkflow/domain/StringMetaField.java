package module.metaWorkflow.domain;

import module.metaWorkflow.presentationTier.dto.MetaFieldBean;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class StringMetaField extends StringMetaField_Base {

    public StringMetaField() {
	super();
    }

    public StringMetaField(MultiLanguageString name, int order, MetaFieldSet parentFieldSet) {
	this();
	init(name, order, parentFieldSet);
    }

    public StringMetaField(MetaFieldBean bean, MetaFieldSet parentFieldSet) {
	this(bean.getName(), bean.getOrder(), parentFieldSet);
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
