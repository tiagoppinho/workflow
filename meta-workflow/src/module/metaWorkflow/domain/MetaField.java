package module.metaWorkflow.domain;

import java.util.Collection;
import java.util.Comparator;

import jvstm.cps.ConsistencyPredicate;
import module.metaWorkflow.presentationTier.dto.MetaFieldBean;
import myorg.util.BundleUtil;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public abstract class MetaField extends MetaField_Base {

    public static Comparator<MetaField> COMPARATOR_BY_FIELD_ORDER = new Comparator<MetaField>() {
	@Override
	public int compare(MetaField field0, MetaField field1) {
	    int comparison = field0.getFieldOrder().compareTo(field1.getFieldOrder());
	    if (comparison != 0) {
		return comparison;
	    }
	    return field0.getExternalId().compareTo(field1.getExternalId());
	}
    };

    protected MetaField() {
	super();
	setOjbConcreteClass(getClass().getName());
    }

    protected void init(MultiLanguageString name, Integer order, MetaFieldSet parentFieldSet) {
	this.setName(name);
	this.setFieldOrder(order);
	this.setParentFieldSet(parentFieldSet);
    }

    @Service
    public static MetaField createMetaField(MetaFieldBean bean, MetaFieldSet parentFieldSet) {
	if (StringMetaField.class == bean.getFieldClass()) {
	    return new StringMetaField(bean, parentFieldSet);
	}
	if (StringsMetaField.class == bean.getFieldClass()) {
	    return new StringsMetaField(bean, parentFieldSet);
	}
	if (DateTimeMetaField.class == bean.getFieldClass()) {
	    return new DateTimeMetaField(bean, parentFieldSet);
	}
	if (LocalDateMetaField.class == bean.getFieldClass()) {
	    return new LocalDateMetaField(bean, parentFieldSet);
	}
	if (MetaFieldSet.class == bean.getFieldClass()) {
	    return new MetaFieldSet(bean, parentFieldSet);
	}

	throw new RuntimeException("Unknown MetaField: " + bean.getFieldClass().getName());
    }

    @ConsistencyPredicate
    public boolean checkHasParent() {
	return hasParentFieldSet();
    }

    @ConsistencyPredicate
    public boolean checkHasName() {
	Collection<Language> allLanguages = getName().getAllLanguages();

	for (Language language : allLanguages) {
	    String content = getName().getContent(language);
	    if (StringUtils.isEmpty(content)) {
		return false;
	    }
	}

	return true;
    }

    public String getLocalizedClassName() {
	return BundleUtil.getStringFromResourceBundle("resources/MetaWorkflowResources", "label." + getClass().getName());
    }

    /**
     * @return a new instance of the FieldValue that will contain the field's
     *         data
     */
    public abstract FieldValue createFieldValue();

    public abstract void delete();

}
