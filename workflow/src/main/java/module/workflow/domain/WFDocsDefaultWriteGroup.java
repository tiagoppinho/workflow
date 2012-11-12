package module.workflow.domain;

import pt.ist.bennu.core.domain.User;

public class WFDocsDefaultWriteGroup extends WFDocsDefaultWriteGroup_Base {
    
    private static String NAME = "WorkFlow Documents Write Access Group";

    /**
     * For use with _Base only. Use
     * {@link WFDocsDefaultWriteGroup#WFDocsDefaultWriteGroup(WorkflowProcess)}
     * instead
     */
    @Deprecated
    public  WFDocsDefaultWriteGroup() {
        super();
    }

    public WFDocsDefaultWriteGroup(WorkflowProcess process) {
	super();
	setProcess(process);
    }

    @Override
    public boolean isMember(User user) {
	return super.isMember(user) && getProcess().isFileEditionAllowed() && getProcess().isFileEditionAllowed(user);
    }

    @Override
    public String getName() {
	return NAME;
    }
    
    public static module.workflow.domain.AbstractWFDocsGroup getOrCreateInstance(WorkflowProcess process) {
	WFDocsDefaultWriteGroup writeGroup = (process.getDocumentsRepository() == null || !(process.getDocumentsRepository()
		.getReadGroup() instanceof WFDocsDefaultWriteGroup)) ? null : (WFDocsDefaultWriteGroup) process
		.getDocumentsRepository().getWriteGroup();
	if (writeGroup == null) {
	    writeGroup = new WFDocsDefaultWriteGroup(process);
	}
	return writeGroup;
    }

}
