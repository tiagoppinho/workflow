package module.metaWorkflow.domain;

import java.util.Comparator;

import jvstm.cps.ConsistencyPredicate;
import myorg.util.BundleUtil;
import pt.ist.fenixWebFramework.services.Service;
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

    @ConsistencyPredicate
    public boolean checkHasParent() {
	return hasParentFieldSet();
    }

    public String getLocalizedClassName() {
	return BundleUtil.getStringFromResourceBundle("resources/MetaWorkflowResources", "label." + getClass().getName());
    }

    @Service
    public static MetaField createMetaField(Class<? extends MetaField> fieldClass, MultiLanguageString name, int order,
	    MetaFieldSet parentFieldSet) {
	try {
	    MetaField newField = fieldClass.newInstance();
	    newField.setName(name);
	    newField.setFieldOrder(order);
	    newField.setParentFieldSet(parentFieldSet);
	    return newField;
	} catch (InstantiationException e) {
	    throw new RuntimeException("Unable to create instance of: " + fieldClass.getName(), e);
	} catch (IllegalAccessException e) {
	    throw new RuntimeException("Unable to create instance of: " + fieldClass.getName(), e);
	}

    }

    public abstract FieldValue createFieldValue();

    public abstract void delete();

}
