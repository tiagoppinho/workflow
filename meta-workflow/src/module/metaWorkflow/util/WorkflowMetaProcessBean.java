package module.metaWorkflow.util;

import java.io.Serializable;

import module.metaWorkflow.domain.WorkflowMetaType;
import pt.ist.fenixWebFramework.util.DomainReference;

public class WorkflowMetaProcessBean implements Serializable {

    String instanceDescription;
    DomainReference<WorkflowMetaType> metaType;

    
    public WorkflowMetaProcessBean() {
	setMetaType(null);
    }
    
    public String getInstanceDescription() {
	return instanceDescription;
    }

    public void setInstanceDescription(String instanceDescription) {
	this.instanceDescription = instanceDescription;
    }

    public WorkflowMetaType getMetaType() {
	return metaType.getObject();
    }

    public void setMetaType(WorkflowMetaType metaType) {
	this.metaType = new DomainReference<WorkflowMetaType>(metaType);
    }

}
