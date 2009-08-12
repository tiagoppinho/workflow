package module.metaWorkflow.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import module.metaWorkflow.activities.ChangeQueue;
import module.metaWorkflow.activities.ChangeRequestor;
import module.metaWorkflow.activities.CloseMetaProcess;
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
import module.workflow.presentationTier.WorkflowLayoutContext;
import module.workflow.presentationTier.actions.ProcessManagement;
import module.workflow.presentationTier.actions.ProcessManagement.ProcessRequestHandler;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import myorg.domain.index.IndexDocument;
import myorg.util.VariantBean;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.services.Service;

public class WorkflowMetaProcess extends WorkflowMetaProcess_Base {

    public static Map<String, WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> activityMap = new HashMap<String, WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>>();
    private static final String subjectKey;
    private static final String descriptionKey;

    private static final String queueKey;
    private static final String requestorKey;
    private static final String creatorKey;

    static {
	activityMap.put(TakeProcess.class.getSimpleName(), new TakeProcess());
	activityMap.put(ReleaseProcess.class.getSimpleName(), new ReleaseProcess());
	activityMap.put(StealProcess.class.getSimpleName(), new StealProcess());
	activityMap.put(GiveProcess.class.getSimpleName(), new GiveProcess());
	activityMap.put(ChangeQueue.class.getSimpleName(), new ChangeQueue());
	activityMap.put(CloseMetaProcess.class.getSimpleName(), new CloseMetaProcess());
	activityMap.put(OpenMetaProcess.class.getSimpleName(), new OpenMetaProcess());
	activityMap.put(AddObserver.class.getSimpleName(), new AddObserver());
	activityMap.put(RemoveObserver.class.getSimpleName(), new RemoveObserver());
	activityMap.put(ChangeRequestor.class.getSimpleName(), new ChangeRequestor());

	// Index names

	subjectKey = "subject";
	descriptionKey = "description";
	queueKey = "queue";
	requestorKey = "requestor";
	creatorKey = "creator";

    }

    public WorkflowMetaProcess(WorkflowMetaType type, String subject, String instanceDescription, WorkflowQueue queue,
	    Requestor requestor) {
	super();
	setMetaType(type);
	setSubject(subject);
	setProcessNumber(new LocalDate().getYear() + "-" + type.getNextIdentifier());
	setCreator(UserView.getCurrentUser());
	setCreationDate(new DateTime());
	setInstanceDescription(instanceDescription);
	open();
	setCurrentQueue(queue);
	setRequestor(requestor);
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
	context.setHead("/metaWorkflow/layoutHead.jsp");
	return context;
    }

    @Override
    public List<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> getActivities() {
	List<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> list = new ArrayList<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>>();
	list.addAll(activityMap.values());
	return list;
    }

    @Override
    public void setCurrentQueue(WorkflowQueue currentQueue) {
	if (getCurrentQueue() != null) {
	    addQueueHistory(getCurrentQueue());
	}
	super.setCurrentQueue(currentQueue);
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
    public boolean isCommentsSupportAvailable() {
	return false;
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

    public boolean isUserMetaMetaTypeObserver(User user) {
	return getMetaType().getMetaTypeObservers().contains(user);
    }

    @Override
    public boolean isAccessible(User user) {
	return isTakenByPerson(user) || getCreator() == user || isUserObserver(user) || isUserAbleToAccessCurrentQueue(user)
		|| isUserAbleToAccessPastQueues(user);
    }

    public boolean isUserAbleToAccessCurrentQueue(User user) {
	WorkflowQueue currentQueue = getCurrentQueue();
	return currentQueue != null ? currentQueue.isUserAbleToAccessQueue(user) : false;
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
    public IndexDocument getDocumentToIndex() {
	IndexDocument document = super.getDocumentToIndex();
	document.indexField(subjectKey, this.getSubject());
	document.indexField(descriptionKey, this.getInstanceDescription());

	document.indexField(queueKey, getCurrentQueue().getName());
	document.indexField(requestorKey, getRequestor().getUser().getPresentationName());
	document.indexField(creatorKey, getCreator().getPresentationName());

	return document;
    }

    @Service
    public static WorkflowMetaProcess createNewProcess(String subject, String instanceDescription, WorkflowQueue queue, User user) {
	Requestor requestor = user.hasRequestor() ? user.getRequestor() : new UserRequestor(user);

	return new WorkflowMetaProcess(queue.getMetaType(), subject, instanceDescription, queue, requestor);
    }

}
