/*
 * @(#)WorkflowProcess.java
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

package module.workflow.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import module.workflow.activities.WorkflowActivity;
import module.workflow.activities.ActivityInformation;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.MyOrg;
import myorg.domain.User;
import myorg.domain.exceptions.DomainException;

import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import pt.ist.fenixWebFramework.services.Service;

public abstract class WorkflowProcess extends WorkflowProcess_Base {

    public WorkflowProcess() {
	super();
	setOjbConcreteClass(getClass().getName());
	setMyOrg(MyOrg.getInstance());
    }

    @SuppressWarnings("unchecked")
    public static <T extends WorkflowProcess> Set<T> getAllProcesses(Class<T> processClass) {
	Set<T> classes = new HashSet<T>();
	for (WorkflowProcess process : MyOrg.getInstance().getProcessesSet()) {
	    if (processClass.isAssignableFrom(process.getClass())) {
		classes.add((T) process);
	    }
	}
	return classes;
    }

    @SuppressWarnings("unchecked")
    public static <T extends WorkflowProcess> Set<T> getAllProcesses(Class<T> processClass, Predicate predicate) {
	Set<T> classes = new HashSet<T>();
	for (WorkflowProcess process : MyOrg.getInstance().getProcessesSet()) {
	    if (processClass.isAssignableFrom(process.getClass()) && predicate.evaluate(process)) {
		classes.add((T) process);
	    }
	}

	return classes;
    }

    public <T extends WorkflowProcess, AI extends ActivityInformation<T>> WorkflowActivity<T, AI> getActivity(String activityName) {
	List<WorkflowActivity<T, AI>> activeActivities = getActivities();
	for (WorkflowActivity<T, AI> activity : activeActivities) {
	    if (activity.getName().equals(activityName)) {
		return activity;
	    }
	}

	return null;
    }

   public abstract <T extends WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> List<T> getActivities();

    @SuppressWarnings("unchecked")
    public <T extends WorkflowProcess, AI extends ActivityInformation<T>> List<WorkflowActivity<T, AI>> getActiveActivities() {
	List<WorkflowActivity<T, AI>> activities = new ArrayList<WorkflowActivity<T, AI>>();
	List<WorkflowActivity<T, AI>> activeActivities = getActivities();

	for (WorkflowActivity<T, AI> activity : activeActivities) {
	    if (activity.isActive((T) this)) {
		activities.add(activity);
	    }
	}

	return activities;
    }

    @SuppressWarnings("unchecked")
    public boolean hasAnyAvailableActivitity() {
	for (WorkflowActivity activity : getActivities()) {
	    if (activity.isActive(this)) {
		return true;
	    }
	}
	return false;
    }

    public DateTime getDateFromLastActivity() {
	List<ActivityLog> logs = new ArrayList<ActivityLog>();
	logs.addAll(getExecutionLogs());
	Collections.sort(logs, new Comparator<ActivityLog>() {

	    public int compare(ActivityLog log1, ActivityLog log2) {
		return -1 * log1.getWhenOperationWasRan().compareTo(log2.getWhenOperationWasRan());
	    }

	});

	return logs.isEmpty() ? null : logs.get(0).getWhenOperationWasRan();
    }

    public static boolean isCreateNewProcessAvailable() {
	final User user = UserView.getCurrentUser();
	return user != null;
    }

    public WorkflowProcessComment getMostRecentComment() {
	TreeSet<WorkflowProcessComment> comments = new TreeSet<WorkflowProcessComment>(WorkflowProcessComment.REVERSE_COMPARATOR);
	comments.addAll(getComments());
	return comments.size() > 0 ? comments.first() : null;
    }

    public List<ActivityLog> getExecutionLogs(DateTime begin, DateTime end) {
	return getExecutionLogs(begin, end);
    }

    public List<ActivityLog> getExecutionLogs(DateTime begin, DateTime end, Class<?>... activitiesClass) {
	List<ActivityLog> logs = new ArrayList<ActivityLog>();
	Interval interval = new Interval(begin, end);
	for (ActivityLog log : getExecutionLogs()) {
	    if (interval.contains(log.getWhenOperationWasRan())
		    && (activitiesClass.length == 0 || match(activitiesClass, log.getOperation()))) {
		logs.add(log);
	    }
	}
	return logs;
    }

    private boolean match(Class<?>[] classes, String name) {
	for (Class<?> clazz : classes) {
	    if (clazz.getSimpleName().equals(name)) {
		return true;
	    }
	}
	return false;
    }

    @Service
    public void createComment(User user, String comment) {
	new WorkflowProcessComment(this, user, comment);
    }

    @Service
    public void addFile(String displayName, String filename, byte[] consumeInputStream) {
	GenericFile file = new GenericFile(displayName, filename, consumeInputStream);
	addFiles(file);
    }

    @Override
    public void setCurrentOwner(User currentOwner) {
	throw new DomainException("error.message.illegal.method.useTakeInstead");
    }

    @Override
    public void removeCurrentOwner() {
	throw new DomainException("error.message.illegal.method.useReleaseInstead");
    }

    public void systemProcessRelease() {
	super.setCurrentOwner(null);
    }

    @Service
    public void takeProcess() {
	final User currentOwner = getCurrentOwner();
	if (currentOwner != null) {
	    throw new DomainException("error.message.illegal.method.useStealInstead");
	}
	super.setCurrentOwner(UserView.getCurrentUser());
    }

    @Service
    public void releaseProcess() {
	final User loggedPerson = UserView.getCurrentUser();
	final User person = getCurrentOwner();
	if (loggedPerson != null && person != null && loggedPerson != person) {
	    throw new DomainException("error.message.illegal.state.unableToReleaseATicketNotOwnerByUser");
	}
	super.setCurrentOwner(null);
    }

    @Service
    public void stealProcess() {
	super.setCurrentOwner(UserView.getCurrentUser());
    }

    public void giveProcess(User user) {
	final User currentOwner = getCurrentOwner();
	final User currentUser = UserView.getCurrentUser();
	if (currentOwner != null && currentOwner != currentUser) {
	    throw new DomainException("error.message.illegal.state.unableToGiveAnAlreadyTakenProcess");
	}
	super.setCurrentOwner(user);
    }

    public boolean isUserCurrentOwner() {
	final User loggedPerson = UserView.getCurrentUser();
	return loggedPerson != null && loggedPerson == getCurrentOwner();
    }

    public boolean isTakenByPerson(User person) {
	return person != null && person == getCurrentOwner();
    }

    public boolean isTakenByCurrentUser() {
	final User loggedPerson = UserView.getCurrentUser();
	return loggedPerson != null && isTakenByPerson(loggedPerson);
    }

    @SuppressWarnings("unchecked")
    public <T extends ActivityLog> T logExecution(User person, String operationName, Object... args) {
	return (T) new ActivityLog(this, person, operationName);
    }

    @Override
    @Service
    public void removeFiles(GenericFile file) {
	super.removeFiles(file);
	addDeletedFiles(file);
    }

    public List<WorkflowProcessComment> getUnreadCommentsForCurrentUser() {
	return getUnreadCommentsForUser(UserView.getCurrentUser());
    }

    public List<WorkflowProcessComment> getUnreadCommentsForUser(User user) {
	List<WorkflowProcessComment> comments = new ArrayList<WorkflowProcessComment>();
	for (WorkflowProcessComment comment : getComments()) {
	    if (comment.isUnreadBy(user)) {
		comments.add(comment);
	    }
	}
	return comments;
    }

    @Service
    public void markCommentsAsReadForUser(User user) {
	for (WorkflowProcessComment comment : getComments()) {
	    if (comment.isUnreadBy(user)) {
		comment.addReaders(user);
	    }
	}
    }

    public boolean isFileSupportAvailable() {
	return true;
    }

    public boolean isCommentsSupportAvailable() {
	return true;
    }

    public boolean isTicketSupportAvailable() {
	return true;
    }

}
