package module.metaWorkflow.domain;

import java.util.Set;
import java.util.TreeSet;

import jvstm.cps.ConsistencyPredicate;

public class FieldSetValue extends FieldSetValue_Base {

    protected FieldSetValue() {
	super();
    }

    public FieldSetValue(MetaFieldSet metaField) {
	this();
	setMetaField(metaField);
    }

    public FieldSetValue(WorkflowMetaProcess metaProcess, MetaFieldSet metaField) {
	this(metaField);
	setProcess(metaProcess);
    }

    public FieldSetValue(FieldSetValue parentFieldSet, MetaFieldSet metaField) {
	this(metaField);
	setParentFieldSet(parentFieldSet);
    }

    @Override
    @ConsistencyPredicate
    public final boolean checkHasParent() {
	return super.checkHasParent() || hasProcess();
    }

    public Set<FieldValue> getOrderedChildFields() {
	Set<FieldValue> orderedFields = new TreeSet<FieldValue>(FieldValue.COMPARATOR_BY_FIELD_ORDER);
	orderedFields.addAll(getChildFieldValues());
	return orderedFields;
    }

    public FieldValue findChildField(String OID) {
	for (FieldValue childField : getChildFieldValues()) {
	    if (childField.getExternalId().equals(OID)) {
		return childField;
	    }
	    if (childField instanceof FieldSetValue) {
		FieldValue possibleMatchField = ((FieldSetValue) childField).findChildField(OID);
		if (possibleMatchField != null) {
		    return possibleMatchField;
		}
	    }
	}
	return null;
    }

    @Override
    public boolean isFieldSet() {
	return true;
    }
}
