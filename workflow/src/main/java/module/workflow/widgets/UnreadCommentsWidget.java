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
import module.dashBoard.widgets.WidgetController;
import module.organization.domain.Person;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.utils.WorkflowCommentCounter;
import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.bennu.core.util.ClassNameBundle;

/**
 * 
 * @author João Antunes
 * 
 */
@ClassNameBundle(bundle = "resources/WorkflowResources", key = "widget.title.processesWithUnreadComments")
public class UnreadCommentsWidget extends WidgetController {
    public static final Comparator<WorkflowProcess> CLASS_NAME_COMPARATOR = new Comparator<WorkflowProcess>() {

	@Override
	public int compare(WorkflowProcess o1, WorkflowProcess o2) {
	    final Class<? extends WorkflowProcess> c1 = o1.getClass();
	    final Class<? extends WorkflowProcess> c2 = o2.getClass();

	    final String s1 = BundleUtil.getLocalizedNamedFroClass(c1);
	    final String s2 = BundleUtil.getLocalizedNamedFroClass(c2);

	    return s1.compareTo(s2);
	}

    };

    private static final Set<WorkflowCommentCounter> processClassesToCount = new HashSet<WorkflowCommentCounter>();

    public static void register(WorkflowCommentCounter commentCounter) {
	processClassesToCount.add(commentCounter);
    }

    //    @Override
    //    public boolean isOptionsModeSupported() {
    //	return true;
    //    }

    //    @Override
    //    @Service
    //    protected ExpenditureWidgetOptions getOrCreateOptions(DashBoardWidget widget) {
    //	ExpenditureWidgetOptions options = (ExpenditureWidgetOptions) widget.getOptions();
    //	if (options == null) {
    //	    options = new ExpenditureWidgetOptions(10);
    //	    widget.setOptions(options);
    //	}
    //	return options;
    //    }

    @Override
    public void doView(WidgetRequest request) {
	DashBoardWidget widget = request.getWidget();
	//	ExpenditureWidgetOptions options = getOrCreateOptions(widget);
	Person loggedPerson = UserView.getCurrentUser().getPerson();
	Map<Class, Integer> numberUnreadCommentsPerProcess = new HashMap<Class, Integer>();

	List<WorkflowProcess> processesWithUnreadComments = getProcessesWithUnreadComments(numberUnreadCommentsPerProcess,
		loggedPerson, null);

	int processCount = processesWithUnreadComments.size();
	processesWithUnreadComments = processesWithUnreadComments.subList(0, Math.min(20, processCount));

	//	request.setAttribute("widgetOptions-" + widget.getExternalId(), options);
	request.setAttribute("numberUnreadCommentsPerProcess", numberUnreadCommentsPerProcess);
	//	request.setAttribute("processesWithUnreadComments", processesWithUnreadComments);
    }

    /**
     * 
     * @param numberUnreadCommentsPerProcess
     *            a Map<String,Integer> to be filled with a list of the number
     *            of unread comments per kind of workflowprocess
     * @return a List of WorkflowProcess {@link WorkflowProcess} which have
     *         unread comments
     */
    public static List<WorkflowProcess> getProcessesWithUnreadComments(Map<Class, Integer> numberUnreadCommentsPerProcess,
	    Person forGivenPerson, String className) {

	List<WorkflowProcess> processesWithUnreadComments = new ArrayList<WorkflowProcess>();
	for (WorkflowCommentCounter commentCounter : processClassesToCount) {
	    Set<WorkflowProcess> processesToAdd = commentCounter.getProcessesWithUnreadComments(forGivenPerson, className);
	    processesWithUnreadComments.addAll(processesToAdd);
	    if (numberUnreadCommentsPerProcess != null)
		numberUnreadCommentsPerProcess.put(commentCounter.getClassToFilter(), processesToAdd.size());
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
	return BundleUtil.getStringFromResourceBundle("resources/WorkflowResources", "widget.description.UnreadCommentsWidget");
    }
}
