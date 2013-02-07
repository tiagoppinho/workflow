/*
 * @(#)ChangeMetaQueue.java
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
package module.metaWorkflow.activities;

import java.util.Iterator;

import module.metaWorkflow.domain.WorkflowMetaProcess;
import module.workflow.activities.ChangeQueue;
import module.workflow.activities.ChangeQueueInformation;
import module.workflow.domain.WorkflowQueue;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.BundleUtil;

/**
 * 
 * @author João Antunes
 * @author João Neves
 * @author Paulo Abrantes
 * 
 */
public class ChangeMetaQueue extends ChangeQueue<WorkflowMetaProcess> {

    @Override
    public String getUsedBundle() {
        return "resources/MetaWorkflowResources";
    }

    @Override
    public boolean isActive(WorkflowMetaProcess process, User user) {
        return process.isUserAbleToAccessCurrentQueues(user) || process.isTakenByPerson(user);
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
        return false;
    }

    @Override
    protected String[] getArgumentsDescription(ChangeQueueInformation<WorkflowMetaProcess> activityInformation) {
        String queuesDescription = "";
        if (activityInformation.hasAnyQueuesToRemove()) {
            if (activityInformation.hasAnyQueuesToAdd()) {
                queuesDescription += BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label.from");
            } else {
                queuesDescription += BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label.removedFrom");
            }

            queuesDescription += " ";
            Iterator<WorkflowQueue> queuesToRemove = activityInformation.getQueuesToRemove().iterator();
            if (queuesToRemove.hasNext()) {
                queuesDescription += queuesToRemove.next().getName();
            }
            while (queuesToRemove.hasNext()) {
                queuesDescription += ", " + queuesToRemove.next().getName();
            }
        }

        if (activityInformation.hasAnyQueuesToAdd()) {
            if (activityInformation.hasAnyQueuesToRemove()) {
                queuesDescription += " | ";
            }
            queuesDescription += BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label.to");

            queuesDescription += " ";
            Iterator<WorkflowQueue> queuesToAdd = activityInformation.getQueuesToAdd().iterator();
            if (queuesToAdd.hasNext()) {
                queuesDescription += queuesToAdd.next().getName();
            }
            while (queuesToAdd.hasNext()) {
                queuesDescription += ", " + queuesToAdd.next().getName();
            }
        }

        return new String[] { queuesDescription };
    }

    @Override
    public boolean isUserAwarenessNeeded(WorkflowMetaProcess process, User user) {
        //joantune: so, basicly we want to make sure that:
        //- the given user belongs to the current queue;
        //- and the proccess isn't closed;
        //- and the proccess isn't taken by somebody else

        if (process.isOpen() && (process.getCurrentOwner() == null || process.isTakenByPerson(user))
                && process.isUserAbleToAccessCurrentQueues(user)) {
            return true;

        }
        return false;
    }
}
