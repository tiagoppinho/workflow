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

import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.presentationTier.Context;
import pt.ist.bennu.core.presentationTier.actions.ContextBaseAction;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/workflowQueueManagement")
/**
 * 
 * @author Anil Kassamali
 * @author Paulo Abrantes
 * 
 */
public class QueueManagementAction extends ContextBaseAction {

    public ActionForward manageQueues(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        request.setAttribute("queues", WorkflowSystem.getInstance().getWorkflowQueues());

        return forward(request, "/workflow/queues/manageQueues.jsp");
    }

    public ActionForward viewQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        WorkflowQueue queue = getDomainObject(request, "queueId");
        request.setAttribute("queue", queue);

        return forward(request, "/workflow/queues/viewQueue.jsp");
    }

    public ActionForward prepareEditQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        WorkflowQueue queue = getDomainObject(request, "queueId");
        WorkflowQueueBean beanForType = getBeanForType(queue.getClass());
        beanForType.setQueue(queue);

        request.setAttribute("bean", beanForType);

        return forward(request, "/workflow/queues/editQueue.jsp");
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

        return forward(request, "/workflow/queues/createQueue.jsp");
    }

    public ActionForward doPostback(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        WorkflowQueueBean bean = getRenderedObject("newQueue");
        RenderUtils.invalidateViewState();

        Class<? extends WorkflowQueue> queueType = bean.getQueueType();
        request.setAttribute("creationPage", WorkflowQueueLayoutContext.getBootstrapFor(queueType));
        request.setAttribute("bean", bean);

        return forward(request, "/workflow/queues/createQueue.jsp");
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

        return forward(request, "/workflow/queues/createQueue.jsp");
    }

    private WorkflowQueueBean getBeanForType(Class<? extends WorkflowQueue> selectedType) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        Class<? extends WorkflowQueueBean> beanClass =
                (Class<? extends WorkflowQueueBean>) Class.forName(selectedType.getName() + "Bean");
        WorkflowQueueBean bean = beanClass.newInstance();
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

        return forward(request, "/workflow/queues/createQueue.jsp");
    }

    public ActionForward oneMoreUserInQueueInEdition(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        WorkflowUserGroupQueueBean bean = getRenderedObject("queue");
        addUserInBean(request, bean);

        return forward(request, "/workflow/queues/editQueue.jsp");
    }

    private void addUserInBean(final HttpServletRequest request, WorkflowUserGroupQueueBean bean) {
        User userToAdd = bean.getUserToAdd();
        if (userToAdd != null) {
            bean.addUser(userToAdd);
        }

        bean.setUserToAdd(null);
        request.setAttribute("bean", bean);
        request.setAttribute("queues", WorkflowSystem.getInstance().getWorkflowQueues());

        RenderUtils.invalidateViewState("users");
    }

    public ActionForward createNewQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        WorkflowQueueBean bean = getRenderedObject("newQueue");
        bean.createWorkflowQueue();

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
