package module.workflow.domain;

import java.util.HashSet;
import java.util.Set;

import myorg.domain.User;

public class WFDocumentsWritePG extends WFDocumentsWritePG_Base {
    private static String NAME = "WorkFlow Documents Write Access Group";
    
    /**
     * Use {@link WFDocumentsWritePG#WFDocumentsWritePG(WorkflowProcess)} or
     * {@link WFDocumentsWritePG#getOrCreateInstance(WorkflowProcess)} instead
     */
    @Deprecated
    public  WFDocumentsWritePG() {
        super();
    }

    private WFDocumentsWritePG(WorkflowProcess process) {
	super();
	setProcess(process);
    }

    @Override
    public boolean isMember(User user) {
	return getProcess().getDocumentsReadPersistentGroup().isMember(user) && getProcess().isFileEditionAllowed()
		&& getProcess().isFileEditionAllowed(user);
    }

    @Override
    public String getName() {
	return NAME;
    }

    @Override
    public Set<User> getMembers() {
	Set<User> users = new HashSet();
	for (User user : getProcess().getDocumentsWritePersistentGroup().getMembers()) {
	    if (getProcess().isFileEditionAllowed(user))
		users.add(user);
	}
	return users;
    }
    
    /**
     * 
     * @param process
     *            given a {@link WorkflowProcess}, one gets the corresponding
     *            associated WFDocumentsWritePG
     * @return an instance of this class associated with the process
     */
    public static WFDocumentsWritePG getOrCreateInstance(WorkflowProcess process) {
	WFDocumentsWritePG documentsWritePersistentGroup = process.getDocumentsWritePersistentGroup();
	if (documentsWritePersistentGroup == null) {
	    documentsWritePersistentGroup = new WFDocumentsWritePG(process);
	}
	return documentsWritePersistentGroup;

    }

}
