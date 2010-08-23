package module.metaWorkflow.domain;

import javax.servlet.http.HttpServletRequest;

import module.dashBoard.WidgetRegister.WidgetAditionPredicate;
import module.dashBoard.domain.DashBoardPanel;
import module.metaWorkflow.presentationTier.actions.OrganizationModelPluginAction.QueueView;
import module.organization.presentationTier.actions.OrganizationModelAction;
import module.workflow.presentationTier.actions.ProcessManagement;
import module.workflow.presentationTier.actions.ProcessManagement.ProcessRequestHandler;
import myorg.domain.ModuleInitializer;
import myorg.domain.MyOrg;
import myorg.domain.User;
import myorg.util.VariantBean;
import pt.ist.fenixWebFramework.services.Service;

public class MetaWorkflowInitializer extends MetaWorkflowInitializer_Base implements ModuleInitializer {

    private static boolean isInitialized = false;

    private static ThreadLocal<MetaWorkflowInitializer> init = null;

    private MetaWorkflowInitializer() {
	setMyOrg(MyOrg.getInstance());
    }

    public static MetaWorkflowInitializer getInstance() {
	if (init != null) {
	    return init.get();
	}

	if (!isInitialized) {
	    initialize();
	}
	final MyOrg myOrg = MyOrg.getInstance();
	return myOrg.getMetaWorkflowInitializer();
    }

    @Service
    public synchronized static void initialize() {
	if (!isInitialized) {
	    try {
		final MyOrg myOrg = MyOrg.getInstance();
		final MetaWorkflowInitializer initializer = myOrg.getMetaWorkflowInitializer();
		if (initializer == null) {
		    new MetaWorkflowInitializer();
		}
		init = new ThreadLocal<MetaWorkflowInitializer>();
		init.set(myOrg.getMetaWorkflowInitializer());

		isInitialized = true;
	    } finally {
		init = null;
	    }
	}
    }

    @Override
    public void init(MyOrg root) {

	ProcessManagement.registerProcessRequestHandler(WorkflowMetaProcess.class,
		new ProcessRequestHandler<WorkflowMetaProcess>() {

		    @Override
		    public void handleRequest(WorkflowMetaProcess process, HttpServletRequest request) {
			request.setAttribute("commentBean", new VariantBean());

		    }
		});

	OrganizationModelAction.partyViewHookManager.register(new QueueView());
    }

}
