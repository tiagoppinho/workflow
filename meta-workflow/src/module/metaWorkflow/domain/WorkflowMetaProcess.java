package module.metaWorkflow.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import module.metaWorkflow.activities.ChangeQueue;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.GiveProcess;
import module.workflow.activities.ReleaseProcess;
import module.workflow.activities.StealProcess;
import module.workflow.activities.TakeProcess;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.GenericFile;
import module.workflow.domain.WorkflowProcess;
import myorg.applicationTier.Authenticate.UserView;

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
    }

    public WorkflowMetaProcess(WorkflowMetaType type, String instanceDescription) {
	super();
	setMetaType(type);
	setProcessNumber(WorkflowMetaProcessIdentifierCounter.getProcessIdentifierCounter().getNextIdentifier());
	setCreator(UserView.getCurrentUser());
	setCreationDate(new DateTime());
	setInstanceDescription(instanceDescription);
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

    @Service
    public static WorkflowMetaProcess createNewProcess(WorkflowMetaType metaType, String instanceDescription) {
	return new WorkflowMetaProcess(metaType, instanceDescription);
    }
}
