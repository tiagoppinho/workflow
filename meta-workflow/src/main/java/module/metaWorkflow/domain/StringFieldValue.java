/*
 * @(#)StringFieldValue.java
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

/**
 * 
 * @author João Neves
 * 
 */
public class StringFieldValue extends StringFieldValue_Base {

    public class StringFieldValueBean extends FieldValueBean {
        private String value;

        public StringFieldValueBean(String value) {
            this.value = value;
        }

        public String getStringValue() {
            return value;
        }

        public void setStringValue(String value) {
            this.value = value;
        }
    }

    @Override
    public boolean isDefined() {
        return (getStringValue() != null) && (!getStringValue().isEmpty());
    }

    protected StringFieldValue() {
        super();
    }

    public StringFieldValue(StringMetaField metaField) {
        this();
        setMetaField(metaField);
    }

    public StringFieldValue(FieldSetValue parentFieldSet, StringMetaField metaField) {
        this(metaField);
        setParentFieldSet(parentFieldSet);
    }

    public StringFieldValue(String value, FieldSetValue parentFieldSet, StringMetaField metaField) {
        this(parentFieldSet, metaField);
        setStringValue(value);
    }

    @Override
    public StringFieldValueBean createFieldValueBean() {
        return new StringFieldValueBean(getStringValue());
    }

    @Override
    public void writeValueFromBean(FieldValueBean bean) {
        setStringValue(((StringFieldValueBean) bean).getStringValue());
    }
}
