package module.workflow.presentationTier.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.workflow.domain.WorkflowSystem;
import myorg.domain.VirtualHost;
import myorg.presentationTier.actions.ContextBaseAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/workflowConfiguration")
public class WorkflowConfigurationAction extends ContextBaseAction {

    public ActionForward viewConfiguration(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	return forward(request, "/workflowConfiguration.jsp");
    }

    public ActionForward createNewSystem(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
	WorkflowSystem.createSystem(virtualHost);

	return viewConfiguration(mapping, form, request, response);
    }

    public ActionForward useSystem(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	final WorkflowSystem workflowSystem = getDomainObject(request, "systemId");
	final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
	workflowSystem.setForVirtualHost(virtualHost);

	return viewConfiguration(mapping, form, request, response);
    }

}
