/*
 * @(#)FieldValue.java
 *
 * Copyright 2011 Instituto Superior Tecnico
 * Founding Authors: Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Meta-Workflow Module.
 *
 *   The Meta-Workflow Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Meta-Workflow Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Meta-Workflow Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.metaWorkflow.domain;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import jvstm.cps.ConsistencyPredicate;

/**
 * 
 * @author João Neves
 * @author Anil Kassamali
 * 
 */
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
    }

    @ConsistencyPredicate
    public boolean checkHasParent() {
	return hasParentFieldSet();
    }

    public boolean isFieldSet() {
	return false;
    }

    public WorkflowMetaProcess getProcess() {
	return getParentFieldSet().getProcess();
    }

    public Set<FieldValue> getAllFields() {
	Set<FieldValue> fieldSet = new HashSet<FieldValue>();
	fieldSet.add(this);
	return fieldSet;
    }

    abstract public boolean isDefined();

    abstract public FieldValueBean createFieldValueBean();

    abstract public void writeValueFromBean(FieldValueBean bean);
}
