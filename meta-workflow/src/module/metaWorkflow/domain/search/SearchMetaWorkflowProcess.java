package module.metaWorkflow.domain.search;

import java.util.Collection;
import java.util.Set;

import module.metaWorkflow.domain.WorkflowMetaProcess;
import module.metaWorkflow.domain.WorkflowMetaType;
import module.metaWorkflow.domain.WorkflowQueue;
import module.workflow.domain.WorkflowProcess;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import myorg.domain.util.Search;

import org.apache.commons.collections.Predicate;

import pt.ist.fenixWebFramework.util.DomainReference;

public class SearchMetaWorkflowProcess extends Search<WorkflowMetaProcess> {

    private DomainReference<User> requestor;
    private DomainReference<User> creator;
    private DomainReference<WorkflowMetaType> metaType;
    private DomainReference<WorkflowQueue> queue;

    public User getRequestor() {
	return requestor.getObject();
    }

    public void setRequestor(User requestor) {
	this.requestor = new DomainReference<User>(requestor);
    }

    public User getCreator() {
	return creator.getObject();
    }

    public void setCreator(User creator) {
	this.creator = new DomainReference<User>(creator);
    }

    public WorkflowMetaType getMetaType() {
	return metaType.getObject();
    }

    public void setMetaType(WorkflowMetaType metaType) {
	this.metaType = new DomainReference<WorkflowMetaType>(metaType);
    }

    public WorkflowQueue getQueue() {
	return queue.getObject();
    }

    public void setQueue(WorkflowQueue queue) {
	this.queue = new DomainReference<WorkflowQueue>(queue);
    }

    public SearchMetaWorkflowProcess() {
	setRequestor(null);
	setCreator(null);
	setMetaType(null);
	setQueue(null);
    }

    @Override
    public Set<WorkflowMetaProcess> search() {
	try {
	    return new WorkflowMetaProcessSearchResult(getProcesses());
	} catch (Exception ex) {
	    ex.printStackTrace();
	    throw new Error(ex);
	}

    }

    private Collection<WorkflowMetaProcess> getProcesses() {
	final User currentUser = UserView.getCurrentUser();
	return WorkflowProcess.getAllProcesses(WorkflowMetaProcess.class, new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		return ((WorkflowMetaProcess) arg0).isAccessible(currentUser);
	    }

	});

    }

    private class WorkflowMetaProcessSearchResult extends SearchResultSet<WorkflowMetaProcess> {

	public WorkflowMetaProcessSearchResult(Collection<WorkflowMetaProcess> c) {
	    super(c);
	}

	@Override
	protected boolean matchesSearchCriteria(WorkflowMetaProcess process) {
	    return matchCriteria(getCreator(), process.getCreator())
		    && matchCriteria(getRequestor(), process.getRequestor().getUser())
		    && matchCriteria(getMetaType(), process.getMetaType()) && matchCriteria(getQueue(), process.getCurrentQueue());
	}
    }
}
