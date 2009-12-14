package module.workflow.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowProcessComment;
import module.workflow.presentationTier.actions.CommentBean;
import module.workflow.presentationTier.actions.UserNotificationBean;
import myorg.domain.User;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CommentersForProcess implements DataProvider {

    @Override
    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	CommentBean bean = (CommentBean) source;
	Set<UserNotificationBean> availablePeopleToNotify = new HashSet<UserNotificationBean>();
	WorkflowProcess process = bean.getProcess();

	availablePeopleToNotify.add(new UserNotificationBean(process.getProcessCreator(), process));

	for (WorkflowProcessComment comment : process.getCommentsSet()) {
	    availablePeopleToNotify.add(new UserNotificationBean(comment.getCommenter(), process));
	}

	availablePeopleToNotify.addAll(getWorkers(process));

	return new ArrayList<UserNotificationBean>(availablePeopleToNotify);
    }

    private List<UserNotificationBean> getWorkers(WorkflowProcess process) {
	List<UserNotificationBean> moreBeans = new ArrayList<UserNotificationBean>();
	for (User user : process.getProcessWorkers()) {
	    moreBeans.add(new UserNotificationBean(user, process));
	}
	return moreBeans;
    }
}
