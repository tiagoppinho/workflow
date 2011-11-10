package module.metaWorkflow.domain;

import module.metaWorkflow.presentationTier.dto.MetaFieldBean;

public class StringsMetaField extends StringsMetaField_Base {

    public StringsMetaField() {
	super();
    }

    public StringsMetaField(MetaFieldBean bean, MetaFieldSet parentFieldSet) {
	setName(bean.getName());
	setFieldOrder(bean.getOrder());
	setParentFieldSet(parentFieldSet);
    }

    @Override
    public FieldValue createFieldValue() {
	return new StringsFieldValue(this);
    }

    @Override
    public void delete() {
    }
}
