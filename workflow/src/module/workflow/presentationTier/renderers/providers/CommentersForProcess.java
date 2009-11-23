package module.workflow.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowProcessComment;
import module.workflow.presentationTier.actions.CommentBean;
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
	Set<User> availablePeopleToNotify = new HashSet<User>();
	WorkflowProcess process = bean.getProcess();

	availablePeopleToNotify.add(process.getProcessCreator());

	for (WorkflowProcessComment comment : process.getCommentsSet()) {
	    availablePeopleToNotify.add(comment.getCommenter());
	}

	availablePeopleToNotify.addAll(process.getProcessWorkers());

	return new ArrayList<User>(availablePeopleToNotify);
    }

}
