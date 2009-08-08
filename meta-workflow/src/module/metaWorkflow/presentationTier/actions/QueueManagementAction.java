package module.metaWorkflow.presentationTier.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.metaWorkflow.domain.WorkflowQueue;
import module.metaWorkflow.domain.WorkflowUserGroupQueue;
import module.metaWorkflow.domain.WorkflowUserGroupQueueBean;
import module.metaWorkflow.presentationTier.WorkflowQueueLayoutContext;
import module.metaWorkflow.util.WorkflowQueueBean;
import myorg.domain.MyOrg;
import myorg.domain.User;
import myorg.presentationTier.Context;
import myorg.presentationTier.actions.ContextBaseAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/metaWorkflowQueueManagement")
public class QueueManagementAction extends ContextBaseAction {

    public ActionForward manageQueues(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	request.setAttribute("queues", MyOrg.getInstance().getMetaWorkflowQueues());

	return forward(request, "/metaWorkflow/queues/manageQueues.jsp");
    }

    public ActionForward viewQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	WorkflowQueue queue = getDomainObject(request, "queueId");
	request.setAttribute("queue", queue);

	return forward(request, "/metaWorkflow/queues/viewQueue.jsp");
    }

    public ActionForward prepareEditQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

	WorkflowQueue queue = getDomainObject(request, "queueId");
	WorkflowQueueBean beanForType = getBeanForType(queue.getClass());
	beanForType.setQueue(queue);

	request.setAttribute("bean", beanForType);

	return forward(request, "/metaWorkflow/queues/editQueue.jsp");
    }

    public ActionForward editQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

	WorkflowQueueBean bean = getRenderedObject("queue");
	bean.getQueue().edit(bean);

	return manageQueues(mapping, form, request, response);
    }

    public ActionForward prepareCreateQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	request.setAttribute("bean", new WorkflowQueueBean());

	return forward(request, "/metaWorkflow/queues/createQueue.jsp");
    }

    public ActionForward doPostback(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	WorkflowQueueBean bean = getRenderedObject("newQueue");
	RenderUtils.invalidateViewState();

	Class<? extends WorkflowQueue> queueType = bean.getQueueType();
	request.setAttribute("creationPage", WorkflowQueueLayoutContext.getBootstrapFor(queueType));
	request.setAttribute("bean", bean);

	return forward(request, "/metaWorkflow/queues/createQueue.jsp");
    }

    @SuppressWarnings("unchecked")
    public ActionForward selectQueueType(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	Class<? extends WorkflowQueue> selectedType = getRenderedObject("queueType");
	WorkflowQueueBean bean;
	try {
	    bean = getBeanForType(selectedType);
	} catch (Exception e) {
	    e.printStackTrace();
	    return manageQueues(mapping, form, request, response);
	}

	request.setAttribute("bean", bean);
	request.setAttribute("creationPage", WorkflowQueueLayoutContext.getBootstrapFor(selectedType));

	return forward(request, "/metaWorkflow/queues/createQueue.jsp");
    }

    private WorkflowQueueBean getBeanForType(Class<? extends WorkflowQueue> selectedType) throws ClassNotFoundException,
	    InstantiationException, IllegalAccessException {
	WorkflowQueueBean bean;
	Class<? extends WorkflowQueueBean> beanClass = (Class<? extends WorkflowQueueBean>) Class.forName(selectedType.getName()
		+ "Bean");
	bean = beanClass.newInstance();
	bean.setQueueType(selectedType);
	return bean;
    }

    public ActionForward removeUser(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

	WorkflowUserGroupQueue queue = getDomainObject(request, "queueId");
	User user = getDomainObject(request, "userId");
	queue.removeUsers(user);

	return prepareEditQueue(mapping, form, request, response);
    }

    public ActionForward oneMoreUserInQueueInCreation(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	WorkflowUserGroupQueueBean bean = getRenderedObject("newQueue");
	addUserInBean(request, bean);

	request.setAttribute("creationPage", WorkflowQueueLayoutContext.getBootstrapFor(bean.getQueueType()));
	request.setAttribute("bean", bean);

	return forward(request, "/metaWorkflow/queues/createQueue.jsp");
    }

    public ActionForward oneMoreUserInQueueInEdition(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	WorkflowUserGroupQueueBean bean = getRenderedObject("queue");
	addUserInBean(request, bean);

	return forward(request, "/metaWorkflow/queues/editQueue.jsp");
    }

    private void addUserInBean(final HttpServletRequest request, WorkflowUserGroupQueueBean bean) {
	User userToAdd = bean.getUserToAdd();
	if (userToAdd != null) {
	    bean.addUser(userToAdd);
	}

	bean.setUserToAdd(null);
	request.setAttribute("bean", bean);
	request.setAttribute("queues", MyOrg.getInstance().getMetaWorkflowQueues());

	RenderUtils.invalidateViewState("users");
    }

    public ActionForward createNewQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	WorkflowQueueBean bean = getRenderedObject("newQueue");
	WorkflowQueue.createQueue(bean.getQueueType(), bean);

	RenderUtils.invalidateViewState("newQueue");
	return manageQueues(mapping, form, request, response);
    }

    @Override
    public Context createContext(String contextPathString, HttpServletRequest request) {

	WorkflowQueue queue = getDomainObject(request, "queueId");
	if (queue != null) {
	    WorkflowQueueLayoutContext queueContext = queue.getDefaultContext();
	    queueContext.setElements(contextPathString);
	    return queueContext;
	}
	return super.createContext(contextPathString, request);
    }
}
