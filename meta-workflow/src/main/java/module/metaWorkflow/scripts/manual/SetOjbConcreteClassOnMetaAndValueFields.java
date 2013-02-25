/*
 * @(#)SetOjbConcreteClassOnMetaAndValueFields.java
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
package module.metaWorkflow.scripts.manual;

import java.util.Set;

import module.metaWorkflow.domain.FieldValue;
import module.metaWorkflow.domain.MetaField;
import module.metaWorkflow.domain.MetaFieldSet;
import module.metaWorkflow.domain.WorkflowMetaType;
import module.workflow.domain.WorkflowSystem;
import pt.ist.bennu.core.domain.scheduler.WriteCustomTask;

/**
 * 
 * @author Anil Kassamali
 * 
 */
public class SetOjbConcreteClassOnMetaAndValueFields extends WriteCustomTask {

    @Override
    public void doService() {
        WorkflowSystem system = WorkflowSystem.getInstance();

        Set<WorkflowMetaType> metaTypes = system.getMetaTypes();

        for (WorkflowMetaType workflowMetaType : metaTypes) {
            MetaFieldSet fieldSet = workflowMetaType.getFieldSet();
            setOjbConcteteClassOnMetaField(fieldSet);
        }
    }

    void setOjbConcteteClassOnMetaField(final MetaField metaField) {
        metaField.setOjbConcreteClass(metaField.getClass().getName());

        Set<FieldValue> fieldValues = metaField.getFieldValues();
        for (FieldValue fieldValue : fieldValues) {
            fieldValue.setOjbConcreteClass(fieldValue.getClass().getName());
        }

        if (metaField instanceof MetaFieldSet) {
            MetaFieldSet metaFieldSet = (MetaFieldSet) metaField;

            Set<MetaField> childFields = metaFieldSet.getChildFields();
            for (MetaField child : childFields) {
                setOjbConcteteClassOnMetaField(child);
            }
        }
    }

}
