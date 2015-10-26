/*
 * @(#)WorkflowUserGroupQueueBean.java
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fenixedu.bennu.core.domain.User;

import module.workflow.util.WorkflowQueueBean;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author Anil Kassamali
 * @author Paulo Abrantes
 * 
 */
public class WorkflowUserGroupQueueBean extends WorkflowQueueBean {

    private static final long serialVersionUID = 1L;

    private final Set<User> users = new HashSet<User>();
    private User userToAdd = null;

    public WorkflowUserGroupQueueBean() {
        super();
    }

    public Set<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public User getUserToAdd() {
        return userToAdd;
    }

    public void setUserToAdd(User user) {
        this.userToAdd = user;
    }

    @Override
    protected void fillQueueFields(final WorkflowQueue queue) {
        users.addAll(((WorkflowUserGroupQueue) queue).getUsers());
    }

    @Override
    @Atomic
    public WorkflowUserGroupQueue createWorkflowQueue() {
        return new WorkflowUserGroupQueue(getName(), users);
    }
}
