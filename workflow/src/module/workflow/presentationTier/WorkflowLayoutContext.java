package module.workflow.presentationTier;

import module.workflow.domain.WorkflowProcess;
import myorg.presentationTier.LayoutContext;

public class WorkflowLayoutContext extends LayoutContext {

    public final static String DEFAULT_BODY = "body.jsp";
    public final static String DEFAULT_SHORT_BODY = "shortBody.jsp";
    public final static String DEFAULT_HEAD = "header.jsp";

    private String workflowBody;
    private String workflowShortBody;
    private String workflowHead;
    private String oldHead;

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

    public String getOldHead() {
	return oldHead;
    }

    public void setOldHead(String oldHead) {
	this.oldHead = oldHead;
    }

    @Override
    public void setHead(String head) {
	setOldHead(getHead());
	super.setHead(head);
    }

    public static WorkflowLayoutContext getDefaultWorkflowLayoutContext(WorkflowProcess process) {
	WorkflowLayoutContext context = new WorkflowLayoutContext();
	String folder = process.getClass().getName().replace(".", "/");
	context.setWorkflowBody("/" + folder + "/" + DEFAULT_BODY);
	context.setWorkflowHead("/" + folder + "/" + DEFAULT_HEAD);
	context.setWorkflowShortBody("/" + folder + "/" + DEFAULT_SHORT_BODY);

	return context;
    }
}
