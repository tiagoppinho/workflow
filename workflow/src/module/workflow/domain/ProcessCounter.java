package module.workflow.domain;

import java.util.Comparator;

import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class ProcessCounter {

    public static final Comparator<ProcessCounter> COMPARATOR = new Comparator<ProcessCounter>() {
        
        @Override
        public int compare(ProcessCounter o1, ProcessCounter o2) {
            final Class c1 = o1.getProcessClass();
            final Class c2 = o2.getProcessClass();

            final String s1 = BundleUtil.getLocalizedNamedFroClass(c1);
            final String s2 = BundleUtil.getLocalizedNamedFroClass(c2);

            return s1.compareTo(s2);
        }

    };

    private final Class clazz;

    public ProcessCounter(final Class clazz) {
	this.clazz = clazz;
    }

    public Class getProcessClass() {
	return clazz;
    }

    public Class getProcessClassForForwarding() {
	return getProcessClass();
    }

    public int getCount() {
	int result = 0;
	final User requestingUser = UserView.getCurrentUser();
	for (final WorkflowProcess process : WorkflowSystem.getInstance().getProcesses()) {
	    if (shouldCountProcess(process, requestingUser)) {
		result++;
	    }
	}
        return result;
    }

    protected boolean shouldCountProcess(final WorkflowProcess process, final User requestingUser) {
	return clazz.isAssignableFrom(process.getClass())
		&& process.isAccessible(requestingUser)
		&& process.hasAnyAvailableActivity(requestingUser, true);
    }

}
