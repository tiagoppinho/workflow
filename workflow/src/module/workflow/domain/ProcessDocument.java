package module.workflow.domain;

import java.io.InputStream;

import module.fileManagement.domain.Document;
import module.fileManagement.domain.FileNode;
import module.workflow.util.WorkflowDocumentUploadBean;

/**
 * 
 * @author João Antunes (joao.antunes@tagus.ist.utl.pt) - 7 de Mai de 2012
 * 
 *         Class that represents a {@link Document} (a file with associated
 *         metadata) on a workflow process
 */
public abstract class ProcessDocument extends ProcessDocument_Base {
    
    public  ProcessDocument() {
        super();
    }
    
    public ProcessDocument(String displayName, String filename, byte[] content, WorkflowProcess process) {
	super();
	init(displayName, filename, content, process);
    }

    public final void init(String displayName, String filename, byte[] content, WorkflowProcess process) {
	Document document = new Document(displayName, filename, content);
	//get the read and write groups per process, if they exist, use them, if not, it's time to create it
	document.setReadGroup(WFDocumentsReadPG.getOrCreateInstance(process));
	document.setWriteGroup(WFDocumentsWritePG.getOrCreateInstance(process));
	document.addVersion(displayName, filename, content);
	document.addFileNode(new FileNode(process.getDocumentsRepository(), document));
	setDocument(document);
	
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

    public abstract void validateUpload(WorkflowProcess process);

    /**
     * Method used to fill in the fields that might be declared on
     * specializations of this class
     * 
     * @param bean
     *            the {@link WorkflowDocumentUploadBean} that has the info to be
     *            filled on the slots of this domain item
     */
    public abstract void fillInNonDefaultFields(WorkflowDocumentUploadBean bean);

    public abstract ProcessDocumentMetaDataResolver<? extends ProcessDocument> getMetaDataResolver();

    /**
     * Tells if the file is able to be archieved or not at the given time the
     * method is called. (This will renderer a remove link on the interface when
     * returns true).
     * 
     * By default it returns true
     */
    public boolean isPossibleToArchieve() {
	return true;
    }

    /**
     * 
     * @param displayName
     * @param filename
     * @param process
     * @return An existing {@link ProcessDocument}, if one of the same type,
     *         same displayName and filename exists associated with this
     *         process, or null if it doesn't
     */
    public static ProcessDocument getExistingDocument(String displayName, String filename, WorkflowProcess process,
	    Class<? extends ProcessDocument> clazz) {
	for (ProcessDocument processDocument : process.getFileDocuments()) {
	    if (processDocument.getClass().equals(clazz) && processDocument.getDisplayName().equals(displayName)
		    && processDocument.getFilename().equals(filename)) {
		return processDocument;
	    }
	}
	return null;

    }

    /**
     * @return if the access to this instance is logged or not. To change this
     *         value, per instance, see and override
     *         {@link ProcessDocumentMetaDataResolver#shouldFileContentAccessBeLogged()}
     */
    public final boolean shouldFileContentAccessBeLogged() {
	return getDocument().mustSaveAccessLog();
    }

    public String getFilename() {
	return getDocument().getFileName();
    }

    public String getDisplayName() {
	return getDocument().getDisplayName();
    }

    public InputStream getStream() {
	return getDocument().getLastVersionedFile().getStream();
    }

    /**
     * This method is called after a file is archived in order to allow the user
     * to remove possible relations.
     * 
     * By default does nothing
     */
    public void processRemoval() {

    }

    public boolean isInTrash() {
	return !hasProcess();
    }

    public String getContentType() {
	return getDocument().getLastVersionedFile().getContentType();
    }


}
