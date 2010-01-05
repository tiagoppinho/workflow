package module.workflow.domain;

import module.workflow.util.WorkflowFileUploadBean;
import myorg.util.ClassNameBundle;

@ClassNameBundle(bundle = "resources/WorkflowResources")
public class ProcessFile extends ProcessFile_Base {

    public ProcessFile() {
	super();
    }

    public ProcessFile(String displayName, String filename, byte[] content) {
	super();
	init(displayName, filename, content);
    }

    public void fillInNonDefaultFields(WorkflowFileUploadBean bean) {

    }

    /**
     * Validates if this file is valid to be associated with the workflowProcess
     * 
     * @param workflowProcess
     *            the process to which this file is being associated
     * 
     * @throws module.workflow.domain.ProcessFileValidationException
     *             if does not validate
     */
    public void validateUpload(WorkflowProcess workflowProcess) {

    }

    public boolean isParsableType() {
	return getFilename().toLowerCase().endsWith(".pdf");
    }

    public boolean isPossibleToArchieve() {
	return true;
    }

    @Override
    public void delete() {
	removeProcess();
	removeProcessWithDeleteFile();
	super.delete();
    }

}
