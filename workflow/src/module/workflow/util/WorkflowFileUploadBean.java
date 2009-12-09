package module.workflow.util;

import module.workflow.domain.ProcessFile;
import module.workflow.domain.WorkflowProcess;
import myorg.util.FileUploadBean;

public class WorkflowFileUploadBean extends FileUploadBean {

    private WorkflowProcess process;
    private Class<? extends ProcessFile> selectedInstance;
    private boolean instanceLock;

    public Class<? extends ProcessFile> getSelectedInstance() {
	return selectedInstance;
    }

    public void setSelectedInstance(Class<? extends ProcessFile> selectedInstance) {
	this.selectedInstance = selectedInstance;
    }

    public boolean isInstanceLock() {
	return instanceLock;
    }

    public void setInstanceLock(boolean instanceLock) {
	this.instanceLock = instanceLock;
    }

    public void setProcess(WorkflowProcess process, Class<? extends ProcessFile> selectedInstance) {
	setProcess(process);
	setSelectedInstance(selectedInstance);
	this.instanceLock = true;
    }

    public WorkflowFileUploadBean(WorkflowProcess process) {
	setProcess(process);
	this.instanceLock = false;
    }

    public <T extends WorkflowProcess> T getProcess() {
	return (T) this.process;
    }

    public void setProcess(WorkflowProcess process) {
	this.process = process;
    }

}
