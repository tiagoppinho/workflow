/*
 * @(#)WorkflowProcessComment.java
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

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import myorg.domain.MyOrg;
import myorg.domain.User;
import myorg.domain.index.interfaces.Indexable;
import myorg.domain.index.interfaces.Searchable;

import org.joda.time.DateTime;

public class WorkflowProcessComment extends WorkflowProcessComment_Base implements Searchable {

    public final static Comparator<WorkflowProcessComment> COMPARATOR = new Comparator<WorkflowProcessComment>() {

	public int compare(WorkflowProcessComment o1, WorkflowProcessComment o2) {
	    return o1.getDate().compareTo(o2.getDate());
	}

    };

    public final static Comparator<WorkflowProcessComment> REVERSE_COMPARATOR = new Comparator<WorkflowProcessComment>() {

	public int compare(WorkflowProcessComment o1, WorkflowProcessComment o2) {
	    return -1 * o1.getDate().compareTo(o2.getDate());
	}

    };

    public WorkflowProcessComment(WorkflowProcess process, User commenter, String comment) {
	super();
	setComment(comment);
	setCommenter(commenter);
	setDate(new DateTime());
	setProcess(process);
	setMyOrg(MyOrg.getInstance());
    }

    public boolean isUnreadBy(User user) {
	return !getReaders().contains(user);
    }

    @Override
    public Set<Indexable> getObjectsToIndex() {
	return Collections.singleton((Indexable) getProcess());
    }
}
