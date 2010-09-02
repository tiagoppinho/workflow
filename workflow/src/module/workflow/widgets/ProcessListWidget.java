package module.workflow.widgets;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import module.dashBoard.presentationTier.WidgetRequest;
import module.dashBoard.widgets.WidgetController;
import module.workflow.domain.ProcessCounter;
import myorg.util.BundleUtil;
import myorg.util.ClassNameBundle;

@ClassNameBundle(bundle = "resources/WorkflowResources", key = "widget.title.processListWidget")
public class ProcessListWidget extends WidgetController {

    private static final Set<ProcessCounter> processCounters = new HashSet<ProcessCounter>();

    @Override
    public void doView(WidgetRequest request) {
	final Set<ProcessCounter> processCounters = new TreeSet<ProcessCounter>(ProcessCounter.COMPARATOR);
	processCounters.addAll(this.processCounters);
	request.setAttribute("pendingProcessList", processCounters);
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

    public static void register(final ProcessCounter processCounter) {
	processCounters.add(processCounter);
    }

}
