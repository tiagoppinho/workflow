/*
 * @(#)WorkflowQueueBean.java
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
package module.workflow.util;

import java.io.Serializable;

import module.workflow.domain.WorkflowQueue;

/**
 * 
 * @author João Antunes
 * @author Anil Kassamali
 * @author Paulo Abrantes
 * 
 */
public class WorkflowQueueBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private Class<? extends WorkflowQueue> queueType;
    private WorkflowQueue queue;

    public WorkflowQueueBean(WorkflowQueue queue) {
        setQueue(queue);
    }

    public WorkflowQueueBean() {
        setQueue(null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //TODO probably put this method abstract and override it on its subclasses
    public Class<? extends WorkflowQueue> getQueueType() {
        return queueType;
    }

    public void setQueueType(Class<? extends WorkflowQueue> queueType) {
        this.queueType = queueType;
    }

    public WorkflowQueue getQueue() {
        return queue;
    }

    public void setQueue(WorkflowQueue queue) {
        this.queue = queue;
        if (queue != null) {
            setName(queue.getName());
            fillQueueFields(queue);
        }
    }

    protected void fillQueueFields(WorkflowQueue queue) {

    }

    public WorkflowQueue createWorkflowQueue() {
        throw new RuntimeException("only subclasses can create workflow queues");
    }
}
