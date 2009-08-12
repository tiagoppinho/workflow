package module.metaWorkflow.presentationTier.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.metaWorkflow.domain.WorkflowMetaType;
import module.metaWorkflow.domain.WorkflowMetaTypeDescription;
import module.metaWorkflow.util.WorkflowMetaTypeBean;
import module.workflow.domain.WorkflowSystem;
import myorg.domain.MyOrg;
import myorg.presentationTier.actions.ContextBaseAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/metaTypeManagement")
public class MetaTypeManagement extends ContextBaseAction {

    public ActionForward manageMetaType(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	request.setAttribute("metaTypes", WorkflowSystem.getInstance().getMetaTypes());
	return forward(request, "/metaWorkflow/metaType/manageMetaTypes.jsp");
    }

    public ActionForward prepareCreateMetaType(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	request.setAttribute("bean", new WorkflowMetaTypeBean());
	return forward(request, "/metaWorkflow/metaType/createMetaType.jsp");
    }

    public ActionForward viewMetaType(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	WorkflowMetaType type = getDomainObject(request, "metaTypeId");
	request.setAttribute("metaType", type);

	return forward(request, "/metaWorkflow/metaType/viewMetaType.jsp");
    }

    public ActionForward createNewMetaType(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	WorkflowMetaTypeBean bean = getRenderedObject("newMetaType");
	WorkflowMetaType.createNewMetaType(bean.getName(), bean.getDescription(), bean.getOrganizationModel(), bean
		.getFileClassNames());

	RenderUtils.invalidateViewState("newMetaType");
	return manageMetaType(mapping, form, request, response);
    }

    public ActionForward editMetaType(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	WorkflowMetaType type = getDomainObject(request, "metaTypeId");
	request.setAttribute("metaType", type);
	return forward(request, "/metaWorkflow/metaType/editMetaType.jsp");
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

	return viewMetaType(mapping, form, request, response);
    }

    public ActionForward viewVersion(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	WorkflowMetaType metaType = getDomainObject(request, "metaTypeId");
	Integer version = Integer.valueOf(request.getParameter("version"));
	request.setAttribute("historyVersion", metaType.getDescriptionAtVersion(version));

	return viewMetaType(mapping, form, request, response);
    }

}
