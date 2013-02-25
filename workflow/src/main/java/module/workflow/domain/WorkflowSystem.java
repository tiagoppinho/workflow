/*
 * @(#)WorkflowSystem.java
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
package module.workflow.domain;

import javax.servlet.http.HttpServletRequest;

import module.dashBoard.WidgetRegister;
import module.workflow.widgets.ProcessListWidget;
import module.workflow.widgets.QuickViewWidget;
import module.workflow.widgets.UnreadCommentsWidget;
import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter.ChecksumPredicate;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

/**
 * 
 * @author João Neves
 * @author João Antunes
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class WorkflowSystem extends WorkflowSystem_Base {

    public static class VirtualHostMyOrgRelationListener extends RelationAdapter<VirtualHost, MyOrg> {

        @Override
        public void beforeRemove(VirtualHost vh, MyOrg myorg) {
            vh.removeWorkflowSystem();
            super.beforeRemove(vh, myorg);
        }
    }

    static {
        VirtualHost.MyOrgVirtualHost.addListener(new VirtualHostMyOrgRelationListener());

        WidgetRegister.registerWidget(ProcessListWidget.class);
        WidgetRegister.registerWidget(QuickViewWidget.class);
        WidgetRegister.registerWidget(UnreadCommentsWidget.class);

        RequestChecksumFilter.registerFilterRule(new ChecksumPredicate() {
            @Override
            public boolean shouldFilter(HttpServletRequest httpServletRequest) {
                return !(httpServletRequest.getRequestURI().endsWith("/workflowProcessManagement.do")
                        && httpServletRequest.getQueryString() != null && httpServletRequest.getQueryString().contains(
                        "method=viewTypeDescription"));
            }
        });
    }

    private WorkflowSystem(final VirtualHost virtualHost) {
        super();
        virtualHost.setWorkflowSystem(this);
    }

    public static WorkflowSystem getInstance() {
        final VirtualHost virtualHostForThread = VirtualHost.getVirtualHostForThread();
        return virtualHostForThread == null ? null : virtualHostForThread.getWorkflowSystem();
    }

    @Atomic
    public static void createSystem(final VirtualHost virtualHost) {
        if (!virtualHost.hasWorkflowSystem() || virtualHost.getWorkflowSystem().getVirtualHostCount() > 1) {
            new WorkflowSystem(virtualHost);
        }
    }

    @Atomic
    public void setForVirtualHost(final VirtualHost virtualHost) {
        virtualHost.setWorkflowSystem(this);
    }
}
