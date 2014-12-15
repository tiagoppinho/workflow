/*
 * @(#)UnreadCommentsWidget.java
 *
 * Copyright 2011 Instituto Superior Tecnico
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import module.dashBoard.domain.DashBoardWidget;
import module.dashBoard.presentationTier.WidgetRequest;
import module.dashBoard.widgets.DashboardWidget;
import module.dashBoard.widgets.WidgetController;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.utils.WorkflowCommentCounter;
import module.workflow.util.WorkflowClassUtil;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;

/**
 * 
 * @author João Antunes
 * 
 */
@DashboardWidget(nameBundle = "resources.WorkflowResources", nameKey = "widget.title.processesWithUnreadComments",
        defaultColumn = 0)
public class UnreadCommentsWidget extends WidgetController {

    public static final Comparator<WorkflowProcess> CLASS_NAME_COMPARATOR = (o1, o2) -> {
        final Class<? extends WorkflowProcess> c1 = o1.getClass();
        final Class<? extends WorkflowProcess> c2 = o2.getClass();

        final String s1 = WorkflowClassUtil.getNameForType(c1);
        final String s2 = WorkflowClassUtil.getNameForType(c2);

        return s1.compareTo(s2);
    };

    private static final Set<WorkflowCommentCounter> processClassesToCount = new HashSet<WorkflowCommentCounter>();

    public static void register(WorkflowCommentCounter commentCounter) {
        processClassesToCount.add(commentCounter);
    }

    @Override
    public void doView(WidgetRequest request) {
        DashBoardWidget widget = request.getWidget();
        //	ExpenditureWidgetOptions options = getOrCreateOptions(widget);
        Map<Class, Integer> numberUnreadCommentsPerProcess = new HashMap<Class, Integer>();

        List<WorkflowProcess> processesWithUnreadComments =
                getProcessesWithUnreadComments(numberUnreadCommentsPerProcess, Authenticate.getUser(), null);

        int processCount = processesWithUnreadComments.size();
        processesWithUnreadComments = processesWithUnreadComments.subList(0, Math.min(20, processCount));

        //	request.setAttribute("widgetOptions-" + widget.getExternalId(), options);
        request.setAttribute("numberUnreadCommentsPerProcess", numberUnreadCommentsPerProcess);
        //	request.setAttribute("processesWithUnreadComments", processesWithUnreadComments);
    }

    public static List<WorkflowProcess> getProcessesWithUnreadComments(Map<Class, Integer> numberUnreadCommentsPerProcess,
            User forGivenUser, String className) {

        List<WorkflowProcess> processesWithUnreadComments = new ArrayList<WorkflowProcess>();
        for (WorkflowCommentCounter commentCounter : processClassesToCount) {
            Set<WorkflowProcess> processesToAdd = commentCounter.getProcessesWithUnreadComments(forGivenUser, className);
            processesWithUnreadComments.addAll(processesToAdd);
            if (numberUnreadCommentsPerProcess != null) {
                numberUnreadCommentsPerProcess.put(commentCounter.getClassToFilter(), processesToAdd.size());
            }
        }
        Collections.sort(processesWithUnreadComments, CLASS_NAME_COMPARATOR);
        return processesWithUnreadComments;
    }

    @Override
    public void doEditOptions(WidgetRequest request) {
        DashBoardWidget widget = request.getWidget();
        request.setAttribute("edit-widgetOptions-" + widget.getExternalId(), getOrCreateOptions(widget));
    }

    public static Set<WorkflowCommentCounter> getWorkflowCommentCounters() {
        return processClassesToCount;
    }

    @Override
    public String getWidgetDescription() {
        return BundleUtil.getString("resources/WorkflowResources", "widget.description.UnreadCommentsWidget");
    }

}
