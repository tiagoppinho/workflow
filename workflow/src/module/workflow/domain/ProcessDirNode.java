package module.workflow.domain;

import module.fileManagement.domain.AbstractFileNode;
import module.fileManagement.domain.DirNode;
import module.fileManagement.domain.FileNode;
import module.fileManagement.domain.log.AccessFileLog;
import module.fileManagement.domain.log.FileLog;
import myorg.domain.exceptions.DomainException;
import dml.runtime.Relation;
import dml.runtime.RelationListener;

public class ProcessDirNode extends ProcessDirNode_Base {

    static {
	//relation listener for the access logs
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

	//relation listener for the 'deletion' of a file i.e. sending it to the trash
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

    }

    public ProcessDirNode(WorkflowProcess process) {
	super();
	if (process.getDocumentsRepository() != null)
	    throw new DomainException("error.this.process.already.has.a.repository");
	setReadGroup(WFDocumentsReadPG.getOrCreateInstance(process));
	setWriteGroup(WFDocumentsWritePG.getOrCreateInstance(process));
	setWorkflowProcess(process);
	setQuota(Long.MAX_VALUE); // no limit for the quota of the processes
	createTrashFolder();

    }

    @Override
    public boolean checkParent() {
	return hasParent() ? true : hasWorkflowProcess();
    }

}
