/*
 * @(#)ChangeRequestor.java
 *
 * Copyright 2009 Instituto Superior Tecnico
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

import module.metaWorkflow.domain.Requestor;
import module.metaWorkflow.domain.UserRequestor;
import module.metaWorkflow.domain.WorkflowMetaProcess;
import module.workflow.activities.UserInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;

/**
 * 
 * @author João Neves
 * @author João Antunes
 * @author Paulo Abrantes
 * 
 */
public class ChangeRequestor extends WorkflowActivity<WorkflowMetaProcess, UserInformation<WorkflowMetaProcess>> {

    @Override
    public boolean isActive(WorkflowMetaProcess process, User user) {
	return process.isUserAbleToAccessCurrentQueues(user) || process.isTakenByPerson(user);
    }

    @Override
    protected void process(UserInformation<WorkflowMetaProcess> activityInformation) {
	User user = activityInformation.getUser();
	Requestor requestor = user.getRequestor();
	if (requestor == null) {
	    requestor = new UserRequestor(user);
	}
	activityInformation.getProcess().setRequestor(requestor);
    }

    @Override
    public String getUsedBundle() {
	return "resources/MetaWorkflowResources";
    }

    @Override
    public UserInformation<WorkflowMetaProcess> getActivityInformation(WorkflowMetaProcess process) {
	return new UserInformation<WorkflowMetaProcess>(process, this);
    }

    @Override
    protected String[] getArgumentsDescription(UserInformation<WorkflowMetaProcess> activityInformation) {
	User user = activityInformation.getUser();
	Requestor currentRequestor = activityInformation.getProcess().getRequestor();
	return new String[] { currentRequestor != null ? currentRequestor.getName() : "", user.getPresentationName() };
    }

    @Override
    public boolean isUserAwarenessNeeded(WorkflowMetaProcess process, User user) {
	return false;
    }
}
