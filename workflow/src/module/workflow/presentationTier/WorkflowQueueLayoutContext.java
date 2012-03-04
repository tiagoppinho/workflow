/*
 * @(#)WorkflowQueueLayoutContext.java
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
package module.workflow.presentationTier;

import java.util.HashMap;
import java.util.Map;

import module.workflow.domain.WorkflowQueue;
import myorg.presentationTier.LayoutContext;

/**
 * 
 * @author Paulo Abrantes
 * 
 */
public class WorkflowQueueLayoutContext extends LayoutContext {

    private static Map<Class<? extends WorkflowQueue>, String> creationPageRegister = new HashMap<Class<? extends WorkflowQueue>, String>();

    public static final String DEFAULT_CREATE = "create.jsp";
    public static final String DEFAULT_EDIT = "edit.jsp";
    public static final String DEFAULT_VIEW = "view.jsp";

    private String create;
    private String edit;
    private String view;

    public String getCreateQueue() {
	return create;
    }

    public void setCreateQueue(String create) {
	this.create = create;
    }

    public String getEditQueue() {
	return edit;
    }

    public void setEditQueue(String edit) {
	this.edit = edit;
    }

    public String getViewQueue() {
	return view;
    }

    public void setViewQueue(String view) {
	this.view = view;
    }

    public static void registerCreationPage(Class<? extends WorkflowQueue> instanceType, String page) {
	creationPageRegister.put(instanceType, page);
    }

    public static String getBootstrapFor(Class<? extends WorkflowQueue> instanceType) {
	String page = creationPageRegister.get(instanceType);
	return page == null ? "/" + instanceType.getName().replace(".", "/") + "/" + DEFAULT_CREATE : page;
    }

    public static WorkflowQueueLayoutContext getDefaultLayout(WorkflowQueue queue) {
	String folder = "/" + queue.getClass().getName().replace(".", "/") + "/";
	WorkflowQueueLayoutContext layout = new WorkflowQueueLayoutContext();
	layout.setCreateQueue(folder + DEFAULT_CREATE);
	layout.setEditQueue(folder + DEFAULT_EDIT);
	layout.setViewQueue(folder + DEFAULT_VIEW);

	return layout;
    }
}
