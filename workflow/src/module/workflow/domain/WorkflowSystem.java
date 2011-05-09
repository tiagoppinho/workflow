package module.workflow.domain;

import javax.servlet.http.HttpServletRequest;

import module.dashBoard.WidgetRegister;
import module.workflow.widgets.ProcessListWidget;
import module.workflow.widgets.QuickViewWidget;
import module.workflow.widgets.UnreadCommentsWidget;
import myorg.domain.VirtualHost;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter.ChecksumPredicate;

public class WorkflowSystem extends WorkflowSystem_Base {

    static {
	WidgetRegister.registerWidget(ProcessListWidget.class);
	WidgetRegister.registerWidget(QuickViewWidget.class);
	WidgetRegister.registerWidget(UnreadCommentsWidget.class);

	RequestChecksumFilter.registerFilterRule(new ChecksumPredicate() {
	    @Override
	    public boolean shouldFilter(HttpServletRequest httpServletRequest) {
		return !(httpServletRequest.getRequestURI().endsWith("/workflowProcessManagement.do")
			&& httpServletRequest.getQueryString() != null && httpServletRequest.getQueryString().contains(
			"method=viewTypeDescription"));
	    }
	});
    }

    private WorkflowSystem(final VirtualHost virtualHost) {
	super();
	virtualHost.setWorkflowSystem(this);
    }

    public static WorkflowSystem getInstance() {
	final VirtualHost virtualHostForThread = VirtualHost.getVirtualHostForThread();
	return virtualHostForThread == null ? null : virtualHostForThread.getWorkflowSystem();
    }

    @Service
    public static void createSystem(final VirtualHost virtualHost) {
	if (!virtualHost.hasWorkflowSystem() || virtualHost.getWorkflowSystem().getVirtualHostCount() > 1) { 
	    new WorkflowSystem(virtualHost);
	}
    }

    @Service
    public void setForVirtualHost(final VirtualHost virtualHost) {
	virtualHost.setWorkflowSystem(this);
    }

}
