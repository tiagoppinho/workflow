/*
 * @(#)CommentBean.java
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
package module.workflow.presentationTier.actions;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import module.workflow.domain.WorkflowProcess;
import pt.ist.bennu.core.domain.User;

/**
 * 
 * @author João Antunes
 * @author Paulo Abrantes
 * 
 */
public class CommentBean implements Serializable {

    private WorkflowProcess process;
    private String comment;
    private List<UserNotificationBean> peopleToNotify;
    private List<QueueNotificationBean> queuesToNotify;

    public CommentBean(WorkflowProcess process) {
        this.process = process;
        this.peopleToNotify = Collections.emptyList();
        this.queuesToNotify = Collections.emptyList();
    }

    public WorkflowProcess getProcess() {
        return process;
    }

    public void setProcess(WorkflowProcess process) {
        this.process = process;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<User> getUsersToNotify() {
        Set<User> users = new HashSet<User>();
        for (UserNotificationBean bean : this.peopleToNotify) {
            if (bean.isAbleToNotify()) {
                users.add(bean.getUser());
            }
        }
        for (QueueNotificationBean bean : this.getQueuesToNotify()) {
            if (bean.isAbleToNotify()) {
                users.addAll(bean.getUsers());
            }
        }
        return users;
    }

    public List<UserNotificationBean> getPeopleToNotify() {
        return this.peopleToNotify;
    }

    public void setPeopleToNotify(List<UserNotificationBean> peopleToNotify) {
        this.peopleToNotify = peopleToNotify;
    }

    public List<QueueNotificationBean> getQueuesToNotify() {
        return queuesToNotify;
    }

    public void setQueuesToNotify(List<QueueNotificationBean> queuesToNotify) {
        this.queuesToNotify = queuesToNotify;
    }
}
