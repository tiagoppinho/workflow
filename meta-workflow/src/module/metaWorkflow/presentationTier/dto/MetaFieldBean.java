package module.metaWorkflow.presentationTier.dto;

import java.io.Serializable;

import module.metaWorkflow.domain.DateTimeMetaField;
import module.metaWorkflow.domain.LocalDateMetaField;
import module.metaWorkflow.domain.MetaField;
import module.metaWorkflow.domain.MetaFieldSet;
import module.metaWorkflow.domain.StringMetaField;
import pt.ist.fenixWebFramework.services.Service;
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

    @Service
    public MetaField createMetaField(MetaFieldSet parentFieldSet) {
	if (StringMetaField.class == getFieldClass()) {
	    return new StringMetaField(this, parentFieldSet);
	} else if (DateTimeMetaField.class == getFieldClass()) {
	    return new DateTimeMetaField(this, parentFieldSet);
	} else if (LocalDateMetaField.class == getFieldClass()) {
	    return new LocalDateMetaField(this, parentFieldSet);
	} else if (MetaFieldSet.class == getFieldClass()) {
	    return new MetaFieldSet(this, parentFieldSet);
	}

	throw new RuntimeException("i dont know this this " + getFieldClass().getName());
    }

}
