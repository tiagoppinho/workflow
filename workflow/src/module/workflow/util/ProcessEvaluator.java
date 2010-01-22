package module.workflow.util;

import module.workflow.domain.WorkflowProcess;

public abstract class ProcessEvaluator<W extends WorkflowProcess> {

    protected ProcessEvaluator<W> next;

    public void evaluate(final W w) {
	if (next != null) {
	    next.evaluate(w);
	}
    }

}
