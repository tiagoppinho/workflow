package module.workflow.domain;

import module.workflow.domain.WorkflowProcess.WorkflowProcessIndex;
import pt.ist.fenixframework.plugins.luceneIndexing.domain.IndexDocument;

public class CommentIndexer {

    public static void indexCommentsInProcess(IndexDocument document, WorkflowProcess workflowProcess) {
	StringBuilder commentsBuffer = new StringBuilder();
	StringBuilder commentorsBuffer = new StringBuilder();
	for (WorkflowProcessComment comment : workflowProcess.getComments()) {
	    commentsBuffer.append(comment.getComment());
	    commentsBuffer.append(" ");
	    commentorsBuffer.append(comment.getCommenter().getPresentationName());
	    commentorsBuffer.append(" ");

	}

	document.indexField(WorkflowProcessIndex.COMMENTS, commentsBuffer.toString());
	document.indexField(WorkflowProcessIndex.COMMENTORS, commentorsBuffer.toString());

    }

}
