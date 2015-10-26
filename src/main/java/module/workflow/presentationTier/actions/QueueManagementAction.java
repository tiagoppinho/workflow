/*
 * @(#)QueueManagementAction.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Case Handleing Based Workflow Module.
 *
 *   The Case Handleing Based Workflow Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Workflow Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Workflow Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.workflow.presentationTier.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.workflow.domain.WorkflowQueue;
import module.workflow.domain.WorkflowSystem;
import module.workflow.domain.WorkflowUserGroupQueue;
import module.workflow.domain.WorkflowUserGroupQueueBean;
import module.workflow.presentationTier.WorkflowQueueLayoutContext;
import module.workflow.util.WorkflowQueueBean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.base.BaseAction;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsApplication;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@Mapping(path = "/workflowQueueManagement")
@StrutsApplication(bundle = "WorkflowResources", path = "manage-workflow-queues", titleKey = "link.topBar.QueueManagement",
        hint = "Workflow", accessGroup = "#managers")
/**
 * 
 * @author Anil Kassamali
 * @author Paulo Abrantes
 * 
 */
public class QueueManagementAction extends BaseAction {

    private final ActionForward doForward(final HttpServletRequest request, final String body) {
        WorkflowQueue queue = getDomainObject(request, "queueId");
        if (queue != null) {
            final WorkflowQueueLayoutContext queueContext = queue.getDefaultContext();
            request.setAttribute("context", queueContext);
        }
        return forward(body);
    }

    @EntryPoint
    public ActionForward manageQueues(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        request.setAttribute("queues", WorkflowSystem.getInstance().getWorkflowQueuesSet());

        return doForward(request, "/workflow/queues/manageQueues.jsp");
    }

    public ActionForward viewQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        final WorkflowQueue queue = getDomainObject(request, "queueId");
        request.setAttribute("queue", queue);

        return doForward(request, "/workflow/queues/viewQueue.jsp");
    }

    public ActionForward prepareEditQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        final WorkflowQueue queue = getDomainObject(request, "queueId");
        final WorkflowQueueBean beanForType = getBeanForType(queue.getClass());
        beanForType.setQueue(queue);

        request.setAttribute("bean", beanForType);

        return doForward(request, "/workflow/queues/editQueue.jsp");
    }

    public ActionForward editQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        final WorkflowQueueBean bean = getRenderedObject("queue");
        bean.getQueue().edit(bean);

        return manageQueues(mapping, form, request, response);
    }

    public ActionForward prepareCreateQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        request.setAttribute("bean", new WorkflowQueueBean());

        return doForward(request, "/workflow/queues/createQueue.jsp");
    }

    public ActionForward doPostback(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        final WorkflowQueueBean bean = getRenderedObject("newQueue");
        RenderUtils.invalidateViewState();

        final Class<? extends WorkflowQueue> queueType = bean.getQueueType();
        request.setAttribute("creationPage", WorkflowQueueLayoutContext.getBootstrapFor(queueType));
        request.setAttribute("bean", bean);

        return doForward(request, "/workflow/queues/createQueue.jsp");
    }

    @SuppressWarnings("unchecked")
    public ActionForward selectQueueType(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        final Class<? extends WorkflowQueue> selectedType = getRenderedObject("queueType");
        final WorkflowQueueBean bean;
        try {
            bean = getBeanForType(selectedType);
        } catch (Exception e) {
            e.printStackTrace();
            return manageQueues(mapping, form, request, response);
        }

        request.setAttribute("bean", bean);
        request.setAttribute("creationPage", WorkflowQueueLayoutContext.getBootstrapFor(selectedType));

        return doForward(request, "/workflow/queues/createQueue.jsp");
    }

    private WorkflowQueueBean getBeanForType(final Class<? extends WorkflowQueue> selectedType) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        final Class<? extends WorkflowQueueBean> beanClass =
                (Class<? extends WorkflowQueueBean>) Class.forName(selectedType.getName() + "Bean");
        final WorkflowQueueBean bean = beanClass.newInstance();
        bean.setQueueType(selectedType);
        return bean;
    }

    public ActionForward removeUser(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        final WorkflowUserGroupQueue queue = getDomainObject(request, "queueId");
        final User user = getDomainObject(request, "userId");
        queue.removeUsers(user);

        return prepareEditQueue(mapping, form, request, response);
    }

    public ActionForward oneMoreUserInQueueInCreation(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        final WorkflowUserGroupQueueBean bean = getRenderedObject("newQueue");
        addUserInBean(request, bean);

        request.setAttribute("creationPage", WorkflowQueueLayoutContext.getBootstrapFor(bean.getQueueType()));
        request.setAttribute("bean", bean);

        return doForward(request, "/workflow/queues/createQueue.jsp");
    }

    public ActionForward oneMoreUserInQueueInEdition(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        final WorkflowUserGroupQueueBean bean = getRenderedObject("queue");
        addUserInBean(request, bean);

        return doForward(request, "/workflow/queues/editQueue.jsp");
    }

    private void addUserInBean(final HttpServletRequest request, WorkflowUserGroupQueueBean bean) {
        final User userToAdd = bean.getUserToAdd();
        if (userToAdd != null) {
            bean.addUser(userToAdd);
        }

        bean.setUserToAdd(null);
        request.setAttribute("bean", bean);
        request.setAttribute("queues", WorkflowSystem.getInstance().getWorkflowQueuesSet());

        RenderUtils.invalidateViewState("users");
    }

    public ActionForward createNewQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        final WorkflowQueueBean bean = getRenderedObject("newQueue");
        bean.createWorkflowQueue();

        RenderUtils.invalidateViewState("newQueue");
        return manageQueues(mapping, form, request, response);
    }

}
