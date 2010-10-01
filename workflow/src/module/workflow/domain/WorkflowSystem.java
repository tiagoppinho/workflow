package module.workflow.domain;

import javax.servlet.http.HttpServletRequest;

import module.dashBoard.WidgetRegister;
import module.workflow.widgets.ProcessListWidget;
import module.workflow.widgets.QuickViewWidget;
import myorg.domain.ModuleInitializer;
import myorg.domain.MyOrg;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter.ChecksumPredicate;

public class WorkflowSystem extends WorkflowSystem_Base implements ModuleInitializer {

    private static boolean isInitialized = false;

    private static ThreadLocal<WorkflowSystem> init = null;

    private WorkflowSystem() {
	setMyOrg(MyOrg.getInstance());
    }

    public static WorkflowSystem getInstance() {
	if (init != null) {
	    return init.get();
	}

	if (!isInitialized) {
	    initialize();
	}
	final MyOrg myOrg = MyOrg.getInstance();
	return myOrg.getWorkflowSystem();
    }

    @Service
    public synchronized static void initialize() {
	if (!isInitialized) {
	    try {
		final MyOrg myOrg = MyOrg.getInstance();
		final WorkflowSystem system = myOrg.getWorkflowSystem();
		if (system == null) {
		    new WorkflowSystem();
		}
		init = new ThreadLocal<WorkflowSystem>();
		init.set(myOrg.getWorkflowSystem());

		isInitialized = true;
	    } finally {
		init = null;
	    }
	}
    }

    @Override
    public void init(MyOrg root) {
	WidgetRegister.registerWidget(ProcessListWidget.class);
	WidgetRegister.registerWidget(QuickViewWidget.class);

	RequestChecksumFilter.registerFilterRule(new ChecksumPredicate() {
	    public boolean shouldFilter(HttpServletRequest httpServletRequest) {
		return !(httpServletRequest.getRequestURI().endsWith("/workflowProcessManagement.do")
			&& httpServletRequest.getQueryString() != null && httpServletRequest.getQueryString().contains(
			"method=viewTypeDescription"));
	    }
	});
    }

}
