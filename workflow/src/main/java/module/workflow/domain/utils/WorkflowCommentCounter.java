/*
 * @(#)WorkflowCommentCounter.java
 *
 * Copyright 2011 Instituto Superior Tecnico
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
package module.workflow.domain.utils;

import java.util.HashSet;
import java.util.Set;

import module.organization.domain.Person;
import module.workflow.domain.WorkflowLog;
import module.workflow.domain.WorkflowProcess;
import module.workflow.widgets.UnreadCommentsWidget;

import org.apache.commons.collections.Predicate;

/**
 * 
 * Class used to count unread comments on WorkflowProcess objects given
 * at the constructor {@link WorkflowProcess}. Used by the {@link UnreadCommentsWidget}
 * 
 * 
 * @author João Antunes
 * 
 */
public class WorkflowCommentCounter {

    Class classToFilter;

    public Class getClassToFilter() {
        return classToFilter;
    }

    public void setClassToFilter(Class classToFilter) {
        this.classToFilter = classToFilter;
    }

    public WorkflowCommentCounter(Class workflowClass) {
        Class<WorkflowProcess> workflowProcessClass = WorkflowProcess.class;
        if (!workflowProcessClass.isAssignableFrom(workflowClass)) {
            throw new IllegalArgumentException("Wrong class type provided to the WorkflowCommentCounter");
        }
        this.classToFilter = workflowClass;
    }

    /**
     * @return the processes of unread comments for the given person for the
     *         given classToFilter type of process NOTE: It relies on the logs
     *         of the user to retrieve the comments (as it should be more
     *         efficient and there is a direct relation between a comment and a
     *         log)
     */
    public Set<WorkflowProcess> getProcessesWithUnreadComments(final Person person, final String className) {

        Set<WorkflowProcess> processes = new HashSet<WorkflowProcess>();
        Predicate searchPredicate = new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                if (className != null && classToFilter.toString().contentEquals(className)) {
                    return classToFilter.isAssignableFrom(arg0.getClass())
                            && ((WorkflowProcess) arg0).hasUnreadCommentsForUser(person.getUser());
                } else if (className != null) {
                    return false;
                }
                return classToFilter.isAssignableFrom(arg0.getClass())
                        && ((WorkflowProcess) arg0).hasUnreadCommentsForUser(person.getUser());
            }
        };

        for (WorkflowLog log : person.getUser().getUserLogsSet()) {
            WorkflowProcess process = log.getProcess();
            if (searchPredicate.evaluate(process)) {
                processes.add(process);
            }
        }
        return processes;

    }

}
