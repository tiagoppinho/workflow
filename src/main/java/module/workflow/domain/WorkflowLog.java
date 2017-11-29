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
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.bennu.core.domain.AuthenticationContext;
import org.fenixedu.bennu.core.domain.AuthenticationContext.AuthenticationMethodEvent;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
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

    public static final Comparator<WorkflowLog> COMPARATOR_BY_WHEN = (log1, log2) -> {
        final DateTime when1 = log1.getWhenOperationWasRan();
        final DateTime when2 = log2.getWhenOperationWasRan();
        final int result = when1.compareTo(when2);
        return result == 0 ? log1.getExternalId().compareTo(log2.getExternalId()) : result;
    };

    public static final Comparator<WorkflowLog> COMPARATOR_BY_WHEN_REVERSED =
            (log1, log2) -> COMPARATOR_BY_WHEN.compare(log2, log1);

    public WorkflowLog() {
        super.setWorkflowSystem(WorkflowSystem.getInstance());
        super.setWhenOperationWasRan(new DateTime());
    }

    public void init(String... argumentsDescription) {
        if (argumentsDescription != null) {
            super.setDescriptionArguments(new Strings(argumentsDescription));
        }
    }

    public void init(WorkflowProcess process, String... argumentsDescription) {
        super.setProcess(process);
        final AuthenticationContext context = Authenticate.getAuthenticationContext();
        final User user = context.getUser();
        final String authenticationMethods = toString(context.getAuthenticationMethodEvents());
        super.setActivityExecutor(user);
        super.setAuthenticationMethods(authenticationMethods);
        if (argumentsDescription != null) {
            super.setDescriptionArguments(new Strings(argumentsDescription));
        }
    }

    private String toString(final AuthenticationMethodEvent[] authenticationMethodEvents) {
        if (authenticationMethodEvents == null || authenticationMethodEvents.length == 0) {
            return null;
        }
        final StringBuilder b = new StringBuilder();
        for (final AuthenticationMethodEvent event : authenticationMethodEvents) {
            if (b.length() > 0) {
                b.append(", ");
            }
            b.append(event.getAuthenticationMethod());
        }
        return b.toString();
    }

    public void updateWhenOperationWasRan() {
        super.setWhenOperationWasRan(new DateTime());
    }

    public abstract String getDescription();

    public WorkflowLog getPrevious() {
        final WorkflowProcess workflowProcess = getProcess();
        return workflowProcess.getExecutionLogStream().filter(l -> l.isBefore(this)).max(COMPARATOR_BY_WHEN).orElse(null);
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

    public void delete() {
        setActivityExecutor(null);
        setProcess(null);
        setWorkflowSystem(null);
        deleteDomainObject();

    }

    public SortedSet<String> getUsedAuthenticationMethods() {
        final SortedSet<String> set = new TreeSet<>();
        final String methods = getAuthenticationMethods();
        if (methods != null && methods.length() > 0) {
            for (final String method : methods.split(", ")) {
                set.add(method);
            }
        }
        return set;
    }

}
