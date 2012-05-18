/**
 * 
 */
package module.workflow.scripts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import module.workflow.domain.ProcessDirNode;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowSystem;
import myorg.domain.MyOrg;
import myorg.domain.VirtualHost;
import myorg.domain.scheduler.ReadCustomTask;
import myorg.domain.scheduler.TransactionalThread;

/**
 * @author João Antunes (joao.antunes@tagus.ist.utl.pt) - 15 de Mai de 2012
 * 
 * 
 */
public class AssignDirNodeRepositoryToProcess extends ReadCustomTask {
    static int totalProcesses = 0;
    static int totalNewRepositoriesAssigned = 0;

    static class MigrateProcesses extends TransactionalThread {
	private final List<WorkflowProcess> processes;

	MigrateProcesses(List<WorkflowProcess> processes) {
	    this.processes = processes;
	}

	@Override
	public void transactionalRun() {
	    for (WorkflowProcess process : processes) {
		totalProcesses++;
		if (process.getDocumentsRepository() == null) {
		    new ProcessDirNode(process);
		    totalNewRepositoriesAssigned++;
		}
	    }
	}

    }

    protected void migrateProcesses(List<WorkflowProcess> processes) {

    }

    @Override
    public void doIt() {

	for (VirtualHost virtualHost : MyOrg.getInstance().getVirtualHosts()) {
	    VirtualHost.setVirtualHostForThread(virtualHost);
	    List<WorkflowProcess> processes = WorkflowSystem.getInstance() == null ? null : WorkflowSystem.getInstance()
		    .getProcesses();
	    if (processes == null)
		continue;
	    //let's process 100 processes per thread
	    ArrayList<WorkflowProcess> processesToProcess = new ArrayList<WorkflowProcess>();
	    Iterator<WorkflowProcess> iterator = processes.iterator();
	    do {
		if (processesToProcess.size() >= 100 || !iterator.hasNext()) {
		    MigrateProcesses migrateProcesses = new MigrateProcesses(processesToProcess);
		    migrateProcesses.start();
		    try {
			migrateProcesses.join();
		    } catch (InterruptedException e) {
			e.printStackTrace();
			throw new Error(e);
		    }
		    processesToProcess = new ArrayList<WorkflowProcess>();
		}
		processesToProcess.add(iterator.next());

	    } while (iterator.hasNext());

	    VirtualHost.releaseVirtualHostFromThread();

	}

	out.println("Went through " + totalProcesses + " processes, changed " + totalNewRepositoriesAssigned);

    }

}
