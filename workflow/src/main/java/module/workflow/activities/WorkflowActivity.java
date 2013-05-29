/*
 * @(#)WorkflowActivity.java
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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import module.workflow.domain.ActivityLog;
import module.workflow.domain.WorkflowProcess;
import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * 
 * 
 * @param <P>
 *            process that extends a WorkflowProcess
 * @param <AI>
 *            object that extends ActivityInformation (bean of the activity)
 * 
 * @author Diogo Figueiredo
 * @author João Antunes
 * @author Anil Kassamali
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
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
     *         owner of the process or the process has no owner at all.
     */
    protected boolean isCurrentUserProcessOwner(P process) {
        return process.getCurrentOwner() == null || isProcessTakenByCurrentUser(process);
    }

    /**
     * 
     * @param process
     * @param user
     * @return true if the user is the current owner of the process or the
     *         activity has no owner at all.
     */
    protected boolean isUserProcessOwner(P process, User user) {
        return process.getCurrentOwner() == null || isProcessTakenByUser(process, user);
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
        return isProcessTakenByUser(process, getLoggedPerson());
    }

    /**
     * 
     * @param process
     * @param user
     * @return true if the process is taken by the user
     * 
     */
    protected boolean isProcessTakenByUser(P process, User user) {
        User taker = process.getCurrentOwner();
        return taker != null && taker == user;
    }

    /**
     * Responsible for creating a log of the activity run.
     * 
     * @param process
     * @param operationName
     * @param user
     */
    protected ActivityLog logExecution(P process, String operationName, User user, AI activityInfo,
            String... argumentsDescription) {
        return process.logExecution(getLoggedPerson(), operationName, argumentsDescription);
    }

    /**
     * Activity core method. When called runs the activity, it has 4 different
     * steps: verification of the conditions, log, actual execution and user
     * notification. To specify activity behavior this should not be the method
     * to be overridden. But process instead.
     * 
     * @param activityInformation
     */
    @Atomic
    public final void execute(AI activityInformation) {
        P process = activityInformation.getProcess();
        checkConditionsFor(process);
        ActivityLog log = null;
        if (shouldLogActivity(activityInformation)) {
            log =
                    logExecution(process, getClass().getSimpleName(), getLoggedPerson(), activityInformation,
                            getArgumentsDescription(activityInformation));
        }
        process(activityInformation);
        notifyUsers(process);

    }

    /**
     * @param activityInformation the {@link ActivityInformation} to help decide
     * @return true if this activity should be logged, false otherwise
     */
    protected boolean shouldLogActivity(AI activityInformation) {
        return true;
    }

    /**
     * Returns values that will be used in activity's log description t If this
     * is redefined, when presenting the description the bundle defined in
     * getUsedBundle will be looked for a string
     * label.description.FULL_CLASS_NAME.
     * 
     * @param activityInformation
     * @return String array with the arguments description to be passed to the
     *         locazation string
     */
    protected String[] getArgumentsDescription(AI activityInformation) {
        return null;
    }

    public static class NotActiveActivityException extends ActivityException {

        public NotActiveActivityException(final String activityName) {
            super(BundleUtil.getFormattedStringFromResourceBundle("resources.WorkflowResources",
                    "activities.messages.exception.notActive", activityName), activityName);
        }

    }

    private void checkConditionsFor(final P process) {
        if (!isActive(process)) {
            final String activityName = getLocalizedName();
            throw new NotActiveActivityException(activityName);
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
     * users knows such activity is active.
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
     * The logs will fetch the logs in the given {@link #getUsedBundle()} the
     * label: "label.description.{fullClassName}"
     * 
     * You can provide arguments to the log label by overriding {@link #getArgumentsDescription(ActivityInformation)}
     * 
     * @param activityInformation
     */
    protected abstract void process(AI activityInformation);

    /**
     * 
     * @return The localized activity name used in the interfaces.
     */
    public String getLocalizedName() {
        try {
            return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "activity." + getClass().getSimpleName());
        } catch (java.util.MissingResourceException e) {
            e.printStackTrace();
            return getClass().getSimpleName();
        }
    }

    /**
     * This returns the activity bean that might have further fields that are
     * needed for the activity to be ran.
     * 
     * @param process
     * @return an ActivityInformation or subclass
     */
    public ActivityInformation<P> getActivityInformation(P process) {
        return new ActivityInformation<P>(process, this);
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
     * @param process
     * @return by default false
     */
    public boolean isConfirmationNeeded(P process) {
        return false;
    }

    /**
     * This is used when a activity needs confirmation. By default delegates to
     * {@link WorkflowActivity#getLocalizedConfirmationMessage()}
     * 
     * @param P
     *            the process
     * @return Localized confirmation message
     */
    public String getLocalizedConfirmationMessage(P process) {
        return getLocalizedConfirmationMessage();
    }

    /**
     * This is used when a activity needs confirmation. Holds the confirmation
     * message. By default the label is activity.confirmation.CLASS_NAME
     * 
     * @return Localized confirmation message
     */
    public String getLocalizedConfirmationMessage() {
        return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "activity.confirmation." + getClass().getName());
    }

    /**
     * Specifies which bundle should be used when doing localization operation
     * in the activity. Things such as the LocalizedName,
     * LocalizedConfirmationMessage should relay on this method. Also
     * ActivityLog uses this method in order to localize the description message
     * if needed.
     * 
     * @return name of the used bundle
     */
    public String getUsedBundle() {
        return "resources/WorkflowResources";
    }

    /**
     * Sometimes developers do not want to have the activity listed in the
     * activity list in the process page. If so this method should be overriden
     * and start returning false.
     * 
     * @return true if activity should be listed in the activity list, false
     *         otherwise
     */
    public boolean isVisible() {
        return true;
    }

    /**
     * In the process page, along with the rendering of the activities name
     * there's also the option to render a help [ ? ] marker which on mouse over
     * will display the text return by {@link WorkflowActivity#getHelpMessage()}
     * 
     * Default implementation checks if the Bundle from getUsedBundle() contains
     * a key activity.FULL_CLASS_NAME.help
     * 
     * @return true if an help marker should be rendered for the following
     *         activity
     */
    public boolean hasHelpMessage() {
        final ResourceBundle resourceBundle = ResourceBundle.getBundle(getUsedBundle(), Language.getLocale());
        try {
            resourceBundle.getString("label." + getClass().getName() + ".help");
        } catch (MissingResourceException e) {
            return false;
        }
        return true;
    }

    /**
     * By default it returns the activity.help.CLASS_NAME label.
     * 
     * @return Localized help message
     */
    public String getHelpMessage() {
        return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label." + getClass().getName() + ".help");
    }
}
