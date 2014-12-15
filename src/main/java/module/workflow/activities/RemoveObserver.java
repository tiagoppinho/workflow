/*
 * @(#)RemoveObserver.java
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

import module.workflow.domain.WorkflowProcess;

import org.fenixedu.bennu.core.domain.User;

/**
 * 
 * @author Paulo Abrantes
 * 
 */
public class RemoveObserver<T extends WorkflowProcess> extends WorkflowActivity<T, UserInformation<T>> {

    @Override
    public boolean isActive(T process, User user) {
        return process.isObserverSupportAvailable() && !process.isUserObserver(user)
                && (process.getCurrentOwner() == null || process.getCurrentOwner() == user);
    }

    @Override
    protected void process(UserInformation<T> information) {
        information.getProcess().removeObservers(information.getUser());
    }

    @Override
    public boolean isUserAwarenessNeeded(T process, User user) {
        return false;
    }

    @Override
    public ActivityInformation<T> getActivityInformation(T process) {
        return new UserInformation<T>(process, this);
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    protected String[] getArgumentsDescription(UserInformation<T> activityInformation) {
        return new String[] { activityInformation.getUser().getPresentationName() };
    }
}
