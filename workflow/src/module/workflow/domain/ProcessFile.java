package module.workflow.domain;

import module.workflow.util.WorkflowFileUploadBean;
import myorg.util.ClassNameBundle;

@ClassNameBundle(bundle="resources/WorkflowResources")
public class ProcessFile extends ProcessFile_Base {

    public ProcessFile() {
	super();
    }

    public ProcessFile(String displayName, String filename, byte[] content) {
	super();
	init(displayName, filename, content);
    }

    public <T extends WorkflowFileUploadBean> void fillInNonDefaultFields(T bean) {

    }

    public <T extends WorkflowProcess> boolean validUpload(T workflowProcess) {
	return true;
    }

    public boolean isParsableType() {
	return getFilename().toLowerCase().endsWith(".pdf");
    }
}
