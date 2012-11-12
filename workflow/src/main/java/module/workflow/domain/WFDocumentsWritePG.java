package module.workflow.domain;

import java.util.Set;

import pt.ist.bennu.core.domain.User;

public class WFDocumentsWritePG extends WFDocumentsWritePG_Base {
    
    public  WFDocumentsWritePG() {
        super();
    }

    @Override
    public boolean isMember(User user) {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public void delete() {
	setProcess(null);
	super.delete();
    }

    @Override
    public String getName() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Set<User> getMembers() {
	// TODO Auto-generated method stub
	return null;
    }
    
}
