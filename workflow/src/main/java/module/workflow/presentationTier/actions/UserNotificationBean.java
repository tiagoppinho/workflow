/*
 * @(#)UserNotificationBean.java
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

import module.workflow.domain.WorkflowProcess;
import pt.ist.bennu.core.domain.User;

/**
 * 
 * @author Paulo Abrantes
 * 
 */
public class UserNotificationBean implements Serializable {

    private boolean ableToNotify;
    private User user;

    public UserNotificationBean(User user, WorkflowProcess process) {
        this.user = user;
        this.ableToNotify = process.isSystemAbleToNotifyUser(user);
    }

    public boolean isAbleToNotify() {
        return ableToNotify;
    }

    public void setAbleToNotify(boolean ableToNotify) {
        this.ableToNotify = ableToNotify;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        return user.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UserNotificationBean && ((UserNotificationBean) obj).user == user;
    }
}
