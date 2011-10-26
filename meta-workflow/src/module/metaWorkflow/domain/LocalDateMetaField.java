package module.metaWorkflow.domain;

import module.metaWorkflow.presentationTier.dto.MetaFieldBean;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class LocalDateMetaField extends LocalDateMetaField_Base {

    protected LocalDateMetaField() {
	super();
    }

    public LocalDateMetaField(MultiLanguageString name, Integer order, MetaFieldSet parentFieldSet) {
	this();
	init(name, order, parentFieldSet);
    }

    public LocalDateMetaField(MetaFieldBean bean, MetaFieldSet parentFieldSet) {
	this(bean.getName(), bean.getOrder(), parentFieldSet);
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
