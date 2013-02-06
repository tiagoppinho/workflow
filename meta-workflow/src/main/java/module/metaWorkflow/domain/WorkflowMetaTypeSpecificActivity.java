/*
 * @(#)WorkflowMetaTypeSpecificActivity.java
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
package module.metaWorkflow.domain;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;

/**
 * 
 * @author Paulo Abrantes
 * 
 */
public class WorkflowMetaTypeSpecificActivity extends WorkflowMetaTypeSpecificActivity_Base {

    public WorkflowMetaTypeSpecificActivity(WorkflowMetaType type, Class<? extends WorkflowActivity> activity) {
	super();
	setMetaType(type);
	setActivityClass(activity.getClass().getName());
    }

    public <T extends WorkflowProcess> WorkflowActivity<T, ActivityInformation<T>> getActivity() {
	try {
	    return (WorkflowActivity<T, ActivityInformation<T>>) Class.forName(getActivityClass()).newInstance();
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }
}
