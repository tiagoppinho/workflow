package module.metaWorkflow.domain;

import java.util.Set;
import java.util.TreeSet;

import jvstm.cps.ConsistencyPredicate;
import myorg.util.BundleUtil;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class MetaFieldSet extends MetaFieldSet_Base {

    protected MetaFieldSet() {
	super();
    }

    protected MetaFieldSet(MultiLanguageString name, int order) {
	this();
	setName(name);
	setFieldOrder(order);
    }

    public MetaFieldSet(MultiLanguageString name, int order, WorkflowMetaType metaType) {
	this(name, order);
	setMetaType(metaType);
    }

    public MetaFieldSet(MultiLanguageString name, int order, MetaFieldSet parentFieldSet) {
	this(name, order);
	setParentFieldSet(parentFieldSet);
    }

    @Override
    @ConsistencyPredicate
    public final boolean checkHasParent() {
	return super.checkHasParent() || hasMetaType();
    }

    public Set<MetaField> getOrderedChildFields() {
	Set<MetaField> orderedFields = new TreeSet<MetaField>(MetaField.COMPARATOR_BY_FIELD_ORDER);
	orderedFields.addAll(getChildFields());
	return orderedFields;
    }

    @Override
    public FieldSetValue createFieldValue() {
	FieldSetValue rootSet = new FieldSetValue(this);
	for (MetaField childField : getChildFields()) {
	    rootSet.addChildFieldValues(childField.createFieldValue());
	}
	return rootSet;
    }

    @Override
    @Service
    public void delete() {
	if (hasMetaType()) {
	    throw new Error("Cannot delete the root MetaFieldSet");
	}
	if (hasAnyChildFields()) {
	    throw new RuntimeException(BundleUtil.getStringFromResourceBundle("resources/MetaWorkflowResources",
		    "label.error.cannotDelete.FieldSet.before.childFields"));
	}

	removeParentFieldSet();
	if (!hasAnyFieldValues()) {
	    deleteDomainObject();
	}
    }
}
