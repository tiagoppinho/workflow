package module.workflow.domain;

import module.fileSupport.metadata.parsing.FileMetadata;
import module.fileSupport.metadata.parsing.MetadataParserChain;
import module.workflow.domain.WorkflowProcess.WorkflowProcessIndex;
import pt.ist.fenixframework.plugins.luceneIndexing.domain.IndexDocument;

/*
 * This is only here because we have that
 * nifty problem with the @Service annotation
 * that is not yet solved
 */
public class FileIndexer {

    public static void indexFilesInProcess(IndexDocument document, WorkflowProcess process) {

	FileMetadata parsedFiles = MetadataParserChain.parseFiles(process.getFiles());

	if (parsedFiles.hasContent()) {
	    for (String key : parsedFiles.keySet()) {
		document.indexField(WorkflowProcessIndex.FILE, parsedFiles.getObject(key));
	    }
	}
    }
}
