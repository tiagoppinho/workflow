package module.workflow.domain;

import java.io.InputStream;

import pt.ist.bennu.core.domain.groups.UnionGroup;

import module.fileManagement.domain.AbstractFileNode;
import module.fileManagement.domain.DirNode;
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

    public ProcessDocument() {
	super();
    }

    public ProcessDocument(final FileNode associatedFileNode) {
	super();
	init(associatedFileNode);
	
	
    }
    
    @Deprecated
    public void delete() {
	getDocument().delete();
	setProcess(null);
	deleteDomainObject();
    }

    public final void init(final FileNode associatedFileNode) {
	this.setProcess(((ProcessDirNode) associatedFileNode.getParent()).getWorkflowProcess());
	this.setDocument(associatedFileNode.getDocument());
	AbstractWFDocsGroup readGroup = AbstractWFDocsGroup.getOrCreateInstance(getProcess(), this.getMetaDataResolver().getReadGroupClass());
	AbstractWFDocsGroup writeGroup = AbstractWFDocsGroup.getOrCreateInstance(getProcess(), this.getMetaDataResolver().getWriteGroupClass());
	if (this.getDocument().hasReadGroup())
	{
	    this.getDocument().setReadGroup(UnionGroup.getOrCreateUnionGroup(getDocument().getReadGroup(), readGroup));
	} else {
	    this.getDocument().setReadGroup(readGroup);
	}
	
	if (this.getDocument().hasWriteGroup())
	{
	    this.getDocument().setWriteGroup(UnionGroup.getOrCreateUnionGroup(getDocument().getWriteGroup(), writeGroup));
	} else {
	    this.getDocument().setWriteGroup(writeGroup);
	}
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
     * Validates if this file is valid to be associated with the workflowProcess
     * 
     * @param workflowProcess
     *            the process to which this file is being associated
     * 
     * @throws module.workflow.domain.ProcessFileValidationException
     *             if does not validate
     * 
     * 
     */
    public abstract void validateUpload(WorkflowProcess process) throws ProcessFileValidationException;

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
     * TODO: remove
     * 
     * @param displayName
     * @param filename
     * @param process
     * @return An existing {@link ProcessDocument}, if one of the same
     *         displayName and filename exists associated with this process, or
     *         null if it doesn't public static ProcessDocument
     *         getExistingDocument(String displayName, String filename,
     *         WorkflowProcess process, Class<? extends ProcessDocument> clazz)
     *         { ProcessDirNode documentsRepository =
     *         process.getDocumentsRepository(); AbstractFileNode nodeFound =
     *         documentsRepository.searchNode(displayName); if (nodeFound ==
     *         null) { return null; } //seen that the DocumentRepository offers
     *         a flat repository i.e. no dirs, let's throw an error //if we
     *         found something that is a dir or that cannot be converted to
     *         ProcessDocument if (!(nodeFound instanceof FileNode) ||
     *         ((FileNode) nodeFound).getDocument().getProcessDocument() ==
     *         null) throw new Error("no.dirs.or.other.files.allowed.here");
     *         return (ProcessDocument) (((FileNode)
     *         nodeFound).getDocument().getProcessDocument());
     * 
     *         }
     */

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

    //    public boolean isInTrash() {
    //	getDocument().
    //    }

    public String getContentType() {
	return getDocument().getLastVersionedFile().getContentType();
    }

    /**
     * 
     * @return a fileNode associated with this {@link ProcessDocument}. if none
     *         is found on the {@link WorkflowProcess#getDocumentsRepository()},
     *         it will search on the deleted items i.e. trash
     */
    public final FileNode getFileNode() {
	FileNode fileNodeToReturn = null;
	ProcessDirNode documentsRepository = getProcess().getDocumentsRepository();
	fileNodeToReturn = getDocument().getFileNode(documentsRepository);
	if (fileNodeToReturn == null) {
	    DirNode trash = documentsRepository.getTrash();
	    fileNodeToReturn = getDocument().getFileNode(trash);
	}
	return fileNodeToReturn;

    }

}
