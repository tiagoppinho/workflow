/*
 * @(#)OrganizationModelPluginAction.java
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

import javax.servlet.http.HttpServletRequest;

import module.organization.domain.OrganizationalModel;
import module.organization.domain.Party;
import module.organization.presentationTier.actions.PartyViewHook;
import myorg.presentationTier.actions.ContextBaseAction;
import myorg.util.BundleUtil;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/metaWorkflowOrganizationModel")
/**
 * 
 * @author Luis Cruz
 * 
 */
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
