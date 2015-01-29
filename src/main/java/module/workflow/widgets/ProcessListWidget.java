/*
 * @(#)ProcessListWidget.java
 *
 * Copyright 2010 Instituto Superior Tecnico
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
package module.workflow.widgets;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import module.dashBoard.presentationTier.WidgetRequest;
import module.dashBoard.widgets.DashboardWidget;
import module.dashBoard.widgets.WidgetController;
import module.workflow.domain.ProcessCounter;

import org.fenixedu.bennu.core.i18n.BundleUtil;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
@DashboardWidget(nameBundle = "resources.WorkflowResources", nameKey = "widget.title.processListWidget")
public class ProcessListWidget extends WidgetController {

    private static final Set<ProcessCounter> processCounters = new HashSet<ProcessCounter>();

    @Override
    public void doView(WidgetRequest request) {
        final Set<ProcessCounter> processCounters = new TreeSet<ProcessCounter>(ProcessCounter.COMPARATOR);
        processCounters.addAll(ProcessListWidget.processCounters);
        request.setAttribute("pendingProcessList", processCounters);
    }

    @Override
    public String getWidgetDescription() {
        return BundleUtil.getString("resources/WorkflowResources", "widget.description.processListWidget");
    }

    @Override
    public boolean isHelpModeSupported() {
        return true;
    }

    @Override
    public String getHelp() {
        return BundleUtil.getString("resources/WorkflowResources", "widget.help.processListWidget");
    }

    public static void register(final ProcessCounter processCounter) {
        processCounters.add(processCounter);
    }

}
