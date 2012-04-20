/*
 * @(#)LocalDateFieldValue.java
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

import org.joda.time.LocalDate;

/**
 * 
 * @author João Neves
 * @author Anil Kassamali
 * 
 */
public class LocalDateFieldValue extends LocalDateFieldValue_Base {

    public class LocalDateFieldValueBean extends FieldValueBean {
	private LocalDate value;

	public LocalDateFieldValueBean(LocalDate value) {
	    this.value = value;
	}

	public LocalDate getLocalDateValue() {
	    return value;
	}

	public void setLocalDateValue(LocalDate value) {
	    this.value = value;
	}
    }

    @Override
    public boolean isDefined() {
	return getLocalDateValue() != null;
    }

    public LocalDateFieldValue() {
	super();
    }

    public LocalDateFieldValue(LocalDateMetaField metaField) {
	this();
	setMetaField(metaField);
    }

    public LocalDateFieldValue(LocalDateMetaField metaField, FieldSetValue parentFieldSet, LocalDate value) {
	this(metaField);
	setParentFieldSet(parentFieldSet);
	setLocalDateValue(value);
    }

    @Override
    public FieldValueBean createFieldValueBean() {
	return new LocalDateFieldValueBean(getLocalDateValue());
    }

    @Override
    public void writeValueFromBean(FieldValueBean bean) {
	setLocalDateValue(((LocalDateFieldValueBean) bean).getLocalDateValue());
    }
}
