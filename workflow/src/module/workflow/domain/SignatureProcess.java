package module.workflow.domain;

import module.signature.domain.SignatureIntention;
import module.signature.metadata.SignatureMetaDataProcess;
import pt.ist.fenixWebFramework.services.Service;

public class SignatureProcess extends SignatureProcess_Base {

    @Service
    public static SignatureProcess factory(WorkflowProcess process) {
	return new SignatureProcess(process);
    }

    protected SignatureProcess(WorkflowProcess process) {
	super();

	init(process);
    }

    protected void init(WorkflowProcess process) {
	super.init(process);
    }

    public WorkflowProcess getProcess() {
	return fromExternalId(getSignObjectId());
    }

    @Override
    public WorkflowProcess getSignObject() {
	return getProcess();
    }

    @Override
    public SignatureMetaDataProcess getMetaData() {
	return new SignatureMetaDataProcess(getProcess());
    }

    @Override
    protected void setRelation(SignatureIntention signature) {
	getSignObject().setSignature(signature);
    }

    @Override
    protected void finalizeSignature() {
	// TODO getProcess().closeProcess()
    }
}
