/*
 * @(#)SearchMetaWorkflowProcess.java
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
package module.metaWorkflow.domain.search;

import java.util.Collection;
import java.util.Set;

import module.metaWorkflow.domain.WorkflowMetaProcess;
import module.metaWorkflow.domain.WorkflowMetaType;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowQueue;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import myorg.domain.util.Search;

import org.apache.commons.collections.Predicate;

/**
 * 
 * @author João Neves
 * @author Paulo Abrantes
 * 
 */
public class SearchMetaWorkflowProcess extends Search<WorkflowMetaProcess> {

    private User requestor;
    private User creator;
    private WorkflowMetaType metaType;
    private WorkflowQueue queue;

    public User getRequestor() {
	return requestor;
    }

    public void setRequestor(User requestor) {
	this.requestor = requestor;
    }

    public User getCreator() {
	return creator;
    }

    public void setCreator(User creator) {
	this.creator = creator;
    }

    public WorkflowMetaType getMetaType() {
	return metaType;
    }

    public void setMetaType(WorkflowMetaType metaType) {
	this.metaType = metaType;
    }

    public WorkflowQueue getQueue() {
	return queue;
    }

    public void setQueue(WorkflowQueue queue) {
	this.queue = queue;
    }

    public SearchMetaWorkflowProcess() {
	setRequestor(null);
	setCreator(null);
	setMetaType(null);
	setQueue(null);
    }

    @Override
    public Set<WorkflowMetaProcess> search() {
	try {
	    return new WorkflowMetaProcessSearchResult(getProcesses());
	} catch (Exception ex) {
	    ex.printStackTrace();
	    throw new Error(ex);
	}

    }

    private Collection<WorkflowMetaProcess> getProcesses() {
	final User currentUser = UserView.getCurrentUser();
	return WorkflowProcess.getAllProcesses(WorkflowMetaProcess.class, new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		return ((WorkflowMetaProcess) arg0).isAccessible(currentUser);
	    }

	});

    }

    private class WorkflowMetaProcessSearchResult extends SearchResultSet<WorkflowMetaProcess> {

	public WorkflowMetaProcessSearchResult(Collection<WorkflowMetaProcess> c) {
	    super(c);
	}

	@Override
	protected boolean matchesSearchCriteria(WorkflowMetaProcess process) {
	    return matchCriteria(getCreator(), process.getCreator())
		    && matchCriteria(getRequestor(), process.getRequestor().getUser())
		    && matchCriteria(getMetaType(), process.getMetaType()) && matchesAnyQueue(queue, process.getCurrentQueues());
	}

	private boolean matchesAnyQueue(WorkflowQueue queue, Collection<WorkflowQueue> queues) {
	    return (queue == null) || queues.contains(getQueue());
	}
    }
}
