/*
 * @(#)EditFieldValueInfo.java
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
package module.metaWorkflow.activities;

import module.metaWorkflow.domain.FieldValue;
import module.metaWorkflow.domain.FieldValue.FieldValueBean;
import module.metaWorkflow.domain.WorkflowMetaProcess;
import module.workflow.activities.ActivityInformation;

/**
 * 
 * @author João Neves
 * 
 */
public class EditFieldValueInfo extends ActivityInformation<WorkflowMetaProcess> {

    private static final long serialVersionUID = 1L;
    private FieldValue field;
    private FieldValueBean fieldBean;

    public EditFieldValueInfo(WorkflowMetaProcess process, EditFieldValue activity) {
        super(process, activity);
    }

    public void setField(FieldValue fieldValue) {
        setFieldBean(fieldValue.createFieldValueBean());
    }

    public void setFieldBean(FieldValueBean fieldBean) {
        this.fieldBean = fieldBean;
    }

    public FieldValueBean getFieldBean() {
        return fieldBean;
    }

    @Override
    public boolean hasAllneededInfo() {
        return super.hasAllneededInfo() && isForwardedFromInput();
    }
}
