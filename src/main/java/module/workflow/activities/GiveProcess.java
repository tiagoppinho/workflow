/*
 * @(#)GiveProcess.java
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

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import module.workflow.domain.WorkflowProcess;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class GiveProcess<T extends WorkflowProcess> extends WorkflowActivity<T, UserInformation<T>> {

    public static abstract class NotifyUser {

        public abstract void notifyUser(final User user, final WorkflowProcess process);

    }

    private NotifyUser notifyUser;

    public GiveProcess(final NotifyUser notifyUser) {
        this.notifyUser = notifyUser;
    }

    public GiveProcess() {
        this(null);
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString(getUsedBundle(), "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkflowProcess process, final User user) {
        return process.isTicketSupportAvailable() && !process.isUserObserver(user)
                && (process.getCurrentOwner() == null || process.getCurrentOwner() == user);
    }

    @Override
    protected void process(final UserInformation<T> information) {
        final User user = information.getUser();
        information.getProcess().giveProcess(user);
        if (notifyUser != null) {
            notifyUser.notifyUser(user, information.getProcess());
        }
    }

    @Override
    public boolean isUserAwarenessNeeded(final T process, final User user) {
        return false;
    }

    @Override
    public ActivityInformation<T> getActivityInformation(final T process) {
        return new UserInformation<T>(process, this);
    }

    @Override
    protected String[] getArgumentsDescription(final UserInformation<T> activityInformation) {
        final User user = activityInformation.getUser();
        return new String[] { user.getDisplayName() + " (" + user.getUsername() + ")" };
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
        return false;
    }

}
