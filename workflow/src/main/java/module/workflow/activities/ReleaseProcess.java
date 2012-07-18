/*
 * @(#)ReleaseProcess.java
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
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.BundleUtil;

/**
 * 
 * @author João Antunes
 * @author Paulo Abrantes
 * 
 */
public class ReleaseProcess<T extends WorkflowProcess> extends WorkflowActivity<T, ActivityInformation<T>> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(T process, User user) {
	return process.isTicketSupportAvailable() && process.getCurrentOwner() != null && process.getCurrentOwner() == user;
    }

    @Override
    protected void process(ActivityInformation<T> information) {
	information.getProcess().releaseProcess();
    }

    @Override
    public boolean isUserAwarenessNeeded(T process, User user) {
	//joantune: if we own it, that's because we should do something with it, otherwise
	//we should release it
	return true;
    }

    @Override
    public boolean isVisible() {
	return false;
    }

}
