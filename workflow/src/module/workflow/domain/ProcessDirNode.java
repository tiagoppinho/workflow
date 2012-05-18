package module.workflow.domain;

import myorg.domain.exceptions.DomainException;

public class ProcessDirNode extends ProcessDirNode_Base {
    
    public ProcessDirNode(WorkflowProcess process) {
        super();
	if (process.getDocumentsRepository() != null)
	    throw new DomainException("error.this.process.already.has.a.repository");

    }
    
}
