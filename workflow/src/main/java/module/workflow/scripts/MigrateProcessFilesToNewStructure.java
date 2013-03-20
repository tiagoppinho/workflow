/**
 * 
 */
package module.workflow.scripts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jvstm.cps.ConsistencyException;
import module.fileManagement.domain.WriteDeniedException;
import module.workflow.domain.FileUploadLog;
import module.workflow.domain.ProcessFile;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowSystem;
import module.workflow.domain.exceptions.DuplicateProcessFileNameException;

import org.apache.commons.lang.StringUtils;

import pt.ist.bennu.core.applicationTier.Authenticate;
import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.domain.scheduler.ReadCustomTask;
import pt.ist.bennu.core.domain.scheduler.TransactionalThread;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.TransactionError;
import pt.ist.fenixframework.core.WriteOnReadError;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;
import com.vaadin.data.Buffered;

/**
 * @author João Antunes (joao.antunes@tagus.ist.utl.pt) - 29 de Nov de 2012
 *         Migrate the existing ProcessFiles to use the new structure (i.e.
 *         associated with a Document and with fileNode, etc)
 * 
 */
public class MigrateProcessFilesToNewStructure extends ReadCustomTask {
    static int totalProcesses = 0;
    static int totalProcessesUnableToBeMigrated = 0;
    static int totalFilesMigrated = 0;
    static int totalFilesAlreadyMigrated = 0;
    static int foundUsers = 0;
    static int totalProcessFilesPassedThrough = 0;

    static Map<ProcessFile, Throwable> processFilesUnableToBeMigrated = new HashMap<ProcessFile, Throwable>();
    static Set<ProcessFile> processFilesWithoutUser = new HashSet<ProcessFile>();
    //    static Set<ProcessFile> processFilesWithoutCreationDate = new HashSet<ProcessFile>();
    static Multimap<ProcessFile, User> processFilesWithUserButNoPermission = HashMultimap.create();
    /**
     * If true, it won't delete/change anything
     */
    public static final boolean DRY_RUN = false;

    static class MigrateFiles extends TransactionalThread {
        private final List<String> processes = new ArrayList<String>();

        MigrateFiles(List<WorkflowProcess> processes) {
            for (WorkflowProcess process : processes) {
                this.processes.add(process.getExternalId());
            }
        }

        public static void handleException(Throwable throwable) {
            // This is a little hackish but is somewhat forced by the
            // combination of architectures of both vaadin and the jvstm
            if (throwable instanceof WriteOnReadError) {
                throw (WriteOnReadError) throwable;
            } else if (throwable instanceof ConsistencyException) {
                throw (ConsistencyException) throwable;
            } else if (throwable instanceof TransactionError) {
                throw (TransactionError) throwable;
            } else if (throwable instanceof Buffered.SourceException) {
                for (Throwable cause : ((Buffered.SourceException) throwable).getCauses()) {
                    handleException(cause);
                }
            } else if (throwable.getCause() != null) {
                handleException(throwable.getCause());
            }
        }

        @SuppressWarnings("unused")
        @Override
        public void transactionalRun() {
            boolean foundException = false;
            for (String processId : this.processes) {
                WorkflowProcess process = FenixFramework.getDomainObject(processId);
                if (process.getDocumentsRepository() == null) {
                    totalProcessesUnableToBeMigrated++;
                } else {

                    Set<ProcessFile> files = process.getFiles();

                    for (ProcessFile file : files) {
                        totalProcessFilesPassedThrough++;
                        if (file.isInNewStructure()) {
                            totalFilesAlreadyMigrated++;
                            continue;
                        } else {
                            if (!DRY_RUN) {
                                User userWhoUploaded = FileUploadLog.tryToGetUserWhoUploaded(file);
                                if (userWhoUploaded != null) {
                                    foundUsers++;
                                    foundException = migrateFile(file, process, userWhoUploaded, file.getDisplayName());
                                } else {
                                    processFilesWithoutUser.add(file);

                                }

                            }
                        }
                    }
                }// if (process.getDocumentsRepository() == null)

                if (foundException) {
                    throw new Error("Got an exception, aborting this");
                }
            }// for (String processId : this.processes) 
        }

        private boolean migrateFile(ProcessFile file, WorkflowProcess process, User userWhoUploaded, String originalDisplayName) {
            boolean foundException = false;
            //let's migrate the process files

            //let's get the potential user that uploaded this file
            if (file.getCreationDate() == null) {
                //		processFilesWithoutCreationDate.add(file);
                return false;
            }
            final pt.ist.bennu.core.applicationTier.Authenticate.UserView userView = Authenticate.authenticate(userWhoUploaded);
            pt.ist.fenixWebFramework.security.UserView.setUser(userView);
            try {
                process.migrateFileToNewStructure(file);
                totalFilesMigrated++;
                if (!originalDisplayName.equals(file.getDisplayName())) {
                    //TODO log the filename change here
                    //		    new LabelLog(process, userWhoUploaded, ", bundle, arguments)
                }

            } catch (WriteDeniedException deniedException) {
                processFilesWithUserButNoPermission.put(file, userWhoUploaded);

            } catch (DuplicateProcessFileNameException ex) {
                correctDisplayName(file, originalDisplayName);
                migrateFile(file, process, userWhoUploaded, originalDisplayName);

            } catch (Throwable ex) {
                handleException(ex);
                foundException = true;
                processFilesUnableToBeMigrated.put(file, ex);
            } finally {
                pt.ist.fenixWebFramework.security.UserView.setUser(null);
            }

            return foundException;

        }

        /**
         * 
         * @param processFile
         *            to correct the display name corrects the display name i.e.
         */
        private void correctDisplayName(ProcessFile processFile, String originalFileName) {
            String displayName = processFile.getDisplayName();

            if (StringUtils.equals(displayName, originalFileName)) {
                if (StringUtils.equals(processFile.getDisplayName(), processFile.getFilename())) {
                    //then we should search for the last instance of a . (if any) and append the -1 there
                    int lastIndexOf = StringUtils.lastIndexOf(displayName, '.');
                    if (lastIndexOf == -1) {
                        //let's just add -1 in the end
                        processFile.setDisplayName(displayName + "-1");
                    } else {
                        //let's put the -1 before the last .
                        String suffix = StringUtils.substring(displayName, lastIndexOf, displayName.length());
                        String prefix = StringUtils.substring(displayName, 0, lastIndexOf);
                        processFile.setDisplayName(prefix + "-1" + suffix);
                    }
                } else {
                    //let's just add the -1 in the end
                    processFile.setDisplayName(displayName + "-1");

                }
            } else {
                //then this is not the first iteration, let's get the suffix that we appended, and make raise by one
                String difference = StringUtils.difference(displayName, originalFileName);
                // so this difference should have something like '-N' where N is a number
                Integer currentNumber = Integer.valueOf(StringUtils.substringAfter(difference, "-"));

                //now let's see where should we be replacing the difference
                int indexOfDifference = StringUtils.indexOfDifference(displayName, originalFileName);

                //let's get the suffix after the artificial -N we put in

                String substring = StringUtils.substring(displayName, indexOfDifference);
                String suffix = StringUtils.replace(substring, difference, "", 1);

                //let's get the prefix
                String prefix = StringUtils.substring(displayName, 0, indexOfDifference);

                //ok, so now, let's make the change
                processFile.setDisplayName(prefix + "-" + String.valueOf(currentNumber + 1) + suffix);

                //all done :)

            }

        }
    }

    @Override
    public void doIt() {
        // TODO Auto-generated method stub
        for (VirtualHost virtualHost : MyOrg.getInstance().getVirtualHosts()) {
            VirtualHost.setVirtualHostForThread(virtualHost);
            Set<WorkflowProcess> processes =
                    WorkflowSystem.getInstance() == null ? null : WorkflowSystem.getInstance().getProcesses();
            if (processes == null) {
                continue;
            }
            //let's process 1000 processes per thread
            ArrayList<WorkflowProcess> processesToProcess = new ArrayList<WorkflowProcess>();
            Iterator<WorkflowProcess> iterator = processes.iterator();
            do {
                totalProcesses++;
                processesToProcess.add(iterator.next());
                if (processesToProcess.size() >= 1000 || !iterator.hasNext()) {
                    MigrateFiles migrateFiles = new MigrateFiles(processesToProcess);
                    migrateFiles.start();
                    try {
                        migrateFiles.join();
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

        out.println("Went through " + totalProcesses + " processes. Could not migrate (due to lack of repository): "
                + totalProcessesUnableToBeMigrated);
        out.println("Went through " + totalProcessFilesPassedThrough + " files. Had already migrated "
                + totalFilesAlreadyMigrated + " total files migrated " + totalFilesMigrated
                + " total files unable to migrate  due to exception" + processFilesUnableToBeMigrated.size());

        out.println("Listing the exceptions that caused the processFiles not to be migrated ");
        for (Throwable ex : processFilesUnableToBeMigrated.values()) {
            out.println(ex.toString());
        }

        out.println("Number of files for which we could not find the user: " + processFilesWithoutUser.size()
                + " listing the classes");
        Multiset<Class<? extends ProcessFile>> processFilesWithoutUserClasses = HashMultiset.create();
        for (ProcessFile processFileWithoutUser : processFilesWithoutUser) {
            processFilesWithoutUserClasses.add(processFileWithoutUser.getClass());
        }

        for (Entry<Class<? extends ProcessFile>> fileClassEntry : processFilesWithoutUserClasses.entrySet()) {
            Class<? extends ProcessFile> clazz = fileClassEntry.getElement();
            out.println("Class " + clazz.getSimpleName() + " nr: " + processFilesWithoutUserClasses.count(clazz));
        }

        //	out.println("Number of files for without creation date: " + processFilesWithoutCreationDate.size()
        //		+ " listing the classes");
        //	Multiset<Class<? extends ProcessFile>> processFilesWithoutCreationDateClasses = HashMultiset.create();
        //	for (ProcessFile processFileWithoutCreationDate : processFilesWithoutCreationDate) {
        //	    processFilesWithoutCreationDateClasses.add(processFileWithoutCreationDate.getClass());
        //	}

        for (Entry<Class<? extends ProcessFile>> fileClassEntry : processFilesWithoutUserClasses.entrySet()) {
            Class<? extends ProcessFile> clazz = fileClassEntry.getElement();
            out.println("Class " + clazz.getSimpleName() + " nr: " + processFilesWithoutUserClasses.count(clazz));
        }

        out.println("Got " + processFilesWithUserButNoPermission.size()
                + " process files for whom we found a user, but they had no permission");

        out.println("Found " + foundUsers + " users");
    }

}