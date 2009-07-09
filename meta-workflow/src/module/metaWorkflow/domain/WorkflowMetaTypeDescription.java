package module.metaWorkflow.domain;

import org.apache.commons.lang.StringUtils;
import org.imsproject.xsd.imscpRootv1P1P2.MetadataType;
import org.joda.time.DateTime;

import module.metaWorkflow.util.versioning.Diff;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.MyOrg;

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

    
    public String getDiffWithLastVersion() {
	int version = getVersion();
	if (version == 1) {
	    return StringUtils.EMPTY;
	}
	return Diff.doDiffX(getMetaType().getDescriptionAtVersion(version-1).getDescription(), getDescription());
    }
}
