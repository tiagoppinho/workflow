/*
 * @(#)WorkflowProcess.java
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
package module.workflow.domain;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.exceptions.DuplicateProcessFileNameException;
import module.workflow.domain.exceptions.WorkflowDomainException;
import module.workflow.presentationTier.WorkflowLayoutContext;
import module.workflow.presentationTier.actions.CommentBean;
import module.workflow.util.ProcessEvaluator;
import module.workflow.util.WorkflowClassUtil;
import module.workflow.util.WorkflowFileUploadBean;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.core.WriteOnReadError;

/**
 * 
 * @author Diogo Figueiredo
 * @author Daniel Ribeiro
 * @author Pedro Santos
 * @author João Neves
 * @author João Antunes
 * @author Anil Kassamali
 * @author Shezad Anavarali
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public abstract class WorkflowProcess extends WorkflowProcess_Base {

    public WorkflowProcess() {
        super();
        setWorkflowSystem(WorkflowSystem.getInstance());
    }

    public static void evaluate(final Class processClass, final ProcessEvaluator<WorkflowProcess> processEvaluator,
            final Collection<? extends WorkflowProcess> processes) {
        for (final WorkflowProcess process : processes) {
            if (processClass.isAssignableFrom(process.getClass())) {
                processEvaluator.evaluate(process);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected static <T extends WorkflowProcess> Set<T> filter(Class<T> processClass, Predicate<WorkflowProcess> predicate,
            Collection<? extends WorkflowProcess> processes) {
        Set<T> classes = new HashSet<T>();
        for (WorkflowProcess process : processes) {
            if (processClass.isAssignableFrom(process.getClass()) && (predicate == null || predicate.test(process))) {
                classes.add((T) process);
            }
        }

        return classes;
    }

    /**
     * 
     * @return true if this process has any relation with queues. False
     *         otherwise
     */
    public boolean isQueuesAssociated() {
        return (!getQueueHistory().isEmpty()) || (!getCurrentQueues().isEmpty());
    }

    public static <T extends WorkflowProcess> Set<T> getAllProcesses(Class<T> processClass) {
        return filter(processClass, null, WorkflowSystem.getInstance().getProcessesSet());
    }

    public static <T extends WorkflowProcess> Set<T> getAllProcesses(Class<T> processClass, Predicate<WorkflowProcess> predicate) {
        return filter(processClass, predicate, WorkflowSystem.getInstance().getProcessesSet());
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

    /*
     * Abstract method that returns ALL activities for the process either
     * they're active or not.
     * 
     * @return List of objects that extends {@link WorkflowActivity}
     */
    public abstract <T extends WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> List<T> getActivities();

    /**
     * This method is usually used by generic filtering mechanisms. Usually
     * inactive processes are discarded.
     * 
     * @return true if the process should be seen as active
     */
    public abstract boolean isActive();

    /**
     * 
     * @return User that should be seen as the process creator
     */
    public abstract User getProcessCreator();

    /**
     * This method is called when the process is sent to the main process page
     * and also in default filtering mechanisms.
     * 
     * In order to implement access control to the process, this method should
     * be overriden.
     * 
     * @param user user
     * @return true if the user given has input can access the process
     */
    public boolean isAccessible(User user) {
        return true;
    }

    public boolean isAccessibleToCurrentUser() {
        return isAccessible(Authenticate.getUser());
    }

    /**
     * The WorkflowProcess already gives a default layout to render the head,
     * body and a short body, which is presented in the activities input pages,
     * of the processes pages. In order to have specific layouts for the process
     * this method should be override with a new Layout class that extends
     * WorkflowLayoutContext.
     * 
     * Default values:
     * 
     * Head: /FULL_CLASS_NAME.replace('.','/')/head.jsp Body:
     * /FULL_CLASS_NAME.replace('.','/')/body.jsp ShortBody:
     * /FULL_CLASS_NAME.replace('.','/')/shortBody.jsp
     * 
     * @return The WorkflowLayoutContext to use in the main process page
     */
    public WorkflowLayoutContext getLayout() {
        return WorkflowLayoutContext.getDefaultWorkflowLayoutContext(this.getClass());
    }

    @SuppressWarnings("unchecked")
    public <T extends WorkflowProcess, AI extends ActivityInformation<T>> List<WorkflowActivity<T, AI>> getActiveActivities() {
        try {
            List<WorkflowActivity<T, AI>> activities = new ArrayList<WorkflowActivity<T, AI>>();
            List<WorkflowActivity<T, AI>> activeActivities = getActivities();

            for (WorkflowActivity<T, AI> activity : activeActivities) {
                if (activity.isActive((T) this)) {
                    activities.add(activity);
                }
            }

            return activities;
        } catch (final Throwable t) {
            t.printStackTrace();
            throw new Error(t);
        }
    }

    @SuppressWarnings("unchecked")
    public boolean hasAnyAvailableActivitity() {
        return hasAnyAvailableActivity(true);
    }

    @SuppressWarnings("unchecked")
    public boolean hasAnyAvailableActivity(boolean checkForUserAwareness) {
        for (WorkflowActivity activity : getActivities()) {
            try {
                if ((!checkForUserAwareness || activity.isUserAwarenessNeeded(this)) && activity.isActive(this)) {
                    return true;
                }
            } catch (Throwable t) {
                t.printStackTrace();
                throw new Error(t);
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public boolean hasAnyAvailableActivity(boolean checkForUserAwareness, User user) {
        for (WorkflowActivity activity : getActivities()) {
            if ((!checkForUserAwareness || activity.isUserAwarenessNeeded(this, user)) && activity.isActive(this, user)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAnyAvailableActivity(User user, boolean checkForUserAwareness) {
        for (WorkflowActivity activity : getActivities()) {
            if ((!checkForUserAwareness || activity.isUserAwarenessNeeded(this, user)) && activity.isActive(this, user)) {
                return true;
            }
        }
        return false;
    }

    public DateTime getDateFromLastActivity() {
        List<WorkflowLog> logs = new ArrayList<WorkflowLog>();
        logs.addAll(getExecutionLogs());
        Collections.sort(logs, (log1, log2) -> -1 * log1.getWhenOperationWasRan().compareTo(log2.getWhenOperationWasRan()));

        return logs.isEmpty() ? null : logs.get(0).getWhenOperationWasRan();
    }

    public static boolean isCreateNewProcessAvailable() {
        final User user = Authenticate.getUser();
        return user != null;
    }

    public WorkflowProcessComment getMostRecentComment() {
        TreeSet<WorkflowProcessComment> comments = new TreeSet<WorkflowProcessComment>(WorkflowProcessComment.REVERSE_COMPARATOR);
        comments.addAll(getComments());
        return comments.size() > 0 ? comments.first() : null;
    }

    /**
     * This method returns logs that were not 'finished' and should not be
     * showed to the user
     * 
     * @return List
     */
    public List<WorkflowLog> getPendingLogs() {
        List<WorkflowLog> list = new ArrayList<WorkflowLog>();

        for (WorkflowLog log : super.getExecutionLogsSet()) {
            if (log.getWhenOperationWasRan() == null) {
                list.add(log);
            }
        }

        return list;
    }

    @Override
    public Set<WorkflowLog> getExecutionLogsSet() {
        Set<WorkflowLog> list = new HashSet<WorkflowLog>();

        for (WorkflowLog log : super.getExecutionLogsSet()) {
            if (log.getWhenOperationWasRan() != null) {
                list.add(log);
            }
        }

        return list;
    }

    public List<ActivityLog> getExecutionLogs(DateTime begin, DateTime end) {
        return getExecutionLogs(begin, end);
    }

    public List<WorkflowLog> getExecutionLogs(DateTime begin, DateTime end, Class<?>... activitiesClass) {
        List<WorkflowLog> logs = new ArrayList<WorkflowLog>();
        Interval interval = new Interval(begin, end);
        for (WorkflowLog log : getExecutionLogs()) {
            if (interval.contains(log.getWhenOperationWasRan())
                    && (activitiesClass.length == 0 || (log instanceof ActivityLog && match(activitiesClass,
                            ((ActivityLog) log).getOperation())))) {
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

    @Atomic
    public void createComment(User user, CommentBean bean) {
        String comment = bean.getComment();
        new WorkflowProcessComment(this, user, comment);
        for (User notifyUser : bean.getUsersToNotify()) {
            notifyUserDueToComment(notifyUser, comment);
        }
    }

    /**
     * The comment page allows for the comments to be delivered to the users
     * through some communication channel, usually email or sms, this method
     * should implement that communication system.
     * 
     * @param user user
     * @param comment comment
     */
    public abstract void notifyUserDueToComment(User user, String comment);

    /**
     * Not all users might be reachable through the notify system implemented in
     * {@link WorkflowProcess#notifyUserDueToComment(User user, String comment)} , for example you've implemented an email
     * notification system, but the
     * user has no email in the system.
     * 
     * This method is use to determine that and render warnings in the interface
     * for the users that cannot be notified.
     * 
     * @param user user
     * @return true if the system is able to notify the user
     */
    public boolean isSystemAbleToNotifyUser(User user) {
        return true;
    }

    /**
     * For some reason (e.g. lots of users in a queue) one might not want to
     * send a notification by email to the whole list of users in a queue
     * 
     * @author joantune - João Antunes
     * 
     * @param queue
     *            the queue to where to assert if one wants to send a
     *            notification or not
     * @return if true, notify away, if false don't
     */
    public boolean isSystemAbleToNotifyQueue(WorkflowQueue queue) {
        return true;
    }

    public Set<User> getProcessWorkers() {
        Set<User> users = new HashSet<User>();
        for (WorkflowLog log : getExecutionLogs()) {
            users.add(log.getActivityExecutor());
        }
        return users;
    }

    public void preAccessFile(ProcessFile file) {
        if (isFileSupportAvailable()) {
            file.preFileContentAccess();
            return;
        }
        throw new WorkflowDomainException("label.error.workflowProcess.noSupportForFiles");
    }

    @Atomic
    public void postAccessFile(ProcessFile file) {
        if (isFileSupportAvailable()) {
            file.postFileContentAccess();
            if (file.shouldFileContentAccessBeLogged()) {
                String nameToLog = file.getDisplayName() != null ? file.getDisplayName() : file.getFilename();
                new FileAccessLog(this, Authenticate.getUser(), file.getFilename(), nameToLog,
                        WorkflowClassUtil.getNameForType(file.getClass()));
            }
        } else {
            throw new WorkflowDomainException("label.error.workflowProcess.noSupportForFiles");
        }
    }

    @Atomic
    public <T extends ProcessFile> T addFile(Class<T> instanceToCreate, String displayName, String filename,
            byte[] consumeInputStream, WorkflowFileUploadBean bean) throws Exception {
        if (isFileSupportAvailable()) {
            if (!isFileEditionAllowed()) {
                throw new WorkflowDomainException("label.error.workflowProcess.noFileEditionAvailable");
            }
            if (getFilesSet().stream().filter((file) -> file.getDisplayName().equals(displayName)).findAny().isPresent()) {
                throw new DuplicateProcessFileNameException("no.files.same.name.allowed.rename.pls", displayName);
            }
            Constructor<T> fileConstructor = instanceToCreate.getConstructor(String.class, String.class, byte[].class);
            T file = null;
            try {
                file = fileConstructor.newInstance(new Object[] { displayName, filename, consumeInputStream });
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof WriteOnReadError) {
                    throw (WriteOnReadError) e.getCause();
                }
                throw new Error(e);
            }

            file.fillInNonDefaultFields(bean);

            file.preProcess(bean);
            addFiles(file);
            file.postProcess(bean);
            new FileUploadLog(this, Authenticate.getUser(), file.getFilename(), file.getDisplayName(),
                    WorkflowClassUtil.getNameForType(file.getClass()), bean.getExtraArguments());

            return file;
        }
        throw new WorkflowDomainException("label.error.workflowProcess.noSupportForFiles");

    }

    @Override
    public void addFiles(ProcessFile file) {
        file.validateUpload(this);
        super.addFiles(file);
        if (!(isFileSupportAvailable() && isAccessibleToCurrentUser() && isFileEditionAllowed())) {
            throw new WorkflowDomainException("error.current.user.cannot.write.on.document");
        }
    }

    @Override
    @Deprecated
    public void setCurrentOwner(User currentOwner) {
        throw new WorkflowDomainException("error.message.illegal.method.useTakeInstead");
    }

    public void systemProcessRelease() {
        super.setCurrentOwner(null);
    }

    @Atomic
    public void takeProcess() {
        final User currentOwner = getCurrentOwner();
        final User currentUser = Authenticate.getUser();
        if (currentOwner != currentUser) {
            if (currentOwner != null) {
                throw new WorkflowDomainException("error.message.illegal.method.useStealInstead");
            }
            super.setCurrentOwner(currentUser);
        }
    }

    @Atomic
    public void releaseProcess() {
        final User loggedPerson = Authenticate.getUser();
        final User person = getCurrentOwner();
        if (loggedPerson != null && person != null && loggedPerson != person) {
            throw new WorkflowDomainException("error.message.illegal.state.unableToReleaseATicketNotOwnerByUser");
        }
        super.setCurrentOwner(null);
    }

    @Atomic
    public void stealProcess() {
        super.setCurrentOwner(Authenticate.getUser());
    }

    public void giveProcess(User user) {
        final User currentOwner = getCurrentOwner();
        final User currentUser = Authenticate.getUser();
        if (currentOwner != null && currentOwner != currentUser) {
            throw new WorkflowDomainException("error.message.illegal.state.unableToGiveAnAlreadyTakenProcess");
        }
        super.setCurrentOwner(user);
    }

    public boolean isUserCurrentOwner() {
        final User loggedPerson = Authenticate.getUser();
        return loggedPerson != null && loggedPerson == getCurrentOwner();
    }

    public boolean isTakenByPerson(User person) {
        return person != null && person == getCurrentOwner();
    }

    public boolean isTakenByCurrentUser() {
        final User loggedPerson = Authenticate.getUser();
        return loggedPerson != null && isTakenByPerson(loggedPerson);
    }

    public boolean isUserCanViewLogs(User user) {
        return true;
    }

    public boolean isCurrentUserCanViewLogs() {
        return isUserCanViewLogs(Authenticate.getUser());
    }

    public boolean isCurrentUserAbleToAccessAnyQueues() {
        for (WorkflowQueue queue : getCurrentQueues()) {
            if (queue.isCurrentUserAbleToAccessQueue()) {
                return true;
            }
        }
        return false;
    }

    public boolean isCreatedByAvailable() {
        return true;
    }

    @SuppressWarnings("unchecked")
    public <T extends ActivityLog> T logExecution(User person, String operationName, String... args) {
        return (T) new ActivityLog(this, person, operationName, args);
    }

    @Atomic
    @Override
    public void removeFiles(ProcessFile file) {
        if (!file.isPossibleToArchieve()) {
            throw new WorkflowDomainException("error.invalidOperation.tryingToRemoveFileWhenIsNotPossible");
        }
        super.removeFiles(file);
        addDeletedFiles(file);
        file.processRemoval();
        String nameToLog = file.getDisplayName() != null ? file.getDisplayName() : file.getFilename();
        new FileRemoveLog(this, Authenticate.getUser(), file.getFilename(), nameToLog, WorkflowClassUtil.getNameForType(file
                .getClass()));

    }

    public List<WorkflowProcessComment> getUnreadCommentsForCurrentUser() {
        return getUnreadCommentsForUser(Authenticate.getUser());
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

    public boolean hasUnreadCommentsForCurrentUser() {
        User user = Authenticate.getUser();
        return hasUnreadCommentsForUser(user);
    }

    /**
     * When interfaces need to display several processes, which may or may not
     * be from different kinds, this method should be used to render a textual
     * description of the instance.
     * 
     * @return A localized string with the process description.
     */
    public String getDescription() {
        return getClass().getSimpleName() + " " + getProcessNumber();
    }

    @Atomic
    public void markCommentsAsReadForUser(User user) {
        for (WorkflowProcessComment comment : getComments()) {
            if (comment.isUnreadBy(user)) {
                comment.addReaders(user);
            }
        }
    }

    /**
     * By default a workflow process supports documents attached to it, although
     * if there's a process where there should be no support for files, override
     * this method to return false. No upload box will be rendered in the
     * interface and calling the WorkflowProcess#addDocument(Class, String, String, byte[], WorkflowFileUploadBean) will
     * result in an exception.
     * 
     * 
     * @return true if the process supports documents
     */
    public boolean isDocumentSupportAvailable() {
        return true;
    }

    /**
     * By default a workflow process supports documents attached to it, although
     * if there's a process where there should be no support for files, override
     * this method to return false. No upload box will be rendered in the
     * interface and calling the {@link WorkflowProcess#addFile(Class, String, String, byte[], WorkflowFileUploadBean)} will
     * result in an exception.
     * 
     * 
     * @return true if the process supports files
     */
    public boolean isFileSupportAvailable() {
        return true;
    }

    /**
     * By default the edition (removal/upload) of files will be allowed. But
     * this method should be overwritten to allow more intricate conditions to
     * be applied here.
     * 
     * @author João Antunes
     * @param userEditingFiles userEditingFiles
     * @return true if file removeal/upload should be allowed, false otherwise
     */
    public boolean isFileEditionAllowed(User userEditingFiles) {
        return true;
    }

    public boolean isFileEditionAllowed() {
        return isFileEditionAllowed(Authenticate.getUser());
    }

    /**
     * A process may or may not have comments, by default it has. If this method
     * returns false, no link for the comments will be rendered in the
     * interface.
     * 
     * @return true if the process supports comments
     */
    public boolean isCommentsSupportAvailable() {
        return true;
    }

    /**
     * @return true if the comments should be displayed in-line in the main page
     *         of the process, false if the default link on the top right corner
     *         should be used instead
     */
    public boolean isCommentsDisplayedInBody() {
        return false;
    }

    /**
     * By ticket support we mean support for Take/Steal/Give/Release operations.
     * If this method returns false, the interface will stop having this
     * operations. Although the WorkflowProcess methods - currently - don't
     * ensure that the system is active when they're invoked.
     * 
     * 
     * @return true if the process supports ticket system
     */
    public boolean isTicketSupportAvailable() {
        return true;
    }

    /**
     * An observer is someone that can access the process although it cannot do
     * any of it's activities.
     * 
     * To use this system you also have to add the {@link module.workflow.activities.AddObserver} and
     * {@link module.workflow.activities.RemoveObserver} activities to your
     * process.
     * 
     * Keep in mind that if the access control of the process has been overriden
     * through {@link WorkflowProcess#isAccessible(User)} that method must
     * implement also the observer logic.
     * 
     * @return true if the process supports observers
     */
    public boolean isObserverSupportAvailable() {
        return true;
    }

    /**
     * This list represents all the kind of classes that {@link WorkflowProcess#addFiles(ProcessFile)} supports. If
     * {@link WorkflowProcess#addFiles(ProcessFile)} is invoked with some class
     * that is not in this list an exception is thrown.
     * 
     * @return list of classes that extends ProcessFile that are allowed to add
     *         to the process
     */
    @SuppressWarnings("static-method")
    public List<Class<? extends ProcessFile>> getAvailableFileTypes() {
        List<Class<? extends ProcessFile>> availableClasses = new ArrayList<Class<? extends ProcessFile>>();
        availableClasses.add(ProcessFile.class);
        return availableClasses;
    }

/**
     * This list represents all the kind of classes that
     * that is not in this list an exception is thrown.
     * 
     * @return list of classes that extends {@link ProcessFile} that are allowed to add
     *         to the process
     */
    @SuppressWarnings("static-method")
    public List<Class<? extends ProcessFile>> getAvailableDocumentTypes() {
        List<Class<? extends ProcessFile>> availableClasses = new ArrayList<Class<? extends ProcessFile>>();
        availableClasses.add(ProcessFile.class);
        return availableClasses;
    }

    /**
     * This list has to be a subset or the actual list returned by {@link WorkflowProcess#getAvailableFileTypes()}. Only the files
     * in this
     * list will be rendered as possible file options in the upload interface.
     * 
     * @return list of classes that extends ProcessFile that are able to be
     *         uploaded
     */
    public List<Class<? extends ProcessFile>> getUploadableFileTypes() {
        return getAvailableFileTypes();
    }

    /**
     * This list has to be a subset or the actual list returned by {@link WorkflowProcess#getAvailableDocumentTypes()}. Only the
     * files in
     * this list will be rendered as possible file options in the upload
     * interface.
     * 
     * @return list of classes that extends {@link ProcessFile} that are able to
     *         be uploaded
     */
    public List<Class<? extends ProcessFile>> getUploadableFileDocumentTypes() {
        return getAvailableDocumentTypes();
    }

    /**
     * This list has to be a subset or the actual list returned by {@link WorkflowProcess#getAvailableFileTypes()}. Only the files
     * in this
     * list will be rendered in the process page's file listing.
     * 
     * @return list of classes that extends ProcessFile that are displayed in
     *         the process page
     */
    public List<Class<? extends ProcessFile>> getDisplayableFileTypes() {
        return getAvailableFileTypes();
    }

    /*
     * This list has to be a subset or the actual list returned by WorkflowProcess#getDisplayableFileDocumentTypes() Only
     * the files
     * in this list will be rendered in the process page's file listing.
     * 
     * @return list of classes that extends ProcessFile that are displayed in
     *         the process page public List Class ? extends ProcessFile
     *         getDisplayableFileDocumentTypes() { return
     *         getAvailableDocumentTypes(); }
     */

    public <T extends ProcessFile> List<T> getFiles(Class<? extends ProcessFile> selectedClass) {
        List<Class<? extends ProcessFile>> list = new ArrayList<Class<? extends ProcessFile>>();
        list.add(selectedClass);
        return getFilesFromList(getFiles(), list);
    }

    private <T extends ProcessFile> List<T> getFilesFromList(Collection<ProcessFile> list,
            List<Class<? extends ProcessFile>> selectedClasses) {
        List<T> classes = new ArrayList<T>();
        for (ProcessFile file : list) {
            for (Class selectedClass : selectedClasses) {
                if (file.getClass() == selectedClass) {
                    classes.add((T) file);
                }

            }
        }
        return classes;
    }

    public <T extends ProcessFile> List<T> getFilesIncludingDeleted(List<Class<? extends ProcessFile>> selectedClasses,
            boolean sortedByFileName) {
        ArrayList<ProcessFile> classes = new ArrayList<ProcessFile>();
        classes.addAll(getFiles());
        classes.addAll(getDeletedFiles());
        if (!sortedByFileName) {
            return getFilesFromList(classes, selectedClasses);
        }
        ArrayList<T> processFiles = (ArrayList<T>) getFilesFromList(classes, selectedClasses);
        Collections.sort(processFiles, (o1, o2) -> o1.getFilename().compareTo(o2.getFilename()));
        return processFiles;

    }

    @Override
    public void addObservers(User observer) {
        if (getObservers().contains(observer)) {
            throw new WorkflowDomainException("error.workflowProcess.addingExistingObserver");
        }
        super.addObservers(observer);
    }

    public boolean isUserObserver(User user) {
        return getObservers().contains(user);
    }

    /**
     * The WorkflowProcess is integrated with the LuceneSearchPlugin to use
     * lucene's indexing capabilities. It is allowed to index the content of
     * files such as text files, excel, html, pdf, etc. But by default it does
     * not index the files that are attached to the process.
     * 
     * @return true if files should also be indexed
     */
    protected boolean isFileIndexingEnabled() {
        return false;
    }

    /**
     * The WorkflowProcess is integrated with the LuceneSearchPlugin to use
     * lucene's indexing capabilities. It is allowed to specify that a certain
     * process wants their comments also indexed. But by default it does not
     * index the process' comments.
     * 
     * @return true if comments should also be indexed
     */
    protected boolean isCommentingIndexingEnabled() {
        return false;
    }

    public Collection<? extends WorkflowLog> getExecutionLogs(final Class<? extends WorkflowLog>... classes) {
        final Collection<WorkflowLog> result = new ArrayList<WorkflowLog>();
        for (final WorkflowLog workflowLog : getExecutionLogsSet()) {
            if (match(workflowLog, classes)) {
                result.add(workflowLog);
            }
        }
        return result;
    }

    private boolean match(WorkflowLog workflowLog, Class<? extends WorkflowLog>[] classes) {
        for (final Class clazz : classes) {
            if (clazz.isAssignableFrom(workflowLog.getClass())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void removeCurrentQueues(WorkflowQueue queue) {
        if (getCurrentQueues().contains(queue)) {
            addQueueHistory(queue);
        }
        super.removeCurrentQueues(queue);
    }

    public boolean hasLogOfBeingExecuted(Class<? extends WorkflowActivity> clazz) {
        return hasLogOfBeingExecuted(clazz, 1);
    }

    public boolean hasLogOfBeingExecuted(Class<? extends WorkflowActivity> clazz, int count) {
        String operationName = clazz.getSimpleName();
        List<ActivityLog> activitiesExecution = (List<ActivityLog>) getExecutionLogs(ActivityLog.class);
        int counter = 0;
        for (ActivityLog log : activitiesExecution) {
            if (log.getOperation().equalsIgnoreCase(operationName)) {
                counter++;
            }
            if (counter == count) {
                return true;
            }
        }

        return false;
    }

    public DateTime getCreationDate() {
        final WorkflowLog log = findFirstLogEntry();
        return log == null ? new DateTime() : log.getWhenOperationWasRan();

    }

    private WorkflowLog findFirstLogEntry() {
        WorkflowLog result = null;
        for (final WorkflowLog log : getExecutionLogsSet()) {
            if (result == null || log.isBefore(result)) {
                result = log;
            }
        }
        return result;
    }

    public boolean hasUnreadCommentsForUser(User user) {
        List<WorkflowProcessComment> comments = new ArrayList<WorkflowProcessComment>();
        for (WorkflowProcessComment comment : getComments()) {
            if (comment.isUnreadBy(user)) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public java.util.Set<org.fenixedu.bennu.core.domain.User> getObservers() {
        return getObserversSet();
    }

    @Deprecated
    public java.util.Set<module.workflow.domain.WorkflowQueue> getQueueHistory() {
        return getQueueHistorySet();
    }

    @Deprecated
    public java.util.Set<module.workflow.domain.WorkflowProcessComment> getComments() {
        return getCommentsSet();
    }

    @Deprecated
    public java.util.Set<module.workflow.domain.WorkflowQueue> getCurrentQueues() {
        return getCurrentQueuesSet();
    }

    @Deprecated
    public java.util.Set<module.workflow.domain.WorkflowLog> getExecutionLogs() {
        return getExecutionLogsSet();
    }

    @Deprecated
    public java.util.Set<module.workflow.domain.ProcessFile> getDeletedFiles() {
        return getDeletedFilesSet();
    }

    @Deprecated
    public java.util.Set<module.workflow.domain.ProcessFile> getFiles() {
        return getFilesSet();
    }

}
