/*
 * @(#)MetaWorkflowInitializer.java
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
package module.metaWorkflow.domain;

import javax.servlet.http.HttpServletRequest;

import module.metaWorkflow.presentationTier.actions.OrganizationModelPluginAction.QueueView;
import module.organization.presentationTier.actions.OrganizationModelAction;
import module.workflow.presentationTier.actions.ProcessManagement;
import module.workflow.presentationTier.actions.ProcessManagement.ProcessRequestHandler;
import pt.ist.bennu.core.domain.ModuleInitializer;
import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.util.VariantBean;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author João Neves
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class MetaWorkflowInitializer extends MetaWorkflowInitializer_Base implements ModuleInitializer {

    private static boolean isInitialized = false;

    private static ThreadLocal<MetaWorkflowInitializer> init = null;

    private MetaWorkflowInitializer() {
        setMyOrg(MyOrg.getInstance());
    }

    public static MetaWorkflowInitializer getInstance() {
        if (init != null) {
            return init.get();
        }

        if (!isInitialized) {
            initialize();
        }
        final MyOrg myOrg = MyOrg.getInstance();
        return myOrg.getMetaWorkflowInitializer();
    }

    @Atomic
    public synchronized static void initialize() {
        if (!isInitialized) {
            try {
                final MyOrg myOrg = MyOrg.getInstance();
                final MetaWorkflowInitializer initializer = myOrg.getMetaWorkflowInitializer();
                if (initializer == null) {
                    new MetaWorkflowInitializer();
                }
                init = new ThreadLocal<MetaWorkflowInitializer>();
                init.set(myOrg.getMetaWorkflowInitializer());

                isInitialized = true;
            } finally {
                init = null;
            }
        }
    }

    @Override
    public void init(MyOrg root) {

        ProcessManagement.registerProcessRequestHandler(WorkflowMetaProcess.class,
                new ProcessRequestHandler<WorkflowMetaProcess>() {

                    @Override
                    public void handleRequest(WorkflowMetaProcess process, HttpServletRequest request) {
                        request.setAttribute("commentBean", new VariantBean());

                    }
                });

        OrganizationModelAction.partyViewHookManager.register(new QueueView());

    }

}
