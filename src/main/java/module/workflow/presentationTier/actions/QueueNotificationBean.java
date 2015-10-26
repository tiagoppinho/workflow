/*
 * @(#)QueueNotificationBean.java
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
package module.workflow.presentationTier.actions;

import java.io.Serializable;
import java.util.Collection;

import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowQueue;

import org.fenixedu.bennu.core.domain.User;

/**
 * Bean used to wrap the queues for the comment system
 * 
 * @author João Antunes
 * 
 */
public class QueueNotificationBean implements Serializable {

    private boolean ableToNotify;
    private final WorkflowQueue queue;

    public QueueNotificationBean(final WorkflowQueue queue, final WorkflowProcess process) {
        this.queue = queue;
        this.ableToNotify = process.isSystemAbleToNotifyQueue(queue);
    }

    public boolean isAbleToNotify() {
        return ableToNotify;
    }

    public void setAbleToNotify(final boolean ableToNotify) {
        this.ableToNotify = ableToNotify;
    }

    public Collection<User> getUsers() {
        return getQueue().getUsers();
    }

    //    public void setUser(User user) {
    //	this.user = user;
    //    }

    @Override
    public int hashCode() {
        return getQueue().hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof QueueNotificationBean && ((QueueNotificationBean) obj).getQueue() == getQueue();
    }

    public WorkflowQueue getQueue() {
        return queue;
    }
}
