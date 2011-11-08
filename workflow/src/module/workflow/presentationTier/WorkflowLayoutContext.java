package module.workflow.presentationTier;

import module.workflow.domain.WorkflowProcess;
import myorg.presentationTier.LayoutContext;

public class WorkflowLayoutContext extends LayoutContext {

    public final static String DEFAULT_BODY = "body.jsp";
    public final static String DEFAULT_SHORT_BODY = "shortBody.jsp";
    public final static String DEFAULT_HEAD = "header.jsp";

    protected String workflowBody;
    protected String workflowShortBody;
    protected String workflowHead;

    public WorkflowLayoutContext() {
	super();
    }

    public WorkflowLayoutContext(final String path) {
	super(path);
    }

    public String getWorkflowShortBody() {
	return workflowShortBody;
    }

    public void setWorkflowShortBody(String workflowShortBody) {
	this.workflowShortBody = workflowShortBody;
    }

    public String getWorkflowBody() {
	return workflowBody;
    }

    public void setWorkflowBody(String workflowBody) {
	this.workflowBody = workflowBody;
    }

    public String getWorkflowHead() {
	return workflowHead;
    }

    public void setWorkflowHead(String workflowHead) {
	this.workflowHead = workflowHead;
    }

    public static WorkflowLayoutContext getDefaultWorkflowLayoutContext(Class<? extends WorkflowProcess> processClass) {
	WorkflowLayoutContext context = new WorkflowLayoutContext();
	String folder = processClass.getName().replace(".", "/");
	context.setWorkflowBody("/" + folder + "/" + DEFAULT_BODY);
	context.setWorkflowHead("/" + folder + "/" + DEFAULT_HEAD);
	context.setWorkflowShortBody("/" + folder + "/" + DEFAULT_SHORT_BODY);

	return context;
    }
}
