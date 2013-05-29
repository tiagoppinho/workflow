/**
 * 
 */
package module.workflow.scripts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import module.fileManagement.domain.DirNode;
import module.workflow.domain.AbstractWFDocsGroup;
import module.workflow.domain.ProcessDirNode;
import module.workflow.domain.WFDocsDefaultReadGroup;
import module.workflow.domain.WFDocsDefaultWriteGroup;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowSystem;
import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.domain.groups.PersistentGroup;
import pt.ist.bennu.core.domain.scheduler.ReadCustomTask;
import pt.ist.bennu.core.domain.scheduler.TransactionalThread;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author João Antunes (joao.antunes@tagus.ist.utl.pt) - 15 de Mai de 2012
 * 
 *         Assign, and <b>migrate</b> the ProcessDirNode to the existing
 *         proccesses to the new infrastructure
 */
public class AssignDirNodeRepositoryToProcessAndMigrate extends ReadCustomTask {
    static int totalProcesses = 0;
    static int totalNewRepositoriesAssigned = 0;
    static int totalNewRepositoriesChanged = 0;
    static int totalPersistentGroupsDeleted = 0;
    static Set<PersistentGroup> persistentGroupsToDelete = new HashSet<PersistentGroup>();
    static Set<PersistentGroup> persistentGroupsImpossibleToDelete = new HashSet<PersistentGroup>();
    /**
     * If true, it won't delete/change anything
     */
    public static final boolean DRY_RUN = false;

    static class MigrateProcesses extends TransactionalThread {
        private final List<String> processes = new ArrayList<String>();

        MigrateProcesses(List<WorkflowProcess> processes) {
            for (WorkflowProcess process : processes) {
                this.processes.add(process.getExternalId());
            }
        }

        @SuppressWarnings("unused")
        @Override
        public void transactionalRun() {
            for (String processId : this.processes) {
                WorkflowProcess process = FenixFramework.getDomainObject(processId);
                if (process.getDocumentsRepository() == null) {
                    if (!DRY_RUN) {
                        new ProcessDirNode(process);
                    }
                    totalNewRepositoriesAssigned++;
                } else {
                    boolean accountChange = false;
                    if (process.getDocumentsRepository().getReadGroup() != null
                            && !(process.getDocumentsRepository().getReadGroup() instanceof AbstractWFDocsGroup)) {
                        ProcessDirNode documentsRepository = process.getDocumentsRepository();
                        PersistentGroup oldReadGroup = documentsRepository.getReadGroup();
                        if (!DRY_RUN) {
                            documentsRepository.setReadGroup(WFDocsDefaultReadGroup.getOrCreateInstance(process));
                        }
                        persistentGroupsToDelete.add(oldReadGroup);
                        accountChange = true;
                    }
                    if (process.getDocumentsRepository().getWriteGroup() != null
                            && !(process.getDocumentsRepository().getWriteGroup() instanceof AbstractWFDocsGroup)) {
                        ProcessDirNode documentsRepository = process.getDocumentsRepository();
                        PersistentGroup oldWriteGroup = documentsRepository.getWriteGroup();
                        if (!DRY_RUN) {
                            documentsRepository.setWriteGroup(WFDocsDefaultWriteGroup.getOrCreateInstance(process));
                        }
                        persistentGroupsToDelete.add(oldWriteGroup);
                        accountChange = true;

                    }
                    if (process.getDocumentsRepository().getTrash().getReadGroup() != null
                            && !(process.getDocumentsRepository().getTrash().getReadGroup() instanceof AbstractWFDocsGroup)) {
                        DirNode trash = process.getDocumentsRepository().getTrash();
                        PersistentGroup oldReadGroup = trash.getReadGroup();
                        if (!DRY_RUN) {
                            trash.setReadGroup(WFDocsDefaultReadGroup.getOrCreateInstance(process));
                        }
                        persistentGroupsToDelete.add(oldReadGroup);
                        accountChange = true;

                    }
                    if (process.getDocumentsRepository().getTrash().getWriteGroup() != null
                            && !(process.getDocumentsRepository().getTrash().getWriteGroup() instanceof AbstractWFDocsGroup)) {
                        DirNode trash = process.getDocumentsRepository().getTrash();
                        PersistentGroup oldWriteGroup = trash.getWriteGroup();
                        if (!DRY_RUN) {
                            trash.setWriteGroup(WFDocsDefaultWriteGroup.getOrCreateInstance(process));
                        }
                        persistentGroupsToDelete.add(oldWriteGroup);
                        accountChange = true;

                    }
                    if (accountChange) {
                        totalNewRepositoriesChanged++;
                    }
                }
            }

        }

        //	private static void cleanGroupOfNodes(PersistentGroup group) {
        //	    for (DirNode dirNode : group.getDirNodeFromReadGroup()) {
        //		group.removeDirNodeFromReadGroup(dirNode);
        //	    }
        //	    for (DirNode dirNode : group.getDirNodeFromWriteGroup()) {
        //		group.removeDirNodeFromReadGroup(dirNode);
        //	    }
        //
        //	}

    }

    @Override
    public void doIt() {

        for (VirtualHost virtualHost : MyOrg.getInstance().getVirtualHosts()) {
            VirtualHost.setVirtualHostForThread(virtualHost);
            Set<WorkflowProcess> processes =
                    WorkflowSystem.getInstance() == null ? null : WorkflowSystem.getInstance().getProcesses();
            if (processes == null) {
                continue;
            }
            //let's process 100 processes per thread
            ArrayList<WorkflowProcess> processesToProcess = new ArrayList<WorkflowProcess>();
            Iterator<WorkflowProcess> iterator = processes.iterator();
            do {
                totalProcesses++;
                processesToProcess.add(iterator.next());
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

            } while (iterator.hasNext());

            VirtualHost.releaseVirtualHostFromThread();

        }

        //so, now, let's delete all the groups, if possible

        out.println("Went through " + totalProcesses + " processes, created " + totalNewRepositoriesAssigned + " changed "
                + totalNewRepositoriesChanged + " Deleted " + totalPersistentGroupsDeleted + " groups");

        out.println("Listing " + persistentGroupsImpossibleToDelete.size() + " groups that we were unable to delete");
        for (PersistentGroup group : persistentGroupsImpossibleToDelete) {
            out.println(group.getExternalId());
        }

    }

}
