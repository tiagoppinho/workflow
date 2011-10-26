package module.metaWorkflow.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import module.workflow.domain.ProcessFile;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowQueue;
import module.workflow.presentationTier.WorkflowLayoutContext;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import myorg.domain.VirtualHost;
import myorg.domain.exceptions.DomainException;
import myorg.util.BundleUtil;
import myorg.util.ClassNameBundle;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.emailNotifier.domain.Email;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.plugins.luceneIndexing.domain.IndexDocument;

@ClassNameBundle(key = "label.module.metaWorkflow", bundle = "resources/MetaWorkflowResources")
public class WorkflowMetaProcess extends WorkflowMetaProcess_Base {

    public static Map<String, WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> activityMap = new HashMap<String, WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>>();
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
	List<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> list = new ArrayList<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>>();
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
    public List<User> getObservers() {
	List<User> observers = new ArrayList<User>();
	observers.addAll(super.getObservers());
	observers.addAll(getMetaType().getMetaTypeObservers());
	return observers;
    }

    public List<User> getMetaTypeObservers() {
	return getMetaType().getMetaTypeObservers();
    }

    public List<User> getProcessObservers() {
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

    @Service
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
	final String email = user.getPerson().getRemotePerson().getEmailForSendingEmails();
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

}
