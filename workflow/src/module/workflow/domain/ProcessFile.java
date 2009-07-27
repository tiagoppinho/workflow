package module.workflow.domain;

import module.workflow.util.WorkflowFileUploadBean;
import myorg.util.ClassNameResolver;

public class ProcessFile extends ProcessFile_Base {

    static {
	ClassNameResolver.registerType(ProcessFile.class, "resources/WorkflowResources",
		"label.module.workflow.domain.ProcessFile");
    }

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

}
