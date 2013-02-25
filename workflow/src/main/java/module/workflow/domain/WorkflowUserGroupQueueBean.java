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

import module.workflow.util.WorkflowQueueBean;
import pt.ist.bennu.core.domain.User;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author Anil Kassamali
 * @author Paulo Abrantes
 * 
 */
public class WorkflowUserGroupQueueBean extends WorkflowQueueBean {

    private static final long serialVersionUID = 1L;

    private final Set<User> users;
    private User userToAdd;

    public WorkflowUserGroupQueueBean() {
        super();
        users = new HashSet<User>();
        setUserToAdd(null);
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
    protected void fillQueueFields(WorkflowQueue queue) {
        for (User user : ((WorkflowUserGroupQueue) queue).getUsers()) {
            users.add(user);
        }
    }

    @Override
    @Atomic
    public WorkflowUserGroupQueue createWorkflowQueue() {
        List<User> usersList = new ArrayList<User>();
        usersList.addAll(getUsers());

        return new WorkflowUserGroupQueue(getName(), usersList);
    }
}
