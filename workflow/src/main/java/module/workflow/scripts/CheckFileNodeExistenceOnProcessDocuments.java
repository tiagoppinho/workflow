///**
// * 
// */
//package module.workflow.scripts;
//
//import java.io.PrintStream;
//import java.io.PrintWriter;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import org.apache.commons.collections.map.HashedMap;
//
//import module.fileManagement.domain.DirNode;
//import module.fileManagement.domain.FileNode;
//import module.workflow.domain.AbstractWFDocsGroup;
//import module.workflow.domain.ProcessDirNode;
//import module.workflow.domain.ProcessDocument;
//import module.workflow.domain.WFDocsDefaultReadGroup;
//import module.workflow.domain.WFDocsDefaultWriteGroup;
//import module.workflow.domain.WFDocumentsReadPG;
//import module.workflow.domain.WFDocumentsWritePG;
//import module.workflow.domain.WorkflowProcess;
//import module.workflow.domain.WorkflowSystem;
//import pt.ist.bennu.core.domain.MyOrg;
//import pt.ist.bennu.core.domain.VirtualHost;
//import pt.ist.bennu.core.domain.groups.PersistentGroup;
//import pt.ist.bennu.core.domain.scheduler.ReadCustomTask;
//import pt.ist.bennu.core.domain.scheduler.TransactionalThread;
//import pt.ist.bennu.core.util.BundleUtil;
//
///**
// * @author João Antunes (joao.antunes@tagus.ist.utl.pt) - 15 de Mai de 2012
// *         Verifies that all of the {@link ProcessDocument} instances have
// *         associated {@link FileNode}s
// */
//public class CheckFileNodeExistenceOnProcessDocuments extends ReadCustomTask {
//
//    static final List<ProcessDocument> nodeLessProcessDocuments = new ArrayList<ProcessDocument>();
//
//
//    static class ProcessDocumentDeleter extends TransactionalThread {
//
//	private final PrintWriter out;
//
//	public ProcessDocumentDeleter(PrintWriter out) {
//	    this.out = out;
//	}
//
//	@SuppressWarnings("nls")
//	@Override
//	public void transactionalRun() {
//
//	    for (ProcessDocument pd : nodeLessProcessDocuments) {
//		pd.delete();
//	    }
//
//
//	}
//
//    }
//
//    @Override
//    public void doIt() {
//
//	for (VirtualHost virtualHost : MyOrg.getInstance().getVirtualHosts()) {
//	    VirtualHost.setVirtualHostForThread(virtualHost);
//	    List<WorkflowProcess> processes = WorkflowSystem.getInstance() == null ? null : WorkflowSystem.getInstance()
//		    .getProcesses();
//	    if (processes == null)
//		continue;
//
//	    for (WorkflowProcess process : processes) {
//		for (ProcessDocument pDocument : process.getFileDocuments()) {
//		    if (pDocument.getFileNode() == null) {
//			nodeLessProcessDocuments.add(pDocument);
//		    }
//		}
//	    }
//
//	    VirtualHost.releaseVirtualHostFromThread();
//
//	}
//
//	//so, now, let's show them all
//
//	out.println("Got " + nodeLessProcessDocuments.size() + " process documents");
//	for (ProcessDocument processDocument : nodeLessProcessDocuments) {
//	    WorkflowProcess process = processDocument.getProcess();
//	    String displayName = processDocument.getDisplayName();
//	    String localizedNamedFroClass = BundleUtil.getLocalizedNamedFroClass(processDocument.getClass());
//
//	    out.println("PD type: " + localizedNamedFroClass + " process " + process.getDescription() + " type "
//		    + BundleUtil.getLocalizedNamedFroClass(process.getClass()) + " displayName: " + displayName);
//	}
//
//
//	ProcessDocumentDeleter deleterThread = new ProcessDocumentDeleter(out);
//	deleterThread.start();
//	try {
//	    deleterThread.join();
//	} catch (InterruptedException e) {
//	    e.printStackTrace();
//	    throw new Error(e);
//	}
//
//	out.println("done, should have deleted them");
//
//
//    }
//
//}
