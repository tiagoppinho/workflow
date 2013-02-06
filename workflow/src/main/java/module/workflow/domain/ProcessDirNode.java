package module.workflow.domain;

import module.fileManagement.domain.AbstractFileNode;
import module.fileManagement.domain.ContextPath;
import module.fileManagement.domain.DirNode;
import module.fileManagement.domain.FileNode;
import module.workflow.domain.exceptions.DuplicateProcessFileNameException;
import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.bennu.core.domain.groups.PersistentGroup;

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
        setDirNode(new DirNode());
        getDirNode().setReadGroup(WFDocsDefaultReadGroup.getOrCreateInstance(process));
        getDirNode().setWriteGroup(WFDocsDefaultWriteGroup.getOrCreateInstance(process));
        setWorkflowProcess(process);
        getDirNode().setQuota(Long.MAX_VALUE); // no limit for the quota of the processes
        getDirNode().createTrashFolder();
        getDirNode().setRoot(Boolean.TRUE);

    }

    public boolean checkParent() {
        return getDirNode().hasParent() ? true : hasWorkflowProcess();
    }

    /**
     * 
     * @param content
     * @param fileDescription
     * @param fileName
     * @return
     */
    @SuppressWarnings("unchecked")
    <P extends ProcessFile> P uploadDocument( //byte[] content, final String displayName, final String fileName,
            //final Class<P> processDocClass, 
            P processFile) {
        //temporary assignments from the processFile TODO remove them once the migration is done.
        final String displayName = processFile.getDisplayName();
        final String fileName = processFile.getFilename();
        final Class<? extends ProcessFile> processDocClass = processFile.getClass();
        final byte[] content = processFile.getContent();

        //let's see if we already have any document with that name and type
        AbstractFileNode nodeFound = getDirNode().searchNode(displayName);

        //seen that the DocumentRepository offers a flat repository i.e. no dirs, let's throw an error
        //if we found something that is a dir or that cannot be converted to ProcessDocument
        if (nodeFound != null)
            throw new DuplicateProcessFileNameException("no.files.same.name.allowed.rename.pls", displayName);
        ProcessFile existingProcessDocument = null;

        FileNode fileNode =
                getDirNode().createFile(content, displayName, fileName, content.length, new ContextPath(getDirNode()));

        //for now, we will not create the processFile, as it already exists TODO when the migration is done, this is the method that should create the ProcessFile
        if (existingProcessDocument == null) {
            //	    
            //	    try {
            //	    Constructor<P> fileConstructor;
            //		fileConstructor = processDocClass.getConstructor(FileNode.class);
            //		existingProcessDocument = fileConstructor.newInstance(new Object[] { fileNode });
            //	    } catch (InvocationTargetException e) {
            //		if (e.getCause() instanceof IllegalWriteException) {
            //		    throw new IllegalWriteException();
            //		}
            //		throw new Error(e);
            //	    }
            // catch (NoSuchMethodException | IllegalAccessException | InstantiationException | SecurityException e1) {
            //		throw new Error(e1);
            //	    }
            //
            //existingProcessDocument.validateUpload(getWorkflowProcess());

        }

        processFile.init(fileNode);
        existingProcessDocument = processFile;

        return (P) existingProcessDocument;

    }

    /**
     * 
     * @param node
     *            the {@link FileNode} associated with the ProcessDocument
     * @return the {@link ProcessFile} associated with the node, under this
     *         ProcessDirNode, or null if none is found
     */
    private ProcessFile getProcessFile(FileNode node) {
        if (!this.equals(node.getParent()))
            return null;
        for (ProcessFile processDocument : node.getDocument().getProcessDocuments()) {
            if (processDocument.getProcess().equals(getWorkflowProcess()))
                return processDocument;
        }
        return null;

    }

    public String getDisplayName() {
        return getWorkflowProcess().getDescription();
    }

    public PersistentGroup getReadGroup() {
        return getDirNode().getReadGroup();
    }

    public void setReadGroup(AbstractWFDocsGroup readGroup) {
        //if (readGroup == null)
        //   throw new WorkflowDomainException("cannot.set.a.null.group, args)
        getDirNode().setReadGroup(readGroup);

    }

    public PersistentGroup getWriteGroup() {
        return getDirNode().getWriteGroup();
    }

    public void setWriteGroup(AbstractWFDocsGroup instance) {
        getDirNode().setWriteGroup(instance);
    }

    public DirNode getTrash() {
        return getDirNode().getTrash();
    }


    /**
     * 
     * @param fileNode {@link FileNode} to search on
     * @return the {@link WorkflowProcess} associated with that {@link FileNode} or null if it doesn't exist
     */
    public static WorkflowProcess getProcess(AbstractFileNode node) {
        if (node == null)
            return null;
        if (node instanceof DirNode && ((DirNode) node).getProcessDirNode() != null) {
            ProcessDirNode processDirNode = ((DirNode) node).getProcessDirNode();
            if (processDirNode != null)
                return processDirNode.getWorkflowProcess();
            else
                return null;
        } else
            return getProcess(node.getParent());
    }

    public void delete() {
        removeDirNode();
        removeWorkflowProcess();
        deleteDomainObject();
        
    }

}
