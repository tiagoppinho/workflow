/*
 * @(#)WorkflowUnitQueueBean.java
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
import java.util.List;

import module.organization.domain.AccountabilityType;
import module.organization.domain.Unit;
import module.workflow.domain.WorkflowQueue;
import module.workflow.util.WorkflowQueueBean;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author Pedro Santos
 * @author Anil Kassamali
 * @author Paulo Abrantes
 * 
 */
public class WorkflowUnitQueueBean extends WorkflowQueueBean {

    private static final long serialVersionUID = 1L;

    private Unit unit;
    private List<AccountabilityType> accountabilityTypes;

    public WorkflowUnitQueueBean() {
        setAccountabilityTypes(new ArrayList<AccountabilityType>());
        setUnit(null);
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public void setAccountabilityTypes(Collection<AccountabilityType> accountabilityTypes) {
        this.accountabilityTypes = new ArrayList<AccountabilityType>();
        for (AccountabilityType type : accountabilityTypes) {
            this.accountabilityTypes.add(type);
        }
    }

    public List<AccountabilityType> getAccountabilityTypes() {
        return accountabilityTypes;
    }

    @Override
    protected void fillQueueFields(WorkflowQueue queue) {
        setAccountabilityTypes(((WorkflowUnitQueue) queue).getAccountabilityTypes());
    }

    @Override
    @Atomic
    public WorkflowUnitQueue createWorkflowQueue() {
        return new WorkflowUnitQueue(getUnit(), getName(), getAccountabilityTypes());
    }

}
