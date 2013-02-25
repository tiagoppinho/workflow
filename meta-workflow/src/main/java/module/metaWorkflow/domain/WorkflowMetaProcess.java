/*
 * @(#)WorkflowMetaProcess.java
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
package module.metaWorkflow.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import jvstm.cps.ConsistencyPredicate;
import module.metaWorkflow.activities.ChangeMetaQueue;
import module.metaWorkflow.activities.ChangeRequestor;
import module.metaWorkflow.activities.CloseMetaProcess;
import module.metaWorkflow.activities.EditFieldValue;
import module.metaWorkflow.activities.OpenMetaProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.AddObserver;
import module.workflow.activities.GiveProcess;
import module.workflow.activities.ReleaseProcess;
import module.workflow.activities.RemoveObserver;
import module.workflow.activities.StealProcess;
import module.workflow.activities.TakeProcess;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.AbstractWFDocsGroup;
import module.workflow.domain.ProcessDocumentMetaDataResolver;
import module.workflow.domain.ProcessFile;
import module.workflow.domain.WFDocsDefaultWriteGroup;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowQueue;
import module.workflow.presentationTier.WorkflowLayoutContext;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.bennu.core.util.ClassNameBundle;
import pt.ist.emailNotifier.domain.Email;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.plugins.luceneIndexing.domain.IndexDocument;

@ClassNameBundle(key = "label.module.metaWorkflow", bundle = "resources/MetaWorkflowResources")
/**
 * 
 * @author João Neves
 * @author João Antunes
 * @author Anil Kassamali
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class WorkflowMetaProcess extends WorkflowMetaProcess_Base {

    public static Map<String, WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> activityMap =
            new HashMap<String, WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>>();
    private static final String subjectKey = "subject";
    private static final String descriptionKey = "description";

    private static final String queueKey = "queue";
    private static final String requestorKey = "requestor";
    private static final String creatorKey = "creator";

    static {
        activityMap.put(TakeProcess.class.getSimpleName(), new TakeProcess());
        activityMap.put(ReleaseProcess.class.getSimpleName(), new ReleaseProcess());
        activityMap.put(StealProcess.class.getSimpleName(), new StealProcess());
        activityMap.put(GiveProcess.class.getSimpleName(), new GiveProcess());
        activityMap.put(ChangeMetaQueue.class.getSimpleName(), new ChangeMetaQueue());
        activityMap.put(CloseMetaProcess.class.getSimpleName(), new CloseMetaProcess());
        activityMap.put(OpenMetaProcess.class.getSimpleName(), new OpenMetaProcess());
        activityMap.put(AddObserver.class.getSimpleName(), new AddObserver());
        activityMap.put(RemoveObserver.class.getSimpleName(), new RemoveObserver());
        activityMap.put(ChangeRequestor.class.getSimpleName(), new ChangeRequestor());
        activityMap.put(EditFieldValue.class.getSimpleName(), new EditFieldValue());
    }

    protected WorkflowMetaProcess() {
        super();
    }

    protected WorkflowMetaProcess(WorkflowMetaType type, String subject, String instanceDescription, WorkflowQueue queue,
            Requestor requestor) {
        this();
        init(type, subject, instanceDescription, queue, requestor);
    }

    @ConsistencyPredicate
    public final boolean checkMetaTypeRequired() {
        return hasMetaType();
    }

    @ConsistencyPredicate
    public final boolean checkSubjectRequired() {
        return !StringUtils.isEmpty(getSubject());
    }

    @ConsistencyPredicate
    public final boolean checkInstanceDescriptionRequired() {
        return !StringUtils.isEmpty(getInstanceDescription());
    }

    @ConsistencyPredicate
    public final boolean checkRequestorRequired() {
        return hasRequestor();
    }

    protected void init(WorkflowMetaType type, String subject, String instanceDescription, WorkflowQueue queue,
            Requestor requestor) {
        setMetaType(type);
        setMetaTypeVersion(type.getCurrentPublishedWMTVersion());
        setSubject(subject);
        setProcessNumber(new LocalDate().getYear() + "-" + type.getNextIdentifier());
        setCreator(UserView.getCurrentUser());
        setCreationDate(new DateTime());
        setInstanceDescription(instanceDescription);
        open();
        addCurrentQueues(queue);
        setRequestor(requestor);
        super.setFieldSet(type.initValuesOfFields());
    }

    @Override
    public WorkflowLayoutContext getLayout() {
        WorkflowLayoutContext context = super.getLayout();
        if (getMetaType().hasSpecificLayout()) {
            WorkflowMetaTypeSpecificLayout specificLayout = getMetaType().getSpecificLayout();
            context.setWorkflowBody(specificLayout.getBody());
            context.setWorkflowHead(specificLayout.getHeader());
            context.setWorkflowShortBody(specificLayout.getShortBody());
        }
        context.addHead("/metaWorkflow/layoutHead.jsp");
        return context;
    }

    @Override
    public List<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> getActivities() {
        List<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> list =
                new ArrayList<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>>();
        list.addAll(activityMap.values());
        return list;
    }

    @Override
    public List<Class<? extends ProcessFile>> getAvailableFileTypes() {
        return getMetaType().getAvailableFileTypes();
    }

    @Override
    public boolean isFileSupportAvailable() {
        return getAvailableFileTypes().size() > 0;
    }

    @Override
    public boolean isCommentsDisplayedInBody() {
        return true;
    }

    @Override
    public void setOpen(Boolean open) {
        throw new UnsupportedOperationException("Error: use open() and close() methods");
    }

    public void open() {
        super.setOpen(Boolean.TRUE);
    }

    public void close() {
        super.setOpen(Boolean.FALSE);
    }

    public boolean isOpen() {
        return getOpen().booleanValue();
    }

    @Override
    public Set<User> getObservers() {
        Set<User> observers = new HashSet<User>();
        observers.addAll(super.getObservers());
        observers.addAll(getMetaType().getMetaTypeObservers());
        return observers;
    }

    public Set<User> getMetaTypeObservers() {
        return getMetaType().getMetaTypeObservers();
    }

    public Set<User> getProcessObservers() {
        return super.getObservers();
    }

    public List<WorkflowQueue> getCurrentQueuesOrdered() {
        List<WorkflowQueue> queues = new ArrayList<WorkflowQueue>(getCurrentQueues());
        Collections.sort(queues, WorkflowQueue.COMPARATOR_BY_NAME);
        return queues;
    }

    public boolean isUserMetaMetaTypeObserver(User user) {
        return getMetaType().getMetaTypeObservers().contains(user);
    }

    @Override
    public boolean isAccessible(User user) {
        return isTakenByPerson(user) || getCreator() == user || isUserObserver(user) || isUserAbleToAccessCurrentQueues(user)
                || isUserAbleToAccessPastQueues(user);
    }

    public boolean isUserAbleToAccessCurrentQueues(User user) {
        for (WorkflowQueue currentQueue : getCurrentQueues()) {
            if (currentQueue.isUserAbleToAccessQueue(user)) {
                return true;
            }
        }
        return false;
    }

    public boolean isUserAbleToAccessPastQueues(User user) {
        for (WorkflowQueue queue : getQueueHistory()) {
            if (queue.isUserAbleToAccessQueue(user)) {
                return true;
            }
        }
        return false;
    }

    public FieldValue getField(String OID) {
        return getFieldSet().findChildField(OID);
    }

    public Set<FieldValue> getAllFields() {
        return getFieldSet().getAllFields();
    }

    @Override
    public void addCurrentQueues(WorkflowQueue queue) {
        if (!queue.getMetaType().equals(getMetaType())) {
            throw new DomainException("error.queue.has.different.metaType");
        }
        super.addCurrentQueues(queue);
    }

    @Override
    public IndexDocument getDocumentToIndex() {
        IndexDocument document = super.getDocumentToIndex();
        // TODO : FIX THIS
        // document.indexField(subjectKey, this.getSubject());
        // document.indexField(descriptionKey, this.getInstanceDescription());
        //
        // document.indexField(queueKey, getCurrentQueue().getName());
        // document.indexField(requestorKey,
        // getRequestor().getUser().getPresentationName());
        // document.indexField(creatorKey, getCreator().getPresentationName());

        return document;
    }

    @Atomic
    public static WorkflowMetaProcess createNewProcess(String subject, String instanceDescription, WorkflowQueue queue, User user) {
        Requestor requestor = user.hasRequestor() ? user.getRequestor() : new UserRequestor(user);

        return new WorkflowMetaProcess(queue.getMetaType(), subject, instanceDescription, queue, requestor);
    }

    @Override
    public boolean isActive() {
        return isOpen();
    }

    @Override
    public User getProcessCreator() {
        return getCreator();
    }

    @Override
    public void notifyUserDueToComment(User user, String comment) {
        List<String> toAddress = new ArrayList<String>();
        toAddress.clear();
        final String email = user.getEmail();
        if (email != null) {
            toAddress.add(email);

            final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
            new Email(virtualHost.getApplicationSubTitle().getContent(), virtualHost.getSystemEmailAddress(), new String[] {},
                    toAddress, Collections.EMPTY_LIST, Collections.EMPTY_LIST, BundleUtil.getFormattedStringFromResourceBundle(
                            "resources/MetaWorkflowResources", "label.email.commentCreated.subject", getProcessNumber()),
                    BundleUtil.getFormattedStringFromResourceBundle("resources/MetaWorkflowResources",
                            "label.email.commentCreated.body", UserView.getCurrentUser().getPerson().getPresentationName(),
                            getProcessNumber(), comment));
        }
    }

    @Override
    public boolean isConnectedToCurrentHost() {
        final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
        return virtualHost != null && getWorkflowSystem() == virtualHost.getWorkflowSystem();
    }

    @Override
    public boolean isFileEditionAllowed(User userEditingFiles) {
        return isOpen()
                && (isUserAbleToAccessCurrentQueues(userEditingFiles) || (getCurrentOwner() != null && getCurrentOwner().equals(
                        userEditingFiles)));
    }

    public static class WorkflowMetaProcessMetadataResolver extends ProcessDocumentMetaDataResolver<ProcessFile> {

        public final static String SUBJECT = "Assunto";

        public final static String META_TYPE = "Meta tipo";

        public final static String INSTANCE_DESCRIPTION = "Informação";

        public final static String REQUESTOR = "Requisitante";

        @Override
        public @Nonnull
        Class<? extends AbstractWFDocsGroup> getWriteGroupClass() {
            return WFDocsDefaultWriteGroup.class;
        }

        @Override
        public java.util.Map<String, String> getMetadataKeysAndValuesMap(ProcessFile processDocument) {
            WorkflowMetaProcess metaProcess = (WorkflowMetaProcess) processDocument.getProcess();
            Map<String, String> metadataKeysAndValuesMap = super.getMetadataKeysAndValuesMap(processDocument);
            //	    metadataKeysAndValuesMap.put(SUBJECT, metaProcess.getSubject());
            metadataKeysAndValuesMap.put(META_TYPE, metaProcess.getMetaType().getName());
            //	    metadataKeysAndValuesMap.put(INSTANCE_DESCRIPTION, metaProcess.getInstanceDescription());
            //	    metadataKeysAndValuesMap.put(REQUESTOR, metaProcess.getRequestor().getShortName());

            return metadataKeysAndValuesMap;
        }

    }

}
