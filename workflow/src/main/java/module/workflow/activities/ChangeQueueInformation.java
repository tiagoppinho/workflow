/*
 * @(#)ChangeQueueInformation.java
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
package module.workflow.activities;

import java.util.ArrayList;
import java.util.List;

import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowQueue;

import org.fenixedu.bennu.core.security.Authenticate;

/**
 * 
 * @author João Neves
 * @author Paulo Abrantes
 * 
 */
public class ChangeQueueInformation<T extends WorkflowProcess> extends ActivityInformation<T> {

    private List<WorkflowQueue> queuesToRemove;
    private List<WorkflowQueue> queuesToAdd;

    public ChangeQueueInformation(T process, ChangeQueue<T> activity) {
        super(process, activity);
        queuesToRemove = new ArrayList<WorkflowQueue>(process.getCurrentQueuesSet());
        queuesToRemove.retainAll(Authenticate.getUser().getQueuesSet());
    }

    @Override
    public boolean hasAllneededInfo() {
        return isForwardedFromInput();
    }

    public void setQueuesToRemove(List<WorkflowQueue> queuesToRemove) {
        this.queuesToRemove = queuesToRemove;
    }

    public List<WorkflowQueue> getQueuesToRemove() {
        return queuesToRemove;
    }

    public boolean hasAnyQueuesToRemove() {
        return !queuesToRemove.isEmpty();
    }

    public void setQueuesToAdd(List<WorkflowQueue> queuesToAdd) {
        this.queuesToAdd = queuesToAdd;
    }

    public List<WorkflowQueue> getQueuesToAdd() {
        return queuesToAdd;
    }

    public boolean hasAnyQueuesToAdd() {
        return !queuesToAdd.isEmpty();
    }
}
