/*
 * @(#)FieldSetValue.java
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

import java.util.Set;
import java.util.TreeSet;

import jvstm.cps.ConsistencyPredicate;

/**
 * 
 * @author João Neves
 * @author Anil Kassamali
 * 
 */
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

    @Override
    public FieldValueBean createFieldValueBean() {
	return null;
    }

    @Override
    public void writeValueFromBean(FieldValueBean bean) {
    }
}
