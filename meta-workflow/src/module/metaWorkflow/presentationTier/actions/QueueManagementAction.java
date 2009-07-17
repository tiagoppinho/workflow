package module.metaWorkflow.presentationTier.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.metaWorkflow.domain.WorkflowQueue;
import module.metaWorkflow.domain.WorkflowUserGroupQueue;
import module.metaWorkflow.domain.WorkflowUserGroupQueueBean;
import module.metaWorkflow.util.WorkflowQueueBean;
import myorg.domain.MyOrg;
import myorg.domain.User;
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

	request.setAttribute("bean", new WorkflowQueueBean());
	request.setAttribute("queues", MyOrg.getInstance().getMetaWorkflowQueues());

	return forward(request, "/metaWorkflow/manageQueues.jsp");
    }

    @SuppressWarnings("unchecked")
    public ActionForward selectQueueType(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	Class<? extends WorkflowQueue> selectedType = getRenderedObject("queueType");
	WorkflowQueueBean bean;
	try {
	    Class<? extends WorkflowQueueBean> beanClass = (Class<? extends WorkflowQueueBean>) Class.forName(selectedType
		    .getName()
		    + "Bean");
	    bean = beanClass.newInstance();
	    bean.setQueueType(selectedType);
	} catch (Exception e) {
	    e.printStackTrace();
	    return manageQueues(mapping, form, request, response);
	}

	request.setAttribute("bean", bean);

	request.setAttribute("queues", MyOrg.getInstance().getMetaWorkflowQueues());

	return forward(request, "/metaWorkflow/manageQueues.jsp");
    }

    public ActionForward oneMoreUserInQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	WorkflowUserGroupQueueBean bean = getRenderedObject("newQueue");
	User userToAdd = bean.getUserToAdd();
	if (userToAdd != null) {
	    bean.addUser(userToAdd);
	}

	bean.setUserToAdd(null);
	request.setAttribute("bean", bean);
	request.setAttribute("queues", MyOrg.getInstance().getMetaWorkflowQueues());

	RenderUtils.invalidateViewState("users");

	return forward(request, "/metaWorkflow/manageQueues.jsp");
    }

    public ActionForward createNewQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	WorkflowUserGroupQueueBean bean = getRenderedObject("newQueue");
	WorkflowQueue.createQueue(bean.getQueueType(), bean);

	RenderUtils.invalidateViewState("newQueue");
	return manageQueues(mapping, form, request, response);
    }

}
