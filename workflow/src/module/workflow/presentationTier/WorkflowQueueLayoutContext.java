package module.workflow.presentationTier;

import java.util.HashMap;
import java.util.Map;

import module.workflow.domain.WorkflowQueue;
import myorg.presentationTier.LayoutContext;

public class WorkflowQueueLayoutContext extends LayoutContext {

    private static Map<Class<? extends WorkflowQueue>, String> creationPageRegister = new HashMap<Class<? extends WorkflowQueue>, String>();

    public static final String DEFAULT_CREATE = "create.jsp";
    public static final String DEFAULT_EDIT = "edit.jsp";
    public static final String DEFAULT_VIEW = "view.jsp";

    private String create;
    private String edit;
    private String view;

    public String getCreateQueue() {
	return create;
    }

    public void setCreateQueue(String create) {
	this.create = create;
    }

    public String getEditQueue() {
	return edit;
    }

    public void setEditQueue(String edit) {
	this.edit = edit;
    }

    public String getViewQueue() {
	return view;
    }

    public void setViewQueue(String view) {
	this.view = view;
    }

    public static void registerCreationPage(Class<? extends WorkflowQueue> instanceType, String page) {
	creationPageRegister.put(instanceType, page);
    }

    public static String getBootstrapFor(Class<? extends WorkflowQueue> instanceType) {
	String page = creationPageRegister.get(instanceType);
	return page == null ? "/" + instanceType.getName().replace(".", "/") + "/" + DEFAULT_CREATE : page;
    }

    public static WorkflowQueueLayoutContext getDefaultLayout(WorkflowQueue queue) {
	String folder = "/" + queue.getClass().getName().replace(".", "/") + "/";
	WorkflowQueueLayoutContext layout = new WorkflowQueueLayoutContext();
	layout.setCreateQueue(folder + DEFAULT_CREATE);
	layout.setEditQueue(folder + DEFAULT_EDIT);
	layout.setViewQueue(folder + DEFAULT_VIEW);

	return layout;
    }
}
