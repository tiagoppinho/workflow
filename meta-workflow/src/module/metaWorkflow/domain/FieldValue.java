package module.metaWorkflow.domain;

import java.util.Comparator;

import jvstm.cps.ConsistencyPredicate;

public abstract class FieldValue extends FieldValue_Base {

    public static Comparator<FieldValue> COMPARATOR_BY_FIELD_ORDER = new Comparator<FieldValue>() {
	@Override
	public int compare(FieldValue field0, FieldValue field1) {
	    return MetaField.COMPARATOR_BY_FIELD_ORDER.compare(field0.getMetaField(), field1.getMetaField());
	}
    };

    protected FieldValue() {
	super();
    }

    @ConsistencyPredicate
    public boolean checkHasParent() {
	return hasParentFieldSet();
    }

    public abstract String getSchemaName();

    public abstract String getValueSlotName();
}
