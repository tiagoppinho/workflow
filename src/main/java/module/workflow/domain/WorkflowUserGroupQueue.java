/*
 * @(#)WorkflowUserGroupQueue.java
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

import java.util.Collection;
import java.util.List;

import module.workflow.util.ClassNameBundle;
import module.workflow.util.WorkflowQueueBean;

import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author Anil Kassamali
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
@ClassNameBundle(bundle = "WorkflowResources")
public class WorkflowUserGroupQueue extends WorkflowUserGroupQueue_Base {

    protected WorkflowUserGroupQueue() {
        super();
    }

    WorkflowUserGroupQueue(final String name, final Collection<User> baseUsers) {
        this();
        init(name, baseUsers);
    }

    @Override
    protected void init(final String name) {
        throw new RuntimeException("invalid init invocation");
    }

    protected void init(final String name, final Collection<User> baseUsers) {
        setName(name);
        baseUsers.forEach(this::addUsers);
    }

    @Override
    public boolean isUserAbleToAccessQueue(final User user) {
        return getUsers().contains(user);
    }

    @Override
    @Atomic
    public void edit(WorkflowQueueBean bean) {
        setName(bean.getName());
        ((WorkflowUserGroupQueueBean) bean).getUsers().forEach(this::addUsers);
    }

    @Override
    @Atomic
    public void removeUsers(final User users) {
        super.removeUsers(users);
    }

    @Override
    public Collection<User> getUsers() {
        return getUsersSet();
    }

}
