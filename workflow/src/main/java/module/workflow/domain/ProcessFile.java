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

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import module.fileManagement.domain.DirNode;
import module.fileManagement.domain.FileNode;
import module.workflow.util.WorkflowFileUploadBean;

import org.apache.commons.lang.StringUtils;

import pt.ist.bennu.core.domain.groups.UnionGroup;
import pt.ist.bennu.core.util.ClassNameBundle;

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
@ClassNameBundle(bundle = "resources/WorkflowResources")
public class ProcessFile extends ProcessFile_Base {

    public ProcessFile() {
	super();
    }

    public ProcessFile(String displayName, String filename, byte[] content) {
	super();
	init(displayName, filename, content);
    }

    public ProcessFile(final FileNode associatedFileNode) {
	super();
	init(associatedFileNode);
    }

    public final void init(final FileNode associatedFileNode) {
	this.setProcess(((ProcessDirNode) associatedFileNode.getParent()).getWorkflowProcess());
	this.setDocument(associatedFileNode.getDocument());
	AbstractWFDocsGroup readGroup = AbstractWFDocsGroup.getOrCreateInstance(getProcess(), this.getMetaDataResolver()
		.getReadGroupClass());
	AbstractWFDocsGroup writeGroup = AbstractWFDocsGroup.getOrCreateInstance(getProcess(), this.getMetaDataResolver()
		.getWriteGroupClass());
	if (this.getDocument().hasReadGroup()) {
	    this.getDocument().setReadGroup(UnionGroup.getOrCreateUnionGroup(getDocument().getReadGroup(), readGroup));
	} else {
	    this.getDocument().setReadGroup(readGroup);
	}

	if (this.getDocument().hasWriteGroup()) {
	    this.getDocument().setWriteGroup(UnionGroup.getOrCreateUnionGroup(getDocument().getWriteGroup(), writeGroup));
	} else {
	    this.getDocument().setWriteGroup(writeGroup);
	}
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

    @Override
    public String getFilename() {
	if (getDocument() == null) {
	    return super.getFilename();
	} else
	    return getDocument().getFileName();
    }

    @Override
    public String getDisplayName() {
	if (getDocument() == null)
	    return super.getDisplayName();
	else {
	    return getDocument().getDisplayName();
	}
    }

    @Override
    public InputStream getStream() {
	if (getDocument() == null) {
	    return super.getStream();
	} else
	return getDocument().getLastVersionedFile().getStream();
    }

    //    public boolean isInTrash() {
    //	getDocument().
    //    }

    public class GenericPDMetaDataResolver extends ProcessDocumentMetaDataResolver<ProcessFile> {

	private static final String TEMPLATE = "Outros";

	@Override
	public String getMetadataTemplateNameToUseOrCreate() {
	    return TEMPLATE;
	}

	@Override
	public boolean shouldFileContentAccessBeLogged() {
	    return true;
	}

	@Override
	public Map<String, String> getMetadataKeysAndValuesMap(ProcessFile processDocument) {
	    //	    hashMap.put("Nome atríbuido ao ficheiro", processDocument.getGenericDescription());
	    return Collections.emptyMap();
	}

	@Override
	public Class<? extends AbstractWFDocsGroup> getReadGroupClass() {
	    return WFDocsDefaultReadGroup.class;
	}

	@Override
	public Class<? extends AbstractWFDocsGroup> getWriteGroupClass() {
	    return WFDocsDefaultWriteGroup.class;
	}

    }

    public ProcessDocumentMetaDataResolver<ProcessFile> getMetaDataResolver() {
	return new GenericPDMetaDataResolver();
    }

    @Override
    public String getContentType() {
	if (getDocument() == null) {
	    return super.getContentType();
	} else
	return getDocument().getLastVersionedFile().getContentType();
    }

    /**
     * 
     * @return a fileNode associated with this {@link ProcessDocument}. if none
     *         is found on the {@link WorkflowProcess#getDocumentsRepository()},
     *         it will search on the deleted items i.e. trash
     */
    public final FileNode getFileNode() {
	if (getDocument() == null)
	    return null;
	FileNode fileNodeToReturn = null;
	ProcessDirNode documentsRepository = getProcess().getDocumentsRepository();
	fileNodeToReturn = getDocument().getFileNode(documentsRepository);
	if (fileNodeToReturn == null) {
	    DirNode trash = documentsRepository.getTrash();
	    fileNodeToReturn = getDocument().getFileNode(trash);
	}
	return fileNodeToReturn;

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

    // TODO: use this format
    //    /**
    //     * @return if the access to this instance is logged or not. To change this
    //     *         value, per instance, see and override
    //     *         {@link ProcessDocumentMetaDataResolver#shouldFileContentAccessBeLogged()}
    //     */
    //    public final boolean shouldFileContentAccessBeLogged() {
    //	return getDocument().mustSaveAccessLog();
    //    }

    /**
     * Validates if this file is valid to be associated with the workflowProcess
     * 
     * @param workflowProcess
     *            the process to which this file is being associated
     * 
     * @throws module.workflow.domain.ProcessFileValidationException
     *             if does not validate
     * 
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
     */
    public boolean isPossibleToArchieve() {
	return true;
    }

    @Override
    public void delete() {
	removeProcess();
	removeProcessWithDeleteFile();
	getDocument().delete();
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

    /**
     * Temporary method TODO remove it when migration is done
     * 
     * @return true if this ProcessFile is supported on the new structure
     */
    public boolean isInNewStructure() {
	return getDocument() != null;
    }

    public boolean isArchieved() {
	return getProcess() == null && getProcessWithDeleteFile() != null;
    }

    public String getPresentationName() {
	return StringUtils.isEmpty(getDisplayName()) ? getFilename() : getDisplayName();
    }
}
