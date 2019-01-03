/*
 * @(#)ProcessFile.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Case Handleing Based Workflow Module.
 *
 *   The Case Handleing Based Workflow Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Workflow Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Workflow Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.workflow.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.ws.rs.ProcessingException;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.SmartsiignerSdkConfiguration;
import org.fenixedu.bennu.WorkflowConfiguration;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.domain.PortalConfiguration;
import org.springframework.web.multipart.MultipartFile;

import jvstm.cps.ConsistencyPredicate;
import module.workflow.domain.exceptions.WorkflowDomainException;
import module.workflow.util.ClassNameBundle;
import module.workflow.util.WorkflowClassUtil;
import module.workflow.util.WorkflowFileUploadBean;
import pt.ist.fenixframework.Atomic;
import pt.ist.smartsigner.sdk.SmartSignerSdk;

/**
 * 
 * @author João Antunes
 * @author Shezad Anavarali
 * @author Luis Cruz
 * @author Paulo Abrantes
 * @author Susana Fernandes
 * 
 *         TODO: make abstract
 * 
 */
@ClassNameBundle(bundle = "WorkflowResources")
public class ProcessFile extends ProcessFile_Base {

    public ProcessFile() {
        super();
        setShouldBeSigned(Boolean.FALSE);
        setUuid(UUID.randomUUID());
        setSigningState(SigningState.CREATED);

        // initial certified state (false)
        setCertified(Boolean.FALSE);
        // true if it is to be certified
        setToBeCertified(Boolean.FALSE);
        // true if the template contains certification information
        setCertifiedOnCreation(Boolean.FALSE);
    }

    @Override
    public void setCertified(final boolean certified) {
        super.setCertified(certified);
    }

    @Override
    public void setToBeCertified(final boolean toBeCertified) {
        super.setToBeCertified(toBeCertified);
    }

    @Override
    public void setCertifiedOnCreation(final boolean certifiedOnCreation) {
        super.setCertifiedOnCreation(certifiedOnCreation);
    }

    public boolean isCertified() {
        return super.getCertified();
    }

    public boolean isToBeCertified() {
        return super.getToBeCertified();
    }

    public boolean isCertifiedOnCreation() {
        return super.getCertifiedOnCreation();
    }

    public ProcessFile(String displayName, String filename, byte[] content) {
        this();
        init(displayName, filename, content);
    }

    @ConsistencyPredicate
    public boolean checkConnectedWithProcess() {
        return getProcess() != null || getProcessWithDeleteFile() != null;

    }

    public void fillInNonDefaultFields(WorkflowFileUploadBean bean) {

    }

    /**
     * Hook that is called each time someone tries to access the file (for now
     * this translates to Download, but a preview, which is not implemented yet,
     * should also invoke this method)
     * 
     * @author João Antunes
     */
    public void preFileContentAccess() {

    }

    /**
     * Hook that is called after someone accesses a file (for now this
     * translates to Download, but a preview, which is not implemented yet,
     * should also invoke this method)
     * 
     * @author João Antunes
     */
    public void postFileContentAccess() {

    }

    /**
     * 
     * @return true if one should log whenever someone tries to access the
     *         content of the file, false otherwise
     * @author João Antunes
     */
    public boolean shouldFileContentAccessBeLogged() {
        return false;
    }

    /**
     * Validates if this file is valid to be associated with the workflowProcess
     * 
     * @param workflowProcess
     *            the process to which this file is being associated
     * 
     * @throws module.workflow.domain.ProcessFileValidationException
     *             if does not validate
     */
    public void validateUpload(WorkflowProcess workflowProcess) throws ProcessFileValidationException {

    }

    /**
     * Before validation and adding the file this method is called. The default
     * behavior is doing nothing.
     * 
     * @param bean
     *            the bean that contains all the data used to create this file.
     */
    public void preProcess(WorkflowFileUploadBean bean) {

    }

    /**
     * After validation and adding the file this method is called. The default
     * behavior is doing nothing.
     * 
     * @param bean
     *            the bean that contains all the data used to create this file.
     */
    public void postProcess(WorkflowFileUploadBean bean) {

    }

    public boolean isParsableType() {
        return getFilename().toLowerCase().endsWith(".pdf");
    }

    /**
     * Tells if the file is able to be archieved or not at the given time the
     * method is called. (This will renderer a remove link on the interface when
     * returns true).
     * 
     * By default it returns true
     *
     * @return true
     */
    public boolean isPossibleToArchieve() {
        return true;
    }

    @Override
    public void delete() {
        setProcess(null);
        setProcessWithDeleteFile(null);
        super.delete();
    }

    /**
     * This method is called after a file is archived in order to allow the user
     * to remove possible relations.
     * 
     * By default does nothing
     */
    public void processRemoval() {

    }

    public boolean isArchieved() {
        return getProcess() == null && getProcessWithDeleteFile() != null;
    }

    public String getPresentationName() {
        return StringUtils.isEmpty(getDisplayName()) ? getFilename() : getDisplayName();
    }

    @Override
    public boolean isAccessible(User user) {
        return getProcess().isAccessible(user);
    }

    @Override
    public WorkflowProcess getProcess() {
        return super.getProcess();
    }

    private static final Pattern REMOVE_PATTERN = Pattern.compile("[-_]");

    private static String generateSecureId(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Invalid length: " + length);
        }
        final SecureRandom random = new SecureRandom();
        // Request some extra bytes to account for '-' and '_' characters
        final byte[] bytes = new byte[length + 5];
        while (true) {
            random.nextBytes(bytes);

            // Remove all invalid characters
            final String str = REMOVE_PATTERN.matcher(Base64.getUrlEncoder().encodeToString(bytes)).replaceAll("");

            // If we have the necessary length, trim and return it
            if (str.length() >= length) {
                return str.substring(0, length);
            }
        }
    }

    public void sendFileForSigning() {
        sendFileForSigning(ProcessFileSignatureHandler.handlerFor(this).queue());
    }

    @Atomic
    public void sendFileForSigning(String queue) {
        if (isArchieved()) {
            throw new WorkflowDomainException("error.cannot.sign.archieved.files");
        }
        if (!WorkflowConfiguration.getConfiguration().smartsignerIntegration()) {
            throw new WorkflowDomainException("error.smart.signer.not.configured");
        }

        final ProcessFileSignatureHandler<ProcessFile> handler = ProcessFileSignatureHandler.handlerFor(this);

        final InputStream content = new ByteArrayInputStream(getContent());
        final String filename = handler.filename();
        final String creator = PortalConfiguration.getInstance().getApplicationSubTitle().getContent();

        final byte[] jwtSecret = SmartsiignerSdkConfiguration.getConfiguration().jwtSecret().getBytes();

        try {
            SmartSignerSdk.sendForSigning(filename, content, queue, creator, handler.title(), handler.allowMultipleSignatures(),
                    handler.description(), handler.externalIdentifier(), handler.signatureField(), handler.callbackUrl(jwtSecret),
                    Authenticate.getUser().getUsername());
        } catch (final ProcessingException ex) {
            throw new WorkflowDomainException("error.smart.signer.communication.problem", ex);
        }

        setSigningState(SigningState.PENDING);
    }

    @Atomic
    public void setSignedFile(final MultipartFile file, final String filename) {
        try {
            init(filename, filename, file.getBytes());
        } catch (final IOException e) {
            throw new Error(e);
        }
        setSigningState(SigningState.SIGNED);
        new FileUploadLog(getProcess(), filename, filename, WorkflowClassUtil.getNameForType(file.getClass()), "");
    }

}
