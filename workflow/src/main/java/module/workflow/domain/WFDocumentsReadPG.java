package module.workflow.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.domain.User;

public class WFDocumentsReadPG extends WFDocumentsReadPG_Base {
    
    private static String NAME = "WorkFlow Documents Read Access Group";
    
    /**
     * Use {@link WFDocumentsReadPG#WFDocumentsReadPG(WorkflowProcess)} or
     * {@link WFDocumentsReadPG#getOrCreateInstance(WorkflowProcess)} instead
     */
    @Deprecated
    public  WFDocumentsReadPG() {
        super();
    }

    public WFDocumentsReadPG(WorkflowProcess process) {
	super();
	setProcess(process);
    }

    @Override
    public boolean isMember(User user) {
	return getProcess().isFileSupportAvailable() && getProcess().isAccessible(user);
    }

    @Override
    public String getName() {
	return NAME;
    }

    @Override
    public Set<User> getMembers() {
	//TODO shall we do this?!?!
	Set<User> users = new HashSet<User>();
	for (User user : MyOrg.getInstance().getUserSet()) {
	    if (isMember(user))
		users.add(user);
	}
	return users;
    }
    
    /**
     * 
     * @param process
     *            given a {@link WorkflowProcess}, one gets the corresponding
     *            associated WFDocumentsReadPG
     * @return an instance of this class associated with the process
     */
    public static WFDocumentsReadPG getOrCreateInstance(WorkflowProcess process) {
	WFDocumentsReadPG documentsReadPersistentGroup = process.getDocumentsReadPersistentGroup();
	if (documentsReadPersistentGroup == null) {
	    documentsReadPersistentGroup = new WFDocumentsReadPG(process);
	}
	return documentsReadPersistentGroup;

    }

}
