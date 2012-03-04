/*
 * @(#)EditFieldValue.java
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

import module.metaWorkflow.domain.WorkflowMetaProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;

/**
 * 
 * @author João Antunes
 * @author João Neves
 * 
 */
public class EditFieldValue extends WorkflowActivity<WorkflowMetaProcess, ActivityInformation<WorkflowMetaProcess>> {

    @Override
    public String getUsedBundle() {
	return "resources/MetaWorkflowResources";
    }

    @Override
    public boolean isActive(WorkflowMetaProcess process, User user) {
	return process.isUserAbleToAccessCurrentQueues(user) || process.isTakenByPerson(user);
    }

    @Override
    public ActivityInformation<WorkflowMetaProcess> getActivityInformation(WorkflowMetaProcess process) {
	return new EditFieldValueInfo(process, this);
    }

    @Override
    public boolean isUserAwarenessNeeded(WorkflowMetaProcess process) {
	return false;
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
	return false;
    }

    @Override
    public boolean isVisible() {
	return false;
    }

    @Override
    protected void process(ActivityInformation<WorkflowMetaProcess> activityInformation) {
	EditFieldValueInfo fieldInfo = ((EditFieldValueInfo) activityInformation);
	fieldInfo.getFieldBean().writeValueToField();
    }

    @Override
    protected String[] getArgumentsDescription(ActivityInformation<WorkflowMetaProcess> activityInformation) {
	String fieldName = ((EditFieldValueInfo) activityInformation).getFieldBean().getFieldName();
	return new String[] { fieldName };
    }
}
