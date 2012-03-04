/*
 * @(#)WorkflowMetaTypeDescription.java
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

import module.metaWorkflow.util.versioning.DiffUtil;
import module.metaWorkflow.util.versioning.DiffUtil.Revision;
import module.workflow.domain.WorkflowSystem;
import myorg.applicationTier.Authenticate.UserView;

import org.joda.time.DateTime;

/**
 * 
 * @author Paulo Abrantes
 * 
 */
public class WorkflowMetaTypeDescription extends WorkflowMetaTypeDescription_Base implements
	Comparable<WorkflowMetaTypeDescription> {

    public WorkflowMetaTypeDescription(WorkflowMetaType workflowMetaType, String description, int version) {
	super();
	super.setMetaType(workflowMetaType);
	super.setDescription(description);
	super.setDate(new DateTime());
	super.setVersion(version);
	super.setWorkflowSystem(WorkflowSystem.getInstance());
	super.setVersionOwner(UserView.getCurrentUser());
    }

    @Override
    public int compareTo(WorkflowMetaTypeDescription otherDescription) {
	return Integer.valueOf(getVersion()).compareTo(otherDescription.getVersion());
    }

    @Override
    public void setDescription(String description) {
	getMetaType().addDescription(description);
    }

    public Revision getDiffWithVersion(int version) {
	return getDiffWith(getMetaType().getDescriptionAtVersion(version - 1));
    }

    public Revision getDiffWith(WorkflowMetaTypeDescription description) {
	return DiffUtil.diff(getDescription(), description.getDescription());
    }
}
