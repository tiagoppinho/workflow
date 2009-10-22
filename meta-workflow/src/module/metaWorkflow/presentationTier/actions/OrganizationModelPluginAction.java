package module.metaWorkflow.presentationTier.actions;

import javax.servlet.http.HttpServletRequest;

import module.organization.domain.OrganizationalModel;
import module.organization.domain.Party;
import module.organization.presentationTier.actions.PartyViewHook;
import myorg.presentationTier.actions.ContextBaseAction;
import myorg.util.BundleUtil;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/metaWorkflowOrganizationModel")
public class OrganizationModelPluginAction extends ContextBaseAction {

    public static class QueueView extends PartyViewHook {

	@Override
	public String hook(final HttpServletRequest request, final OrganizationalModel organizationalModel, final Party party) {
	    return "/module/metaWorkflow/organizationModelQueueView.jsp";
	}

	@Override
	public String getViewName() {
	    return "03_queueView";
	}

	@Override
	public String getPresentationName() {
	    return BundleUtil.getStringFromResourceBundle("resources.MetaWorkflowResources", "label.queueView");
	}

	@Override
	public boolean isAvailableFor(final Party party) {
	    return party != null && party.isUnit();
	}
    }

}
