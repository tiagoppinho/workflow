package module.metaWorkflow.presentationTier.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.metaWorkflow.domain.WorkflowMetaProcess;
import module.metaWorkflow.domain.WorkflowMetaType;
import module.metaWorkflow.domain.WorkflowMetaTypeDescription;
import module.metaWorkflow.domain.WorkflowQueue;
import module.metaWorkflow.util.WorkflowMetaProcessBean;
import module.metaWorkflow.util.WorkflowMetaTypeBean;
import module.workflow.domain.WorkflowProcess;
import module.workflow.presentationTier.actions.ProcessManagement;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.MyOrg;
import myorg.domain.User;
import myorg.presentationTier.actions.ContextBaseAction;
import myorg.util.VariantBean;
import myorg.util.lucene.DomainIndexer;

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/metaWorkflow")
public class MetaWorkflowAction extends ContextBaseAction {

    private ActionForward viewMetaProcessList(final HttpServletRequest request) {
	request.setAttribute("searchBean", new VariantBean());
	final User currentUser = UserView.getCurrentUser();
	request.setAttribute("user", currentUser);

	return forward(request, "/metaWorkflow/viewMetaProcesses.jsp");
    }

    public ActionForward metaProcessHome(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	final User currentUser = UserView.getCurrentUser();
	request.setAttribute("processes", WorkflowProcess.getAllProcesses(WorkflowMetaProcess.class, new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		WorkflowMetaProcess workflowMetaProcess = (WorkflowMetaProcess) arg0;
		return workflowMetaProcess.isAccessible(currentUser) && workflowMetaProcess.isOpen();
	    }
	}));

	return viewMetaProcessList(request);

    }

    public ActionForward viewProcessInQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	WorkflowQueue queue = getDomainObject(request, "queueId");
	request.setAttribute("processes", queue.getActiveMetaProcesses());
	return viewMetaProcessList(request);
    }

    public ActionForward search(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	String searchQuery = getRenderedObject("searchQuery");
	List<WorkflowMetaProcess> searchResults = DomainIndexer.getInstance().search(WorkflowMetaProcess.class, searchQuery);

	request.setAttribute("searchResult", searchResults);

	return metaProcessHome(mapping, form, request, response);

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
	String comment = getRenderedObject("comment");
	process.createComment(UserView.getCurrentUser(), comment);

	RenderUtils.invalidateViewState("comment");
	return ProcessManagement.forwardToProcess(process);
    }

}
