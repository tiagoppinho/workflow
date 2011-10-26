package module.metaWorkflow.domain;

import java.util.Collection;
import java.util.Comparator;

import jvstm.cps.ConsistencyPredicate;
import myorg.util.BundleUtil;

import org.apache.commons.lang.StringUtils;

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
    }

    protected void init(MultiLanguageString name, Integer order, MetaFieldSet parentFieldSet) {
	this.setName(name);
	this.setFieldOrder(order);
	this.setParentFieldSet(parentFieldSet);
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

    public abstract FieldValue createFieldValue();

    public abstract void delete();

}
