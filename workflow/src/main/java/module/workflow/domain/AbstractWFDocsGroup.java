package module.workflow.domain;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.domain.User;

public abstract class AbstractWFDocsGroup extends AbstractWFDocsGroup_Base {
    
    public AbstractWFDocsGroup(WorkflowProcess process) {
        super();
	setProcess(process);
    }

    public AbstractWFDocsGroup() {
    }

    @Override
    abstract public boolean isMember(User user);

    @Override
    abstract public String getName();

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
    
    public static <P extends AbstractWFDocsGroup> P getOrCreateInstance(WorkflowProcess process, Class<P> groupClass) {
  	P group = (process.getDocumentsRepository() == null || !(groupClass.isInstance(process.getDocumentsRepository()
		.getReadGroup()))) ? null
  		: (P) process
  		.getDocumentsRepository().getReadGroup();
  	if (group == null) {
	    Constructor<P> constructor;
	    try {
		constructor = groupClass.getConstructor(WorkflowProcess.class);
  	    group = constructor.newInstance(process);
	    } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
		    | IllegalArgumentException | InvocationTargetException e) {
		throw new Error(e);
	    }
  	}
  	return group;
      }
    

}
