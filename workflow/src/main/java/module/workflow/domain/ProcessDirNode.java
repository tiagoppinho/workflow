package module.workflow.domain;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import module.fileManagement.domain.AbstractFileNode;
import module.fileManagement.domain.ContextPath;
import module.fileManagement.domain.DirNode;
import module.fileManagement.domain.FileNode;
import module.fileManagement.domain.log.AccessFileLog;
import module.fileManagement.domain.log.FileLog;
import module.workflow.domain.exceptions.WorkflowDomainException;
import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.fenixframework.pstm.IllegalWriteException;
import dml.runtime.Relation;
import dml.runtime.RelationListener;

public class ProcessDirNode extends ProcessDirNode_Base {

 /*   static {
	//relation listener for the access logs - TODO
	FileNode.FileLogFileNode.addListener(new RelationListener<FileNode, FileLog>() {

	    @Override
	    public void afterAdd(Relation<FileNode, FileLog> arg0, FileNode arg1, FileLog arg2) {
		if (!(arg2 instanceof AccessFileLog) || ((AccessFileLog) arg2).getFileNode() == null
			|| ((AccessFileLog) arg2).getFileNode().getDocument().getProcessDocument() == null)
		    return;
		AccessFileLog accessFileLog = (AccessFileLog) arg2;

		ProcessDocument processDocument = accessFileLog.getFileNode().getDocument().getProcessDocument();
		WorkflowProcess process = processDocument.getProcess();
		if (process == null) {
		    process = ((ProcessDirNode) arg1.getParent()).getWorkflowProcess();
		}
		process.preAccessFile(processDocument);
		process.postAccessFile(processDocument);

	    }

	    @Override
	    public void afterRemove(Relation<FileNode, FileLog> arg0, FileNode arg1, FileLog arg2) {

	    }

	    @Override
	    public void beforeAdd(Relation<FileNode, FileLog> arg0, FileNode arg1, FileLog arg2) {

	    }

	    @Override
	    public void beforeRemove(Relation<FileNode, FileLog> arg0, FileNode arg1, FileLog arg2) {

	    }
	});

	//relation listener for the 'deletion' of a file i.e. sending it to the trash - TODO - or not!!
	AbstractFileNode.DirNodeAbstractFileNode.addListener(new RelationListener<AbstractFileNode, DirNode>() {

	    @Override
	    public void afterAdd(Relation<AbstractFileNode, DirNode> arg0, AbstractFileNode arg1, DirNode arg2) {
		if (!arg1.isFile() || !(arg2.getRootDirNode() != null && arg2.getRootDirNode() instanceof ProcessDirNode))
		    return;
		//we are in the trash of a ProcessDirNode and we just 'deleted' a file
		FileNode deletedFileNode = (FileNode) arg1;
		DirNode trashOfProcessDirNode = arg2;
		ProcessDocument processDocument = deletedFileNode.getDocument().getProcessDocument();
		WorkflowProcess process = processDocument.getProcess();

		process.removeTiesWithFileDocument(processDocument);

	    }

	    @Override
	    public void afterRemove(Relation<AbstractFileNode, DirNode> arg0, AbstractFileNode arg1, DirNode arg2) {

	    }

	    @Override
	    public void beforeAdd(Relation<AbstractFileNode, DirNode> arg0, AbstractFileNode arg1, DirNode arg2) {

	    }

	    @Override
	    public void beforeRemove(Relation<AbstractFileNode, DirNode> arg0, AbstractFileNode arg1, DirNode arg2) {

	    }
	});

    } */

    public ProcessDirNode(WorkflowProcess process) {
	super();
	if (process.getDocumentsRepository() != null)
	    throw new DomainException("error.this.process.already.has.a.repository");
	setReadGroup(WFDocsDefaultReadGroup.getOrCreateInstance(process));
	setWriteGroup(WFDocsDefaultWriteGroup.getOrCreateInstance(process));
	setWorkflowProcess(process);
	setQuota(Long.MAX_VALUE); // no limit for the quota of the processes
	createTrashFolder();

    }

    @Override
    public boolean checkParent() {
	return hasParent() ? true : hasWorkflowProcess();
    }
    
    /**
     * 
     * @param content
     * @param fileDescription
     * @param fileName
     * @return
     */
    @SuppressWarnings("unchecked")
    <P extends ProcessDocument> P uploadDocument(byte[] content, final String displayName, final String fileName,
	    final Class<P> processDocClass)
    {
	//let's see if we already have any document with that name and type
	AbstractFileNode nodeFound = searchNode(displayName);

	//seen that the DocumentRepository offers a flat repository i.e. no dirs, let's throw an error
	//if we found something that is a dir or that cannot be converted to ProcessDocument
	if (nodeFound != null
		&& (!(nodeFound instanceof FileNode) || ((FileNode) nodeFound).getDocument().getProcessDocuments() == null))
	    throw new Error("no.dirs.or.other.files.allowed.here");
	ProcessDocument existingProcessDocument = null;
	//if the associated ProcessDocument found is not of the same type of the one that we were supposed to create,
	//let's throw a DomainException
	if (nodeFound != null)
	{
	    existingProcessDocument = getProcessDocument((FileNode) nodeFound);
	    if (!(existingProcessDocument.getClass().equals(processDocClass)))
		    throw new WorkflowDomainException("cant.create.document.other.type.same.name.exists", displayName);
	}
	
	FileNode fileNode = createFile(content, displayName, fileName, content.length, new ContextPath(this));

	if (existingProcessDocument == null) {
	    try {
	    Constructor<P> fileConstructor;
		fileConstructor = processDocClass.getConstructor(FileNode.class);
		existingProcessDocument = fileConstructor.newInstance(new Object[] { fileNode });
	    } catch (InvocationTargetException e) {
		if (e.getCause() instanceof IllegalWriteException) {
		    throw new IllegalWriteException();
		}
		throw new Error(e);
	    }
 catch (NoSuchMethodException | IllegalAccessException | InstantiationException | SecurityException e1) {
		throw new Error(e1);
	    }

	}


	return (P) existingProcessDocument;
	
    }
    
   
    /**
     * 
     * @param node the {@link FileNode} associated with the ProcessDocument
     * @return the {@link ProcessDocument} associated with the node, under this ProcessDirNode, or null if none is found
     */
    private ProcessDocument getProcessDocument(FileNode node)
    {
	if (!this.equals(node.getParent()))
	    return null;
	for (ProcessDocument processDocument : node.getDocument().getProcessDocuments()) {
	    if (processDocument.getProcess().equals(getWorkflowProcess()))
		return processDocument;
	}
	return null;
	
    }

}
