/*
 * @(#)WorkflowLog.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Case Handleing Based Workflow Module.
 *
 *   The Case Handleing Based Workflow Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Workflow Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Workflow Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.workflow.domain;

import java.util.Comparator;

import myorg.domain.User;
import myorg.domain.VirtualHost;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import pt.utl.ist.fenix.tools.util.Strings;

/**
 * 
 * @author Diogo Figueiredo
 * @author João Antunes
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public abstract class WorkflowLog extends WorkflowLog_Base {
    public static final Comparator<WorkflowLog> COMPARATOR_BY_WHEN = new Comparator<WorkflowLog>() {

	@Override
	public int compare(final WorkflowLog log1, final WorkflowLog log2) {
	    final DateTime when1 = log1.getWhenOperationWasRan();
	    final DateTime when2 = log2.getWhenOperationWasRan();
	    final int result = when1.compareTo(when2);
	    return result == 0 ? log1.getExternalId().compareTo(log2.getExternalId()) : result;
	}

    };

    public static final Comparator<WorkflowLog> COMPARATOR_BY_WHEN_REVERSED = new Comparator<WorkflowLog>() {

	@Override
	public int compare(final WorkflowLog log1, final WorkflowLog log2) {
	    return COMPARATOR_BY_WHEN.compare(log2, log1);
	}

    };

    public WorkflowLog() {
	super.setWorkflowSystem(WorkflowSystem.getInstance());
	super.setOjbConcreteClass(this.getClass().getName());
	super.setWhenOperationWasRan(new DateTime());
    }

    public void init(String... argumentsDescription) {
	if (argumentsDescription != null) {
	    super.setDescriptionArguments(new Strings(argumentsDescription));
	}
    }

    public void init(WorkflowProcess process, User person, String... argumentsDescription) {
	super.setProcess(process);
	super.setActivityExecutor(person);
	if (argumentsDescription != null) {
	    super.setDescriptionArguments(new Strings(argumentsDescription));
	}
    }

    public void updateWhenOperationWasRan() {
	super.setWhenOperationWasRan(new DateTime());
    }

    public abstract String getDescription();

    public WorkflowLog getPrevious() {
	final WorkflowProcess workflowProcess = getProcess();
	final DateTime when = getWhenOperationWasRan();
	WorkflowLog previous = null;
	for (final WorkflowLog workflowLog : workflowProcess.getExecutionLogsSet()) {
	    if (workflowLog.isBefore(this) && (previous == null || previous.isBefore(workflowLog))) {
		previous = workflowLog;
	    }
	}
	return previous;
    }

    public boolean isBefore(final WorkflowLog workflowLog) {
	final DateTime when = getWhenOperationWasRan();
	return when.isBefore(workflowLog.getWhenOperationWasRan());
    }

    public Duration getDurationFromPreviousLog() {
	final WorkflowLog previous = getPrevious();
	return previous == null ? null : calculateDuration(previous, this);
    }

    private static Duration calculateDuration(final WorkflowLog previous, final WorkflowLog next) {
	return new Duration(previous.getWhenOperationWasRan(), next.getWhenOperationWasRan());
    }

    @Override
    public boolean isConnectedToCurrentHost() {
	final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
	return virtualHost != null && getWorkflowSystem() == virtualHost.getWorkflowSystem();
    }

}
