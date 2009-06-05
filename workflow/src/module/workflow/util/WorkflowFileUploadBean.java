package module.workflow.util;

import module.workflow.domain.WorkflowProcess;
import myorg.util.FileUploadBean;
import pt.ist.fenixWebFramework.util.DomainReference;

public class WorkflowFileUploadBean extends FileUploadBean {

    private DomainReference<WorkflowProcess> process;
    private Class selectedInstance;
    private boolean instanceLock;

    public Class getSelectedInstance() {
	return selectedInstance;
    }

    public void setSelectedInstance(Class selectedInstance) {
	this.selectedInstance = selectedInstance;
    }

    public void setProcess(DomainReference<WorkflowProcess> process) {
	this.process = process;
    }

    public boolean isInstanceLock() {
	return instanceLock;
    }

    public void setInstanceLock(boolean instanceLock) {
	this.instanceLock = instanceLock;
    }

    public void setProcess(WorkflowProcess process, Class selectedInstance) {
	setProcess(process);
	setSelectedInstance(selectedInstance);
	this.instanceLock = true;
    }

    public WorkflowFileUploadBean(WorkflowProcess process) {
	setProcess(process);
	this.instanceLock = false;
    }

    public <T extends WorkflowProcess> T getProcess() {
	return (T) this.process.getObject();
    }

    public void setProcess(WorkflowProcess process) {
	this.process = new DomainReference<WorkflowProcess>(process);
    }

}
