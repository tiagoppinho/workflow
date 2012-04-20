/*
 * @(#)StringsFieldValue.java
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

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.Strings;

/**
 * 
 * @author João Neves
 * @author Anil Kassamali
 * 
 */
public class StringsFieldValue extends StringsFieldValue_Base {

    public class StringsFieldValueBean extends FieldValueBean {
	private Strings value;

	public StringsFieldValueBean(Strings value) {
	    this.value = value;
	}

	public Strings getStringsValue() {
	    return value;
	}

	public void setStringsValue(Strings value) {
	    this.value = value;
	}
    }

    @Override
    public boolean isDefined() {
	if ((getStringsValue() == null) && (getStringsValue().size() == 0)) {
	    return false;
	}
	for (String string : getStringsValue().getUnmodifiableList()) {
	    if (!StringUtils.isEmpty(string)) {
		return true;
	    }
	}
	return false;
    }

    public StringsFieldValue() {
	super();
    }

    public StringsFieldValue(StringsMetaField metaField) {
	this();
	setMetaField(metaField);
    }

    public StringsFieldValue(StringsMetaField metaField, FieldSetValue parent, Strings value) {
	this(metaField);
	setParentFieldSet(parent);
	setStringsValue(value);
    }

    @Override
    public FieldValueBean createFieldValueBean() {
	return new StringsFieldValueBean(getStringsValue());
    }

    @Override
    public void writeValueFromBean(FieldValueBean bean) {
	setStringsValue(((StringsFieldValueBean) bean).getStringsValue());
    }
}
