/*
 * @(#)BasicSearchProcessBean.java
 *
 * Copyright 2010 Instituto Superior Tecnico
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
import java.util.Collections;
import java.util.Set;

import module.workflow.domain.WorkflowProcess;

import org.apache.commons.collections.Predicate;

/**
 * 
 * @author Paulo Abrantes
 * 
 */
public class BasicSearchProcessBean implements Serializable {

    String processId;

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    @SuppressWarnings("unchecked")
    public Set<WorkflowProcess> search() {
        return (Set<WorkflowProcess>) (processId == null ? Collections.emptySet() : WorkflowProcess.getAllProcesses(
                WorkflowProcess.class, new Predicate() {

                    @Override
                    public boolean evaluate(Object arg0) {
                        return processId.trim().equals(((WorkflowProcess) arg0).getProcessNumber());
                    }

                }));
    }

}
