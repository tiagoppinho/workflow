package module.workflow.presentationTier.actions;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

import module.workflow.domain.WorkflowProcess;

import org.apache.commons.collections.Predicate;

public class BasicSearchProcessBean implements Serializable {

    String processId;

    public String getProcessId() {
	return processId;
    }

    public void setProcessId(String processId) {
	this.processId = processId;
    }

    @SuppressWarnings("unchecked")
    public Set<WorkflowProcess> search() {
	return (Set<WorkflowProcess>) (processId == null ? Collections.emptySet() : WorkflowProcess.getAllProcesses(
		WorkflowProcess.class, new Predicate() {

		    @Override
		    public boolean evaluate(Object arg0) {
			return processId.trim().equals(((WorkflowProcess) arg0).getProcessNumber());
		    }

		}));
    }

}
