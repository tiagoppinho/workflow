/*
 * @(#)ActivityLog.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: João Figueiredo, Luis Cruz, Paulo Abrantes, Susana Fernandes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the MyOrg web application infrastructure.
 *
 *   MyOrg is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.*
 *
 *   MyOrg is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with MyOrg. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package module.workflow.domain;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.domain.VirtualHost;
import myorg.util.BundleUtil;
import pt.utl.ist.fenix.tools.util.Strings;

public class ActivityLog extends ActivityLog_Base {

    protected ActivityLog() {
	super();
    }

    public ActivityLog(WorkflowProcess process, User person, String operationName, String... argumentsDescription) {
	super();
	init(process, person, argumentsDescription);
	setOperation(operationName);
    }

    @Override
    public String getDescription() {
	WorkflowActivity<WorkflowProcess, ActivityInformation<WorkflowProcess>> activity = getProcess().getActivity(
		getOperation());

	Strings arguments = getDescriptionArguments();
	if (arguments != null && !arguments.isEmpty()) {
	    return BundleUtil.getFormattedStringFromResourceBundle(activity.getUsedBundle(), "label.description."
		    + activity.getClass().getName(), getDescriptionArguments().toArray(new String[] {}));
	} else {
	    return activity.getLocalizedName();
	}
    }

    @Override
    public boolean isConnectedToCurrentHost() {
	final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
	return virtualHost != null && getWorkflowSystem() == virtualHost.getWorkflowSystem();
    }

}
