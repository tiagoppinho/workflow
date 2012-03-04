/*
 * @(#)ProcessCounter.java
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
package module.workflow.domain;

import java.util.Comparator;

import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import myorg.util.BundleUtil;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class ProcessCounter {

    public static final Comparator<ProcessCounter> COMPARATOR = new Comparator<ProcessCounter>() {
        
        @Override
        public int compare(ProcessCounter o1, ProcessCounter o2) {
            final Class c1 = o1.getProcessClass();
            final Class c2 = o2.getProcessClass();

            final String s1 = BundleUtil.getLocalizedNamedFroClass(c1);
            final String s2 = BundleUtil.getLocalizedNamedFroClass(c2);

            return s1.compareTo(s2);
        }

    };

    private final Class clazz;

    public ProcessCounter(final Class clazz) {
	this.clazz = clazz;
    }

    public Class getProcessClass() {
	return clazz;
    }

    public Class getProcessClassForForwarding() {
	return getProcessClass();
    }

    public int getCount() {
	int result = 0;
	final User requestingUser = UserView.getCurrentUser();
	for (final WorkflowProcess process : WorkflowSystem.getInstance().getProcesses()) {
	    if (shouldCountProcess(process, requestingUser)) {
		result++;
	    }
	}
        return result;
    }

    protected boolean shouldCountProcess(final WorkflowProcess process, final User requestingUser) {
	return clazz.isAssignableFrom(process.getClass())
		&& process.isAccessible(requestingUser)
		&& process.hasAnyAvailableActivity(requestingUser, true);
    }

}
