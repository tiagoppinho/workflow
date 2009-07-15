package module.metaWorkflow.domain;

import module.metaWorkflow.util.versioning.DiffUtil;
import module.metaWorkflow.util.versioning.DiffUtil.Revision;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.MyOrg;

import org.joda.time.DateTime;

public class WorkflowMetaTypeDescription extends WorkflowMetaTypeDescription_Base implements
	Comparable<WorkflowMetaTypeDescription> {

    public WorkflowMetaTypeDescription(WorkflowMetaType workflowMetaType, String description, int version) {
	super();
	super.setMetaType(workflowMetaType);
	super.setDescription(description);
	super.setDate(new DateTime());
	super.setVersion(version);
	super.setMyOrg(MyOrg.getInstance());
	super.setVersionOwner(UserView.getCurrentUser());
    }

    @Override
    public int compareTo(WorkflowMetaTypeDescription otherDescription) {
	return Integer.valueOf(getVersion()).compareTo(otherDescription.getVersion());
    }

    @Override
    public void setDescription(String description) {
	getMetaType().addDescription(description);
    }

    public Revision getDiffWithVersion(int version) {
	return getDiffWith(getMetaType().getDescriptionAtVersion(version - 1));
    }

    public Revision getDiffWith(WorkflowMetaTypeDescription description) {
	return DiffUtil.diff(getDescription(), description.getDescription());
    }
}
