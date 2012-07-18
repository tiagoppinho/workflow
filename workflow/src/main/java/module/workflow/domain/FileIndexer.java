/*
 * @(#)FileIndexer.java
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

import module.fileSupport.metadata.parsing.FileMetadata;
import module.fileSupport.metadata.parsing.MetadataParserChain;
import module.workflow.domain.WorkflowProcess.WorkflowProcessIndex;
import pt.ist.fenixframework.plugins.luceneIndexing.domain.IndexDocument;

/*
 * This is only here because we have that
 * nifty problem with the @Service annotation
 * that is not yet solved
 */
/**
 * 
 * @author Paulo Abrantes
 * 
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
