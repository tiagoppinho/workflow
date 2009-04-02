/*
 * @(#)WorkflowActivity.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: João Figueiredo, Luis Cruz, Paulo Abrantes, Susana Fernandes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the MyOrg web application infrastructure.
 *
 *   MyOrg is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.*
 *
 *   MyOrg is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with MyOrg. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package module.workflow.activities;

import module.workflow.domain.WorkflowProcess;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * 
 * @param <P>
 *            process that extends a WorkflowProcess
 * @param <AI>
 *            object that extends ActivityInformation (bean of the activity)
 */
public abstract class WorkflowActivity<P extends WorkflowProcess, AI extends ActivityInformation<P>> {

    /**
     * 
     * @return Current Logged User
     */
    protected User getLoggedPerson() {
	return UserView.getCurrentUser();
    }

    /**
     * 
     * @param process
     * @return true if the user that is executing the activity is the current
     *         owner of the process.
     */
    protected boolean isCurrentUserProcessOwner(P process) {
	final User currentOwner = process.getCurrentOwner();
	final User loggedPerson = getLoggedPerson();
	return currentOwner == null || (loggedPerson != null && loggedPerson == currentOwner);
    }

    /**
     * 
     * @param process
     * @return true if the process is taken by someone
     */
    protected boolean isProcessTaken(P process) {
	return process.getCurrentOwner() != null;
    }

    /**
     * 
     * @param process
     * @return true if the process is taken by the user who is running the
     *         activity
     */
    protected boolean isProcessTakenByCurrentUser(P process) {
	final User loggedPerson = getLoggedPerson();
	User taker = process.getCurrentOwner();
	return taker != null && loggedPerson != null && taker == loggedPerson;
    }

    /**
     * Responsible for creating a log of the activity run.
     * 
     * @param process
     * @param operationName
     * @param user
     */
    protected void logExecution(P process, String operationName, User user) {
	process.logExecution(getLoggedPerson(), operationName);
    }

    /**
     * Activity core method. When called runs the activity, it has 4 different
     * steps: verification of the conditions, log, actual execution and user
     * notification. To specify activity behavior this should not be the method
     * to be overridden. But process instead.
     * 
     * @param activityInformation
     */
    @Service
    public final void execute(AI activityInformation) {
	P process = activityInformation.getProcess();
	checkConditionsFor(process);
	logExecution(process, getClass().getSimpleName(), getLoggedPerson());
	process(activityInformation);
	notifyUsers(process);
    }

    private void checkConditionsFor(P process) {
	if (!isActive(process)) {
	    throw new ActivityException("activities.messages.exception.notActive", getLocalizedName());
	}
    }

    /**
     * Default implementation returns the simple class name.
     * 
     * @return activity name
     */
    public String getName() {
	return getClass().getSimpleName();
    }

    /**
     * This method is called when the activity is being ran. New activities
     * might want to override this method in order to notify users. When doing
     * so, please remind that it runs within the same write transaction of the
     * process execution.
     * 
     * @param process
     */
    protected void notifyUsers(P process) {
	// do nothing
    }

    /**
     * An activity is said active when the conditions to be ran are met.
     * 
     * @param process
     * @return true if the process is active for the current user
     */
    public boolean isActive(P process) {
	return isActive(process, getLoggedPerson());
    }

    /**
     * An activity is said active when the conditions to be ran are met.
     * 
     * @param process
     * @param user
     *            that will run activity
     * @return true if the process is active for the given user
     */
    public abstract boolean isActive(P process, User user);

    /**
     * An activity is said to need user awareness if it's important that the
     * users nows such activity is active.
     * 
     * @param process
     * @return true if the current user should be aware of such activity is
     *         active.
     */

    public boolean isUserAwarenessNeeded(P process) {
	return isUserAwarenessNeeded(process, getLoggedPerson());
    }

    /**
     * An activity is said to need user awareness if it's important that the
     * users nows such activity is active.
     * 
     * @param process
     * @param user
     * @return true if the user should be aware of such activity is active.
     */

    public boolean isUserAwarenessNeeded(P process, User user) {
	return true;
    }

    /**
     * This is the method that needs contains the activity specific behavior.
     * 
     * @param activityInformation
     */
    protected abstract void process(AI activityInformation);

    /**
     * 
     * @return The localized activity name used in the interfaces.
     */
    public abstract String getLocalizedName();

    /**
     * This returns the activity bean that might have further fields that are
     * needed for the activity to be ran.
     * 
     * @param process
     * @return an ActivityInformation or subclass
     */
    public ActivityInformation<P> getActivityInformation(P process) {
	return new ActivityInformation<P>(process, (WorkflowActivity<P, ActivityInformation<P>>) this);
    }

    /**
     * Allows for an activity to say if default user interface to receive
     * possible input is used or not. If it is used the user will be sent to
     * activityInput.jsp that shows an ActivityInformation with a schema
     * activityInformation.{ActivityInformationSimpleClassName}. Otherwise it
     * will look for a jsp file in the
     * ActivityInformationClassName.replace('.','/').jsp
     * 
     * @return by default returns true
     */
    public boolean isDefaultInputInterfaceUsed() {
	return true;
    }

    /**
     * Determines if a given activity needs confirmation to be ran. If returns
     * true the interface will first display a confirmation message with a
     * confirm/cancel buttons for the user to acknowledge the operation.
     * 
     * @return by default false
     */
    public boolean needsConfirmation() {
	return false;
    }

    /**
     * This is used when a activity needs confirmation. Holds the confirmation
     * message.
     * 
     * @return Localized confirmation message
     */
    public String getLocalizedConfirmationMessage() {
	return "";
    }
}
