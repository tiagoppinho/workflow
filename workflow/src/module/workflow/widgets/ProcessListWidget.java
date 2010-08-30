package module.workflow.widgets;

import java.util.HashMap;
import java.util.Map;

import module.dashBoard.presentationTier.WidgetRequest;
import module.dashBoard.widgets.WidgetController;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowSystem;
import myorg.domain.User;
import myorg.util.BundleUtil;
import myorg.util.ClassNameBundle;
import myorg.util.Counter;

@ClassNameBundle(bundle = "resources/WorkflowResources", key = "widget.title.processListWidget")
public class ProcessListWidget extends WidgetController {

    @Override
    public void doView(WidgetRequest request) {
	User requestingUser = request.getCurrentUser();
	Map<Class<? extends WorkflowProcess>, Counter<Class<? extends WorkflowProcess>>> map = new HashMap<Class<? extends WorkflowProcess>, Counter<Class<? extends WorkflowProcess>>>();
	for (WorkflowProcess process : WorkflowSystem.getInstance().getProcesses()) {
	    if (process.isAccessible(requestingUser) && process.hasAnyAvailableActivity(requestingUser, true)) {
		register(map, process);
	    }
	}

	request.setAttribute("pendingProcessList", map.values());
    }

    private void register(Map<Class<? extends WorkflowProcess>, Counter<Class<? extends WorkflowProcess>>> map,
	    WorkflowProcess process) {
	Counter<Class<? extends WorkflowProcess>> counter = map.get(process.getClass());
	if (counter == null) {
	    counter = new Counter(process.getClass());
	    map.put(process.getClass(), counter);
	}
	counter.increment();
    }

    @Override
    public String getWidgetDescription() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkflowResources", "widget.description.processListWidget");
    }

    @Override
    public boolean isHelpModeSupported() {
	return true;
    }

    @Override
    public String getHelp() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkflowResources", "widget.help.processListWidget");
    }
}
