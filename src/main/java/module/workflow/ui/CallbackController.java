package module.workflow.ui;

import java.util.UUID;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.fenixedu.bennu.SmartsiignerSdkConfiguration;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.security.SkipCSRF;
import org.fenixedu.employer.Employer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final Employer employer;
    private final CertifierService certifierService;

    @Autowired
    public CallbackController(Employer employer, final CertifierService certifierService) {
        this.employer = employer;
        this.certifierService = certifierService;
    }

    private static final Logger logger = LoggerFactory.getLogger(CallbackController.class);

    @SkipCSRF
    @RequestMapping(value = "/{processFile}/sign", method = RequestMethod.POST)
    public Response addSignedPurchaseOrderDocument(@PathVariable("processFile") ProcessFile processFile,
            @QueryParam("nounce") String nounce, @RequestParam(required = false) MultipartFile file,
            @RequestParam("username") String signerUsername, @RequestParam(value = "", required = false) String refuseReason) {
        if (logger.isDebugEnabled()) {
            logger.debug("/workflow/sign: " + processFile.getExternalId() + " user: " + signerUsername);
        }

        try {
            final String rawUuid =
                    Jwts.parser().setSigningKey(SmartsiignerSdkConfiguration.getConfiguration().jwtSecret().getBytes())
                    .parseClaimsJws(nounce).getBody().getSubject();
            final UUID uuid = UUID.fromString(rawUuid);
            if (logger.isDebugEnabled()) {
                logger.debug("   uuid: " + uuid);
            }

            if (processFile == null || processFile.getSigningState() != SigningState.PENDING || !processFile.getUuid().equals(uuid)) {
                if (logger.isDebugEnabled()) {
                    if (processFile == null) {
                        logger.debug("   no process file");
                    } else if (processFile.getSigningState() != SigningState.PENDING) {
                        logger.debug("   not in pending state");
                    } else if (!processFile.getUuid().equals(uuid)) {
                        logger.debug("   uuid does not match");
                    }
                }
                return Response.serverError().build();
            }

            final User user = User.findByUsername(signerUsername);
            if (logger.isDebugEnabled()) {
                if (user != null) {
                    logger.debug("   found user.");
                }
            }
            Authenticate.mock(user, "System Automation (SmartSigner)");

            final WorkflowProcess process = processFile.getProcess();
            if (logger.isDebugEnabled()) {
                if (process != null) {
                    logger.debug("   found process.");
                }
            }

            // If no file is received it means the signing request was refused
            if (file == null || file.isEmpty()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("   no content... removing file from process.");
                }
                process.removeFiles(processFile);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("   setting signed file.");
                }
                final ProcessFileSignatureHandler<ProcessFile> handler = ProcessFileSignatureHandler.handlerFor(processFile);
                if (logger.isDebugEnabled()) {
                    logger.debug("   using handler: " + handler.getClass().getName());
                }
                processFile.setSignedFile(file, handler.signedFileName());

                if (processFile.isToBeCertified() && !processFile.isCertified()) {
                    certify(processFile);
                }
            }

            if (logger.isDebugEnabled()) {
                logger.debug("   done, sending response.");
            }
            return Response.ok().build();
        } catch (final Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return Response.serverError().build();
        } finally {
            Authenticate.unmock();
        }
    }

    private void certify(final ProcessFile processFile) {
        employer.offer(new CertifierJob(processFile, certifierService));
    }

}