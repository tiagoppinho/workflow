package module.workflow.domain;

import module.signature.domain.SignatureIntention;
import module.signature.metadata.SignatureProcessMetaData;
import pt.ist.fenixWebFramework.services.Service;

public class SignatureProcess extends SignatureProcess_Base {

    @Service
    public static SignatureProcess factory(WorkflowProcess process) {
	SignatureProcess signatureProcess = new SignatureProcess();
	signatureProcess.init(process);

	return signatureProcess;
    }

    protected SignatureProcess() {
	super();
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
    public SignatureProcessMetaData getMetaData() {
	return new SignatureProcessMetaData(getProcess());
    }

    @Override
    protected void setRelation(SignatureIntention signature) {
	getSignObject().setSignature(signature);
    }

    @Override
    protected void finalizeSignature() {
	// TODO getProcess().closeProcess()
    }

    @Override
    public String getSignatureDescription() {
	return "Processo " + getProcess().getProcessNumber();
    }

    @Override
    public String getType() {
	return "Processos";
    }
}
