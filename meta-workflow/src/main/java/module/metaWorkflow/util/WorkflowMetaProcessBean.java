/*
 * @(#)WorkflowMetaProcessBean.java
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
package module.metaWorkflow.util;

import java.io.Serializable;

import module.workflow.domain.WorkflowQueue;
import pt.ist.bennu.core.domain.User;

/**
 * 
 * @author Paulo Abrantes
 * 
 */
public class WorkflowMetaProcessBean implements Serializable {

    String instanceDescription;
    String subject;

    WorkflowQueue queue;
    User requestor;

    public WorkflowMetaProcessBean() {
        setQueue(null);
        setRequestor(null);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getInstanceDescription() {
        return instanceDescription;
    }

    public void setInstanceDescription(String instanceDescription) {
        this.instanceDescription = instanceDescription;
    }

    public WorkflowQueue getQueue() {
        return queue;
    }

    public void setQueue(WorkflowQueue queue) {
        this.queue = queue;
    }

    public User getRequestor() {
        return requestor;
    }

    public void setRequestor(User user) {
        this.requestor = user;
    }
}
