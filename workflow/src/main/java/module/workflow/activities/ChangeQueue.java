/*
 * @(#)ChangeQueue.java
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

import java.util.Collection;

import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowQueue;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.bennu.core.util.BundleUtil;

/**
 * 
 * @author João Neves
 * @author Paulo Abrantes
 * 
 */
public class ChangeQueue<T extends WorkflowProcess> extends WorkflowActivity<T, ChangeQueueInformation<T>> {

    @Override
    public boolean isActive(T process, User user) {
        return process.isAccessible(user) && !process.isUserObserver(user);
    }

    @Override
    protected void process(ChangeQueueInformation<T> activityInformation) {
        WorkflowProcess process = activityInformation.getProcess();
        Collection<WorkflowQueue> queuesToAdd = activityInformation.getQueuesToAdd();
        Collection<WorkflowQueue> queuesToRemove = activityInformation.getQueuesToRemove();

        for (WorkflowQueue queueToRemove : queuesToRemove) {
            if (!queueToRemove.isCurrentUserAbleToAccessQueue()) {
                throw new DomainException(BundleUtil.getFormattedStringFromResourceBundle("resources/MetaWorkflowResources",
                        "error.user.cannotRemove.from.queue", queueToRemove.getName()));
            }
            process.removeCurrentQueues(queueToRemove);
        }

        for (WorkflowQueue queueToAdd : queuesToAdd) {
            process.addCurrentQueues(queueToAdd);
        }

        if (process.getCurrentQueuesSet().isEmpty()) {
            throw new DomainException(BundleUtil.getFormattedStringFromResourceBundle("resources/MetaWorkflowResources",
                    "error.process.mustHave.atLeast.one.queue"));
        }
    }

    @Override
    public String getUsedBundle() {
        return "resources/WorkflowResources";
    }

    @Override
    public ChangeQueueInformation<T> getActivityInformation(T process) {
        return new ChangeQueueInformation<T>(process, this);
    }

}
