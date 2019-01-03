package module.workflow.ui;

import org.fenixedu.employer.exception.JobFailedException;
import org.fenixedu.employer.job.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import module.workflow.domain.ProcessFile;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
public class CertifierJob extends Job {

    private static final Logger logger = LoggerFactory.getLogger(CertifierJob.class);
    
    private final ProcessFile processFile;
    private final CertifierService certifierService;

    public CertifierJob(ProcessFile processFile, CertifierService certifierService) {
        this.processFile = processFile;
        this.certifierService = certifierService;
    }
    
    @Override
    public void start() {

    }

    @Override
    @Atomic(mode = Atomic.TxMode.READ)
    public void execute() throws JobFailedException {
        try {
            if (processFile.isToBeCertified() && !processFile.isCertified()) {
                logger.info("Certifing file {} of process {}", processFile.getExternalId(),
                        processFile.getProcess().getDescription());
                certifierService.sendDocumentToBeCertified(processFile.getFilename(), processFile, processFile.getUuid().toString()
                        , processFile.isCertifiedOnCreation());
            } else {
                logger.info("file {} of process {} already certified", processFile.getExternalId(),
                        processFile.getProcess().getDescription());
            }
        }catch (Exception e) {
            logger.error("something wrong when certifing file {} of process {}", processFile.getExternalId(),
                    processFile.getProcess().getDescription(), e);
            throw new JobFailedException();
        }
    }

    @Override
    public void finish() {
        FenixFramework.atomic(() -> {
            if (!processFile.isCertified()) {
                logger.info("Mark file {} of process {} as certified", processFile.getExternalId(),
                        processFile.getProcess().getDescription());
                processFile.setCertified(true);
            }
        });
    }

    @Override
    @Atomic(mode = Atomic.TxMode.READ)
    public void fail() {
        logger.error("Failed to certify file {}", processFile.getExternalId());
    }


}
