package module.metaWorkflow.presentationTier.dto;

import java.io.Serializable;

import module.metaWorkflow.domain.MetaField;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class MetaFieldBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Class<? extends MetaField> fieldClass;

    private MultiLanguageString name;

    private int order;

    public void setFieldClass(Class<? extends MetaField> fieldClass) {
	this.fieldClass = fieldClass;
    }

    public Class<? extends MetaField> getFieldClass() {
	return fieldClass;
    }

    public void setName(MultiLanguageString name) {
	this.name = name;
    }

    public MultiLanguageString getName() {
	return name;
    }

    public void setOrder(int order) {
	this.order = order;
    }

    public int getOrder() {
	return order;
    }

}
