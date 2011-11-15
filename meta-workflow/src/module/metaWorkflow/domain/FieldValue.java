package module.metaWorkflow.domain;

import java.io.Serializable;
import java.util.Comparator;

import jvstm.cps.ConsistencyPredicate;

public abstract class FieldValue extends FieldValue_Base {

    public abstract class FieldValueBean implements Serializable {
	private static final long serialVersionUID = 1L;

	public void writeValueToField() {
	    writeValueFromBean(this);
	}

	public Class<? extends FieldValue> getFieldClass() {
	    return FieldValue.this.getClass();
	}

	public String getFieldName() {
	    return getMetaField().getName().getContent();
	}
    }

    public static Comparator<FieldValue> COMPARATOR_BY_FIELD_ORDER = new Comparator<FieldValue>() {
	@Override
	public int compare(FieldValue field0, FieldValue field1) {
	    return MetaField.COMPARATOR_BY_FIELD_ORDER.compare(field0.getMetaField(), field1.getMetaField());
	}
    };

    protected FieldValue() {
	super();
	setOjbConcreteClass(getClass().getName());
    }

    @ConsistencyPredicate
    public boolean checkHasParent() {
	return hasParentFieldSet();
    }

    public boolean isFieldSet() {
	return false;
    }

    abstract public FieldValueBean createFieldValueBean();

    abstract public void writeValueFromBean(FieldValueBean bean);
}
