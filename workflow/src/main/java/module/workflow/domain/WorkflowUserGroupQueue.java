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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import module.organization.domain.Person;
import module.workflow.util.WorkflowQueueBean;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.util.ClassNameBundle;
import pt.ist.fenixframework.Atomic;

@ClassNameBundle(bundle = "resources/WorkflowResources")
/**
 * 
 * @author Anil Kassamali
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class WorkflowUserGroupQueue extends WorkflowUserGroupQueue_Base {

    protected WorkflowUserGroupQueue() {
        super();
    }

    WorkflowUserGroupQueue(String name, List<User> baseUsers) {
        this();
        init(name, baseUsers);
    }

    @Override
    protected void init(String name) {
        throw new RuntimeException("invalid init invocation");
    }

    protected void init(String name, List<User> baseUsers) {
        setName(name);

        for (User user : baseUsers) {
            addUsers(user);
        }
    }

    @Override
    public boolean isUserAbleToAccessQueue(User user) {
        return getUsers().contains(user);
    }

    @Override
    @Atomic
    public void edit(WorkflowQueueBean bean) {
        setName(bean.getName());

        for (User user : ((WorkflowUserGroupQueueBean) bean).getUsers()) {
            addUsers(user);
        }
    }

    @Override
    @Atomic
    public void removeUsers(User users) {
        super.removeUsers(users);
    }

    @Override
    public boolean isConnectedToCurrentHost() {
        final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
        return virtualHost != null && getWorkflowSystem() == virtualHost.getWorkflowSystem();
    }

    @Override
    public Collection<Person> getPersons() {
        Collection<Person> persons = new ArrayList<Person>();

        Collection<User> users = getUsers();
        for (User user : users) {
            persons.add(user.getPerson());
        }

        return persons;
    }

}
