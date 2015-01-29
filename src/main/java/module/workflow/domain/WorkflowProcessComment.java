/*
 * @(#)WorkflowProcessComment.java
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

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

/**
 * 
 * @author Anil Kassamali
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class WorkflowProcessComment extends WorkflowProcessComment_Base {

    public final static Comparator<WorkflowProcessComment> COMPARATOR = (o1, o2) -> o1.getDate().compareTo(o2.getDate());

    public final static Comparator<WorkflowProcessComment> REVERSE_COMPARATOR = COMPARATOR.reversed();

    public WorkflowProcessComment(WorkflowProcess process, User commenter, String comment) {
        super();
        setComment(comment);
        setCommenter(commenter);
        setDate(new DateTime());
        setProcess(process);
        setWorkflowSystem(WorkflowSystem.getInstance());
    }

    public boolean isUnreadBy(User user) {
        return !getReadersSet().contains(user);
    }

}
