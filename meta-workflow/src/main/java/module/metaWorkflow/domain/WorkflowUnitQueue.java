/*
 * @(#)WorkflowUnitQueue.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Meta-Workflow Module.
 *
 *   The Meta-Workflow Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Meta-Workflow Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Meta-Workflow Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.metaWorkflow.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import module.organization.domain.AccountabilityType;
import module.organization.domain.Person;
import module.organization.domain.Unit;
import module.workflow.domain.WorkflowQueue;
import module.workflow.domain.WorkflowSystem;
import module.workflow.util.WorkflowQueueBean;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.util.ClassNameBundle;
import pt.ist.fenixframework.Atomic;

@ClassNameBundle(bundle = "resources/MetaWorkflowResources")
/**
 * 
 * @author Anil Kassamali
 * @author Shezad Anavarali
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class WorkflowUnitQueue extends WorkflowUnitQueue_Base {

    protected WorkflowUnitQueue() {
        super();
    }

    WorkflowUnitQueue(Unit unit, String name, List<AccountabilityType> accountabilityTypes) {
        this();
        init(unit, name, accountabilityTypes);
    }

    @Override
    protected void init(String name) {
        throw new RuntimeException("invalid init invocation");
    }

    protected void init(Unit unit, String name, List<AccountabilityType> accountabilityTypes) {
        super.init(name);

        setName(name);
        setUnit(unit);

        for (AccountabilityType accountabilityType : accountabilityTypes) {
            addAccountabilityTypes(accountabilityType);
        }
    }

    @Override
    @Atomic
    public void edit(WorkflowQueueBean bean) {
        WorkflowUnitQueueBean unitQueueBean = (WorkflowUnitQueueBean) bean;
        setName(bean.getName());
        getAccountabilityTypes().clear();
        getAccountabilityTypes().addAll(unitQueueBean.getAccountabilityTypes());
    }

    @Override
    public boolean isUserAbleToAccessQueue(User user) {
        if (user == null) {
            return false;
        }

        final Person person = user.getPerson();
        final Unit unit = getUnit();
        return unit.hasChildAccountabilityIncludingAncestry(getAccountabilityTypes(), person);
    }

    public static Set<WorkflowUnitQueue> getQueuesFor(Collection<Unit> units) {
        Set<WorkflowUnitQueue> queues = new HashSet<WorkflowUnitQueue>();
        for (Unit unit : units) {
            queues.addAll(unit.getQueues());
        }
        return queues;
    }

    public static Collection<WorkflowUnitQueue> readAll() {
        List<WorkflowUnitQueue> queues = new ArrayList<WorkflowUnitQueue>();
        for (WorkflowQueue queue : WorkflowSystem.getInstance().getWorkflowQueues()) {
            if (queue instanceof WorkflowUnitQueue) {
                queues.add((WorkflowUnitQueue) queue);
            }
        }
        return queues;
    }

    @Override
    public boolean isConnectedToCurrentHost() {
        final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
        return virtualHost != null && getWorkflowSystem() == virtualHost.getWorkflowSystem();
    }

    @Override
    public Collection<Person> getPersons() {
        return getUnit().getChildPersons(getAccountabilityTypes());
    }
}
