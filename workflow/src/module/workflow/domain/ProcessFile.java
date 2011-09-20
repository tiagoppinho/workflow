package module.workflow.domain;

import module.workflow.util.WorkflowFileUploadBean;
import myorg.util.ClassNameBundle;

import org.apache.commons.lang.StringUtils;

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

    /**
     * Before validation and adding the file this method is called. The default
     * behavior is doing nothing.
     * 
     * @param bean
     *            the bean that contains all the data used to create this file.
     */
    public void preProcess(WorkflowFileUploadBean bean) {

    }

    /**
     * After validation and adding the file this method is called. The default
     * behavior is doing nothing.
     * 
     * @param bean
     *            the bean that contains all the data used to create this file.
     */
    public void postProcess(WorkflowFileUploadBean bean) {

    }

    public boolean isParsableType() {
	return getFilename().toLowerCase().endsWith(".pdf");
    }

    /**
     * Tells if the file is able to be archieved or not at the given time the
     * method is called. (This will renderer a remove link on the interface when
     * returns true).
     * 
     * By default it returns true
     */
    public boolean isPossibleToArchieve() {
	return true;
    }

    @Override
    public void delete() {
	removeProcess();
	removeProcessWithDeleteFile();
	super.delete();
    }

    /**
     * This method is called after a file is archived in order to allow the user
     * to remove possible relations.
     * 
     * By default does nothing
     */
    public void processRemoval() {

    }


    public boolean isArchieved() {
	return getProcess() == null && getProcessWithDeleteFile() != null;
    }

    public String getPresentationName() {
	return StringUtils.isEmpty(getDisplayName()) ? getFilename() : getDisplayName();
    }
}
