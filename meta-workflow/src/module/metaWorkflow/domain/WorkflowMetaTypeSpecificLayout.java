package module.metaWorkflow.domain;

public class WorkflowMetaTypeSpecificLayout extends WorkflowMetaTypeSpecificLayout_Base {

    public WorkflowMetaTypeSpecificLayout(WorkflowMetaType metaType, String body, String header, String shortBody) {
	super();
	setMetaType(metaType);
	setBody(body);
	setHeader(header);
	setShortBody(shortBody);
    }

}
