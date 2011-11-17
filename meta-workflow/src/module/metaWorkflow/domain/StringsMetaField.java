package module.metaWorkflow.domain;

import module.metaWorkflow.presentationTier.dto.MetaFieldBean;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class StringsMetaField extends StringsMetaField_Base {

    public StringsMetaField() {
	super();
    }

    public StringsMetaField(MultiLanguageString name, Integer order, MetaFieldSet parentFieldSet) {
	setName(name);
	setFieldOrder(order);
	setParentFieldSet(parentFieldSet);
    }

    public StringsMetaField(MetaFieldBean bean, MetaFieldSet parentFieldSet) {
	this(bean.getName(), bean.getOrder(), parentFieldSet);
    }

    @Override
    public FieldValue createFieldValue() {
	return new StringsFieldValue(this);
    }

    @Override
    public void delete() {
    }
}
