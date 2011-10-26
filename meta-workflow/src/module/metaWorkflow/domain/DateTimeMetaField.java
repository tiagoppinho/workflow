package module.metaWorkflow.domain;

import module.metaWorkflow.presentationTier.dto.MetaFieldBean;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class DateTimeMetaField extends DateTimeMetaField_Base {

    protected DateTimeMetaField() {
	super();
    }

    public DateTimeMetaField(MultiLanguageString name, Integer order, MetaFieldSet parentFieldSet) {
	this();
	init(name, order, parentFieldSet);
    }

    public DateTimeMetaField(final MetaFieldBean bean, MetaFieldSet parentFieldSet) {
	this(bean.getName(), bean.getOrder(), parentFieldSet);
    }

    @Override
    public FieldValue createFieldValue() {
	return new DateTimeFieldValue(this);
    }

    @Override
    public void delete() {
	removeParentFieldSet();
	if (!hasAnyFieldValues()) {
	    deleteDomainObject();
	}
    }

}
