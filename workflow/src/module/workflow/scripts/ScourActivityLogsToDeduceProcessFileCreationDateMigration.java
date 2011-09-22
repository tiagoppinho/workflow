/**
 * 
 */
package module.workflow.scripts;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import module.workflow.domain.FileUploadLog;
import module.workflow.domain.ProcessFile;
import module.workflow.domain.WorkflowProcess;
import myorg.domain.scheduler.ReadCustomTask;
import myorg.util.BundleUtil;

import org.joda.time.DateTime;

import pt.ist.fenixframework.plugins.fileSupport.domain.FileSupport;
import pt.ist.fenixframework.plugins.fileSupport.domain.GenericFile;

/**
 * @author João Antunes (joao.antunes@tagus.ist.utl.pt)
 * 
 */
public class ScourActivityLogsToDeduceProcessFileCreationDateMigration extends ReadCustomTask {

    public class ProcessFileAndCreationDateBinder {
	public ProcessFileAndCreationDateBinder(DateTime creationDate, ProcessFile processFile) {
	}
    }

    @Override
    public void doIt() {

	HashMap<WorkflowProcess, Map<ProcessFile, FileUploadLog>> workflowProcessFiles = new HashMap<WorkflowProcess, Map<ProcessFile, FileUploadLog>>();

	int countNrProcessFilesFound = 0;
	int countJustGenericFilesFound = 0;
	int countNrTimestampsIdentified = 0;
	int countNrTimestampsNotIdentified = 0;
	//let's do this by getting all of the processFiles 
	for (GenericFile genericFile : FileSupport.getInstance().getGenericFiles()) {
	    if (genericFile instanceof ProcessFile) {
		countNrProcessFilesFound++;
		ProcessFile processFile = (ProcessFile) genericFile;
		Map<ProcessFile, FileUploadLog> processFiles = workflowProcessFiles.get(processFile.getProcess());
		if (processFiles == null)
		    processFiles = new HashMap<ProcessFile, FileUploadLog>();
		processFiles.put(processFile, null);
		workflowProcessFiles.put(processFile.getProcess(), processFiles);
	    } else {
		countJustGenericFilesFound++;
	    }
	}

	Map<ProcessFile, Set<FileUploadLog>> twoTimestampsFoundProcessFileList = new HashMap<ProcessFile, Set<FileUploadLog>>();

	for (WorkflowProcess process : workflowProcessFiles.keySet()) {
	    Collection<FileUploadLog> logs = Collections.EMPTY_SET;
	    if (process != null) {
		logs = (Collection<FileUploadLog>) process.getExecutionLogs(FileUploadLog.class);
	    }
	    for (ProcessFile processFile : workflowProcessFiles.get(process).keySet()) {
		boolean foundTimestamp = false;
		String filename = processFile.getFilename();
		//		if (processFile.getCreationDate() == null) { //let's only do this for the ones who don't have a date set!
		for (FileUploadLog fileUploadLog : logs) {
		    String logDescription = fileUploadLog.getDescription();
		    if (logDescription != null && (filename != null || processFile.getDisplayName() != null)
			    && (filename == null || logDescription.contains(filename))
			    && (processFile.getDisplayName() == null || logDescription.contains(processFile.getDisplayName()))) {
			//let's make sure this is the entry!
			String fileTypeName = BundleUtil.getLocalizedNamedFroClass(processFile.getClass());
			if (logDescription.contains(fileTypeName)) {
			    //we found one!!
			    foundTimestamp = true;
			    Map<ProcessFile, FileUploadLog> map = workflowProcessFiles.get(process);
			    FileUploadLog existingFileUploadLog = map.get(processFile);
			    if (existingFileUploadLog != null) {
				//we have a problem!
				//			    throw new DomainException("found.two.creationdates.for.same.file");
				Set<FileUploadLog> setDuplicates = twoTimestampsFoundProcessFileList.get(processFile);
				if (setDuplicates == null) {
				    setDuplicates = new HashSet<FileUploadLog>();
				}
				setDuplicates.add(fileUploadLog);
				setDuplicates.add(existingFileUploadLog);
				twoTimestampsFoundProcessFileList.put(processFile, setDuplicates);
			    } else {
				countNrTimestampsIdentified++;
			    }
			    map.put(processFile, fileUploadLog);
			    //			break; //let's not break, to make sure that we find eventual duplicates

			}
		    }
		}
		if (!foundTimestamp)
		    countNrTimestampsNotIdentified++;
		//		}
	    }
	}

	//let's print out the results before applying them
	int nrUnidentifiableProcessFiles = (workflowProcessFiles.get(null) != null) ? workflowProcessFiles.get(null).size() : 0;
	out.println("Results: (unidentifiable is refers only to the ProcessFiles, the GenericFiles are implicitly unidentifiable)");
	out.println("-- nr generic \t nr process \t nr.Identified \t nr.Unidentified \t nr.Unidentifiable \t nrFilesMoreThanOneCreationDate--)");
	out.println(countJustGenericFilesFound + "\t\t" + countNrProcessFilesFound + "\t\t" + countNrTimestampsIdentified
		+ "\t\t" + countNrTimestampsNotIdentified + "\t\t" + nrUnidentifiableProcessFiles + "\t\t"
		+ twoTimestampsFoundProcessFileList.size());

	//let's print out the more than one creation date occurrances:
	out.println("\nPrinting the log entries for each duplicate file found:");
	for (ProcessFile processFile : twoTimestampsFoundProcessFileList.keySet()) {
	    out.println("--");
	    String idProcesso = (processFile.getProcess() == null) ? "-" : processFile.getProcess().getDescription();
	    out.println("ProcessFile: " + processFile.getFilename() + "(" + processFile.getDisplayName() + ")" + "processo: "
		    + idProcesso);
	    for (FileUploadLog fileUploadLog : twoTimestampsFoundProcessFileList.get(processFile)) {
		out.println(fileUploadLog.getWhenOperationWasRan() + " : " + fileUploadLog.getDescription());
	    }
	    out.println("--");
	}
    }
}
