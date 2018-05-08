package module.workflow.ui;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.fenixedu.bennu.SmartsiignerSdkConfiguration;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.security.SkipCSRF;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.jsonwebtoken.Jwts;
import module.workflow.domain.ProcessFile;
import module.workflow.domain.ProcessFileSignatureHandler;
import module.workflow.domain.SigningState;
import module.workflow.domain.WorkflowProcess;

@RestController
@RequestMapping("/workflow")
public class CallbackController {

    @SkipCSRF
    @RequestMapping(value = "/{processFile}/sign", method = RequestMethod.POST)
    public Response addSignedPurchaseOrderDocument(@PathVariable("processFile") ProcessFile processFile,
            @QueryParam("nounce") String nounce, @RequestParam(required = false) MultipartFile file,
            @RequestParam("username") String signerUsername, @RequestParam(value = "", required = false) String refuseReason) {
        try {
            final String uuid = Jwts.parser().setSigningKey(SmartsiignerSdkConfiguration.getConfiguration().jwtSecret().getBytes())
                    .parseClaimsJws(nounce).getBody().getSubject();

            if (processFile == null || processFile.getSigningState() != SigningState.PENDING || !processFile.getUuid().equals(uuid)) {
                return Response.serverError().build();
            }

            Authenticate.mock(User.findByUsername(signerUsername), "System Automation (SmartSigner)");

            final WorkflowProcess process = processFile.getProcess();

            // If no file is received it means the signing request was refused
            if (file == null || file.isEmpty()) {
                process.removeFiles(processFile);
            } else {
                final ProcessFileSignatureHandler<ProcessFile> handler = ProcessFileSignatureHandler.handlerFor(processFile);
                processFile.setSignedFile(file, handler.signedFileName());
            }

            return Response.ok().build();
        } catch (final Exception e) {
            return Response.serverError().build();
        } finally {
            Authenticate.unmock();
        }
    }

}