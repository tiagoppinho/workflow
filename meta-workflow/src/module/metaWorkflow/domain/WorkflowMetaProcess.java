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
import module.workflow.domain.GenericFile;
import module.workflow.domain.WorkflowProcess;
import module.workflow.presentationTier.actions.ProcessManagement;
import module.workflow.presentationTier.actions.ProcessManagement.ProcessRequestHandler;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import myorg.util.VariantBean;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class WorkflowMetaProcess extends WorkflowMetaProcess_Base {

    public static Map<String, WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> activityMap = new HashMap<String, WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>>();

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

	// Registering here the request handler, it should be done in other
	// place though, such as module init. Although we still do not have
	// it. So for now it's here.

	ProcessManagement.registerProcessRequestHandler(WorkflowMetaProcess.class,
		new ProcessRequestHandler<WorkflowMetaProcess>() {

		    @Override
		    public void handleRequest(WorkflowMetaProcess process, HttpServletRequest request) {
			request.setAttribute("commentBean", new VariantBean());

		    }
		});

    }

    public WorkflowMetaProcess(WorkflowMetaType type, String instanceDescription, WorkflowQueue queue) {
	super();
	setMetaType(type);
	setProcessNumber(type.getNextIdentifier());
	setCreator(UserView.getCurrentUser());
	setCreationDate(new DateTime());
	setInstanceDescription(instanceDescription);
	open();
	setCurrentQueue(queue);
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
    public List<Class<? extends GenericFile>> getAvailableFileTypes() {
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

    @Service
    public static WorkflowMetaProcess createNewProcess(WorkflowMetaType metaType, String instanceDescription, WorkflowQueue queue) {
	return new WorkflowMetaProcess(metaType, instanceDescription, queue);
    }

    @Override
    public boolean isAccessible(User user) {
	return getCurrentOwner() == user || getCreator() == user || isUserObserver(user)
		|| getCurrentQueue().isUserAbleToAccessQueue(user) || isUserAbleToAccessPastQueues(user);
    }

    private boolean isUserAbleToAccessPastQueues(User user) {
	for (WorkflowQueue queue : getQueueHistory()) {
	    if (queue.isUserAbleToAccessQueue(user)) {
		return true;
	    }
	}
	return false;
    }

}
