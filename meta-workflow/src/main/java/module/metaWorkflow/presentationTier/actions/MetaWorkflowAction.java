/*
 * @(#)MetaWorkflowAction.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Meta-Workflow Module.
 *
 *   The Meta-Workflow Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Meta-Workflow Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Meta-Workflow Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.metaWorkflow.presentationTier.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.metaWorkflow.domain.WorkflowMetaProcess;
import module.metaWorkflow.domain.WorkflowMetaType;
import module.metaWorkflow.domain.WorkflowMetaTypeDescription;
import module.metaWorkflow.domain.search.SearchMetaWorkflowProcess;
import module.metaWorkflow.util.WorkflowMetaProcessBean;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowQueue;
import module.workflow.presentationTier.actions.CommentBean;
import module.workflow.presentationTier.actions.ProcessManagement;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.presentationTier.actions.ContextBaseAction;
import pt.ist.bennu.core.util.VariantBean;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.plugins.luceneIndexing.DomainIndexer;

@Mapping(path = "/metaWorkflow")
/**
 * 
 * @author João Neves
 * @author Anil Kassamali
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class MetaWorkflowAction extends ContextBaseAction {

    private ActionForward viewMetaProcessList(final HttpServletRequest request) {
        request.setAttribute("searchBean", new VariantBean());
        final User currentUser = UserView.getCurrentUser();
        request.setAttribute("user", currentUser);
        List<WorkflowQueue> sortedQueues = new ArrayList<WorkflowQueue>(WorkflowMetaType.getAllQueuesForUser(currentUser));
        Collections.sort(sortedQueues, WorkflowQueue.COMPARATOR_BY_NAME);
        request.setAttribute("availableQueues", sortedQueues);

        return forward(request, "/metaWorkflow/viewMetaProcesses.jsp");
    }

    public ActionForward viewOpenProcessesInMyQueues(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        final User currentUser = UserView.getCurrentUser();
        request.setAttribute("openProcesses", WorkflowProcess.getAllProcesses(WorkflowMetaProcess.class, new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                WorkflowMetaProcess workflowMetaProcess = (WorkflowMetaProcess) arg0;
                if (!workflowMetaProcess.isOpen()) {
                    return false;
                }
                for (WorkflowQueue queue : workflowMetaProcess.getCurrentQueues()) {
                    if (queue.isUserAbleToAccessQueue(currentUser)) {
                        return true;
                    }
                }
                return false;
            }
        }));

        return viewMetaProcessList(request);

    }

    public ActionForward viewAllOpenProcesses(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        final User currentUser = UserView.getCurrentUser();
        request.setAttribute("displayProcesses", WorkflowProcess.getAllProcesses(WorkflowMetaProcess.class, new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                WorkflowMetaProcess workflowMetaProcess = (WorkflowMetaProcess) arg0;
                return workflowMetaProcess.isAccessible(currentUser) && workflowMetaProcess.isOpen();
            }
        }));

        return viewMetaProcessList(request);

    }

    public ActionForward viewOwnProcesses(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        final User currentUser = UserView.getCurrentUser();
        request.setAttribute("myProcess", currentUser.getMetaProcesses());

        return viewMetaProcessList(request);

    }

    public ActionForward viewProcessInQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        WorkflowQueue queue = getDomainObject(request, "queueId");
        final String active = request.getParameter("active");
        final Boolean state = active != null ? Boolean.valueOf(active) : null;

        request.setAttribute("queue", queue);
        request.setAttribute("displayProcesses", CollectionUtils.select(queue.getProcesses(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return state == null || state == ((WorkflowMetaProcess) arg0).isOpen();
            }

        }));
        return forward(request, "/metaWorkflow/viewProcessesInQueue.jsp");
    }

    public ActionForward search(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        String searchQuery = getRenderedObject("searchQuery");
        if (searchQuery != null) {
            List<WorkflowMetaProcess> searchResults = DomainIndexer.getInstance().search(WorkflowMetaProcess.class, searchQuery);
            request.setAttribute("searchResult", searchResults);
        }
        request.setAttribute("searchBean", new VariantBean());
        return forward(request, "/metaWorkflow/search.jsp");

    }

    public ActionForward advancedSearch(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        SearchMetaWorkflowProcess searchBean = getRenderedObject("advancedSearch");
        if (searchBean == null) {
            searchBean = new SearchMetaWorkflowProcess();
        } else {
            request.setAttribute("searchResult", searchBean.search());
        }
        RenderUtils.invalidateViewState("advancedSearch");
        request.setAttribute("advancedSearchBean", searchBean);

        return forward(request, "/metaWorkflow/search.jsp");

    }

    public ActionForward prepareCreateProcess(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        WorkflowMetaProcessBean processBean = getRenderedObject("processBean");
        if (processBean == null) {
            processBean = new WorkflowMetaProcessBean();
        }

        request.setAttribute("workflowBean", processBean);

        RenderUtils.invalidateViewState("processBean");
        return forward(request, "/metaWorkflow/createMetaProcess.jsp");
    }

    public ActionForward viewMetaProcess(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        WorkflowMetaProcess process = getDomainObject(request, "processId");
        return viewMetaProcess(process);
    }

    private ActionForward viewMetaProcess(WorkflowMetaProcess process) {

        return ProcessManagement.forwardToProcess(process);
    }

    public ActionForward createMetaProcess(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        WorkflowMetaProcessBean processBean = getRenderedObject("processBean");
        WorkflowMetaProcess process =
                WorkflowMetaProcess.createNewProcess(processBean.getSubject(), processBean.getInstanceDescription(),
                        processBean.getQueue(), processBean.getRequestor());

        return viewMetaProcess(process);
    }

    public ActionForward viewMetaTypeDescriptionHistory(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        WorkflowMetaProcess process = getDomainObject(request, "processId");
        request.setAttribute("process", process);
        request.setAttribute("metaType", process.getMetaType());

        return forward(request, "/metaWorkflow/viewMetaTypeVersionInProcess.jsp");
    }

    public ActionForward doDiff(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        String revision1 = request.getParameter("rev1");
        String revision2 = request.getParameter("rev2");
        if (!StringUtils.isEmpty(revision1) && !StringUtils.isEmpty(revision2)) {
            Integer version1 = Integer.valueOf(revision1);
            Integer version2 = Integer.valueOf(revision2);

            WorkflowMetaType metaType = getDomainObject(request, "metaTypeId");
            WorkflowMetaTypeDescription descriptionV1 = metaType.getDescriptionAtVersionOld(version1);
            WorkflowMetaTypeDescription descriptionV2 = metaType.getDescriptionAtVersionOld(version2);

            request.setAttribute("version1", descriptionV1);
            request.setAttribute("version2", descriptionV2);
            request.setAttribute("diff", descriptionV1.getDiffWith(descriptionV2));

        }

        return viewMetaTypeDescriptionHistory(mapping, form, request, response);
    }

    public ActionForward viewVersion(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        WorkflowMetaType metaType = getDomainObject(request, "metaTypeId");
        Integer version = Integer.valueOf(request.getParameter("version"));
        request.setAttribute("historyVersion", metaType.getDescriptionAtVersion(version));

        return viewMetaTypeDescriptionHistory(mapping, form, request, response);
    }

    public ActionForward manageMetaTypeObservers(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        WorkflowMetaType metaType = getDomainObject(request, "metaTypeId");
        request.setAttribute("metaType", metaType);
        request.setAttribute("bean", new VariantBean());

        return forward(request, "/metaWorkflow/manageMetaTypeObservers.jsp");
    }

    public ActionForward addMetaTypeObserver(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        WorkflowMetaType metaType = getDomainObject(request, "metaTypeId");
        User user = getRenderedObject("userToAdd");

        if (user != null) {
            metaType.addObserver(user);
        }

        RenderUtils.invalidateViewState("userToAdd");

        return manageMetaTypeObservers(mapping, form, request, response);
    }

    public ActionForward removeMetaTypeObserver(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        WorkflowMetaType metaType = getDomainObject(request, "metaTypeId");
        User user = getDomainObject(request, "userId");

        metaType.removeObserver(user);

        return manageMetaTypeObservers(mapping, form, request, response);
    }

    public ActionForward addComment(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        final WorkflowMetaProcess process = getDomainObject(request, "processId");
        final String comment = getRenderedObject("comment");
        // TODO : Add people to notify when creating comment.
        final CommentBean commentBean = new CommentBean(process);
        commentBean.setComment(comment);
        process.createComment(UserView.getCurrentUser(), commentBean);

        RenderUtils.invalidateViewState("comment");
        return ProcessManagement.forwardToProcess(process);
    }

}
