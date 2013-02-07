/*
 * @(#)DateTimeFieldValue.java
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

import org.joda.time.DateTime;

/**
 * 
 * @author João Neves
 * 
 */
public class DateTimeFieldValue extends DateTimeFieldValue_Base {

    public class DateTimeFieldValueBean extends FieldValueBean {
        private DateTime value;

        public DateTimeFieldValueBean(DateTime value) {
            this.value = value;
        }

        public DateTime getDateTimeValue() {
            return value;
        }

        public void setDateTimeValue(DateTime value) {
            this.value = value;
        }
    }

    @Override
    public boolean isDefined() {
        return getDateTimeValue() != null;
    }

    public DateTimeFieldValue() {
        super();
    }

    public DateTimeFieldValue(DateTimeMetaField metaField) {
        this();
        setMetaField(metaField);
    }

    @Override
    public FieldValueBean createFieldValueBean() {
        return new DateTimeFieldValueBean(getDateTimeValue());
    }

    @Override
    public void writeValueFromBean(FieldValueBean bean) {
        setDateTimeValue(((DateTimeFieldValueBean) bean).getDateTimeValue());
    }
}
