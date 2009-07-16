package module.metaWorkflow.presentationTier.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.metaWorkflow.domain.WorkflowMetaProcess;
import module.metaWorkflow.domain.WorkflowMetaType;
import module.metaWorkflow.domain.WorkflowMetaTypeDescription;
import module.metaWorkflow.domain.WorkflowQueue;
import module.metaWorkflow.util.WorkflowMetaProcessBean;
import module.metaWorkflow.util.WorkflowMetaTypeBean;
import module.metaWorkflow.util.WorkflowQueueBean;
import module.workflow.domain.WorkflowProcess;
import module.workflow.presentationTier.actions.ProcessManagement;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.MyOrg;
import myorg.presentationTier.actions.ContextBaseAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/metaWorkflow")
public class MetaWorkflowAction extends ContextBaseAction {

    public ActionForward metaProcessHome(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	request.setAttribute("processes", WorkflowProcess.getAllProcesses(WorkflowMetaProcess.class));
	return forward(request, "/metaWorkflow/viewMetaProcesses.jsp");
    }

    public ActionForward prepareCreateProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	WorkflowMetaProcessBean processBean = new WorkflowMetaProcessBean();
	request.setAttribute("workflowBean", processBean);

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
	WorkflowMetaProcess process = WorkflowMetaProcess.createNewProcess(processBean.getMetaType(), processBean.getSubject(),
		processBean.getInstanceDescription(), processBean.getQueue(), processBean.getRequestor());

	return viewMetaProcess(process);
    }

    public ActionForward manageMetaType(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	request.setAttribute("bean", new WorkflowMetaTypeBean());
	request.setAttribute("metaTypes", MyOrg.getInstance().getMetaTypes());

	return forward(request, "/metaWorkflow/manageMetaTypes.jsp");
    }

    public ActionForward manageQueues(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	request.setAttribute("bean", new WorkflowQueueBean());
	request.setAttribute("queues", MyOrg.getInstance().getMetaWorkflowQueues());

	return forward(request, "/metaWorkflow/manageQueues.jsp");
    }

    public ActionForward createNewQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	WorkflowQueueBean bean = getRenderedObject("newQueue");
	WorkflowQueue.createQueue(bean.getName(), bean.getMetaType());

	RenderUtils.invalidateViewState("newQueue");
	return manageQueues(mapping, form, request, response);
    }

    public ActionForward createNewMetaType(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	WorkflowMetaTypeBean bean = getRenderedObject("newMetaType");
	WorkflowMetaType.createNewMetaType(bean.getName(), bean.getDescription(), bean.getFileClassNames());

	RenderUtils.invalidateViewState("newMetaType");
	return manageMetaType(mapping, form, request, response);
    }

    public ActionForward editMetaTypeDescription(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	WorkflowMetaType type = getDomainObject(request, "metaTypeId");
	request.setAttribute("metaType", type);
	return forward(request, "/metaWorkflow/editMetaTypeDescription.jsp");
    }

    public ActionForward viewMetaTypeDescriptionHistory(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	WorkflowMetaType type = getDomainObject(request, "metaTypeId");
	request.setAttribute("metaType", type);
	return forward(request, "/metaWorkflow/viewMetaTypeDescriptionHistory.jsp");
    }

    public ActionForward doDiff(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	String revision1 = request.getParameter("rev1");
	String revision2 = request.getParameter("rev2");
	if (!StringUtils.isEmpty(revision1) && !StringUtils.isEmpty(revision2)) {
	    Integer version1 = Integer.valueOf(revision1);
	    Integer version2 = Integer.valueOf(revision2);

	    WorkflowMetaType metaType = getDomainObject(request, "metaTypeId");
	    WorkflowMetaTypeDescription descriptionV1 = metaType.getDescriptionAtVersion(version1);
	    WorkflowMetaTypeDescription descriptionV2 = metaType.getDescriptionAtVersion(version2);

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

    public ActionForward addComment(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	final WorkflowMetaProcess process = getDomainObject(request, "processId");
	String comment = getRenderedObject("comment");
	process.createComment(UserView.getCurrentUser(), comment);

	RenderUtils.invalidateViewState("comment");
	return ProcessManagement.forwardToProcess(process);
    }

}
