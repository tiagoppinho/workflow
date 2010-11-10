package module.workflow.util;

import javax.servlet.http.HttpServletRequest;

import module.signature.domain.SignatureIntention;
import module.signature.domain.SignatureSystem;
import module.workflow.domain.WorkflowLog;
import module.workflow.domain.WorkflowProcess;
import module.workflow.presentationTier.actions.ProcessManagement.ProcessRequestHandler;

public class SignatureProcessRequestHandler<T extends WorkflowProcess> implements ProcessRequestHandler<T> {

    @Override
    public void handleRequest(T process, HttpServletRequest request) {

	// check pending signatures of the process
	for (SignatureIntention signature : SignatureSystem.getInstance().getSignaturesByIdentification(
		process.getIdentification())) {
	    if (signature.isPending()) {
		request.setAttribute("signatureNotification", "<b>WOW</b> Process Signature Pending...");
		return;
	    }
	}

	for (WorkflowLog log : process.getPendingLogs()) {
	    for (SignatureIntention signature : SignatureSystem.getInstance().getSignaturesByIdentification(
		    log.getIdentification())) {
		if (signature.isPending()) {
		    request.setAttribute("signatureNotification", "<b>WOW</b> ACTIVITY Signature Pending...");
		    return;
		}
	    }
	}
    }
}
