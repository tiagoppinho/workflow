/*
 * @(#)QueuesForProcess.java
 *
 * Copyright 2011 Instituto Superior Tecnico
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
package module.workflow.presentationTier.renderers.providers;

import java.util.HashSet;
import java.util.Set;

import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowQueue;
import module.workflow.presentationTier.actions.CommentBean;
import module.workflow.presentationTier.actions.QueueNotificationBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * Lists the queues where the process has been and where it is, so that one can
 * notify the users of the queues directly via the commentary interface
 * 
 * 
 * @author João Antunes
 * 
 */
public class QueuesForProcess implements DataProvider {

    @Override
    public Converter getConverter() {
        return null;
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        CommentBean bean = (CommentBean) source;
        Set<QueueNotificationBean> availableQueuesToNotify = new HashSet<QueueNotificationBean>();
        WorkflowProcess process = bean.getProcess();

        for (WorkflowQueue queue : process.getQueueHistorySet()) {
            availableQueuesToNotify.add(new QueueNotificationBean(queue, process));
        }
        for (WorkflowQueue currentQueue : process.getCurrentQueuesSet()) {
            availableQueuesToNotify.add(new QueueNotificationBean(currentQueue, process));
        }

        return availableQueuesToNotify;
    }

}
