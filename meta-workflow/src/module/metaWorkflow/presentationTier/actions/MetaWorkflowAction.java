package module.metaWorkflow.presentationTier.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.metaWorkflow.domain.WorkflowMetaProcess;
import module.metaWorkflow.domain.WorkflowMetaType;
import module.metaWorkflow.util.WorkflowMetaProcessBean;
import module.metaWorkflow.util.WorkflowMetaTypeBean;
import module.workflow.domain.WorkflowProcess;
import module.workflow.presentationTier.actions.ProcessManagement;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.MyOrg;
import myorg.presentationTier.actions.ContextBaseAction;

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
	WorkflowMetaProcess process = WorkflowMetaProcess.createNewProcess(processBean.getMetaType(), processBean
		.getInstanceDescription());

	return viewMetaProcess(process);
    }

    public ActionForward manageMetaType(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	request.setAttribute("bean", new WorkflowMetaTypeBean());
	request.setAttribute("metaTypes", MyOrg.getInstance().getMetaTypes());

	return forward(request, "/metaWorkflow/manageMetaTypes.jsp");
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

	WorkflowMetaProcess process = getDomainObject(request, "processId");
	request.setAttribute("process", process);
	request.setAttribute("metaType", process.getMetaType());
	return forward(request, "/metaWorkflow/editMetaTypeDescription.jsp");
    }

    public ActionForward viewMetaTypeDescriptionHistory(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	WorkflowMetaProcess process = getDomainObject(request, "processId");
	request.setAttribute("process", process);
	request.setAttribute("metaType", process.getMetaType());
	return forward(request, "/metaWorkflow/viewMetaTypeDescriptionHistory.jsp");
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
