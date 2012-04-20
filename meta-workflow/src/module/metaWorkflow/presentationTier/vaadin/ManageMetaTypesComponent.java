/*
 * @(#)ManageMetaTypesComponent.java
 *
 * Copyright 2011 Instituto Superior Tecnico
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
package module.metaWorkflow.presentationTier.vaadin;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import module.metaWorkflow.domain.MetaProcessState;
import module.metaWorkflow.domain.WorkflowMetaType;
import module.organization.domain.OrganizationalModel;
import module.vaadin.ui.BennuTheme;
import module.workflow.domain.ProcessFile;
import module.workflow.domain.WorkflowSystem;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.RoleType;
import myorg.util.BundleUtil;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.vaadinframework.annotation.EmbeddedComponent;
import pt.ist.vaadinframework.data.ItemConstructor;
import pt.ist.vaadinframework.data.reflect.DomainContainer;
import pt.ist.vaadinframework.data.reflect.DomainItem;
import pt.ist.vaadinframework.ui.EmbeddedComponentContainer;
import pt.ist.vaadinframework.ui.TransactionalTable;

import com.vaadin.data.Buffered.SourceException;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

/**
 * 
 * @author David Martinho
 * @author João Neves
 * @author João Antunes
 * 
 */
@EmbeddedComponent(path = { "metaTypeManagement" })
public class ManageMetaTypesComponent extends CustomComponent implements EmbeddedComponentContainer {
    private static final String RESOURCE_BUNDLE = "resources/MetaWorkflowResources";
    private static final long serialVersionUID = 1L;

    public ManageMetaTypesComponent() {
	home();
    }

    public void home() {
	manageMetaTypesInterface();
    }

    private void manageMetaTypesInterface() {
	VerticalLayout layout = new VerticalLayout();
	setCompositionRoot(layout);
	layout.setSpacing(true);

	Label title = new Label(getMessage("label.metaType.managament"));
	title.addStyleName(BennuTheme.LABEL_H2);
	layout.addComponent(title);

	Table table = new TransactionalTable(RESOURCE_BUNDLE) {
	    @Override
	    protected String formatPropertyValue(Object rowId, Object colId, com.vaadin.data.Property property) {
		if ("availableFileTypes".equals(colId)) {
		    StringBuilder strBuilder = new StringBuilder();
		    List<Class<? extends ProcessFile>> fileClasses = (List<Class<? extends ProcessFile>>) property.getValue();
		    Iterator<Class<? extends ProcessFile>> fileIterator = fileClasses.iterator();
		    while (fileIterator.hasNext()) {
			strBuilder.append(BundleUtil.getLocalizedNamedFroClass(fileIterator.next()));
			if (fileIterator.hasNext()) {
			    strBuilder.append(", ");
			}
		    }
		    return strBuilder.toString();
		    
		}
		return super.formatPropertyValue(rowId, colId, property);
	    };
	};
	DomainContainer<WorkflowMetaType> container = new DomainContainer<WorkflowMetaType>(WorkflowSystem.getInstance()
		.getMetaTypes(), WorkflowMetaType.class);
	container.setContainerProperties("name", "organizationalModel.name", "availableFileTypes");
	table.setContainerDataSource(container);
	table.addGeneratedColumn("", new ColumnGenerator() {
	    @Override
	    public Object generateCell(Table source, final Object itemId, Object columnId) {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);

		Button buttonFields = new Button(getMessage("link.metaType.manageStates"), new Button.ClickListener() {
		    @Override
		    public void buttonClick(ClickEvent event) {
			manageStatesInterface((WorkflowMetaType) itemId);
		    }
		});
		buttonFields.addStyleName(BaseTheme.BUTTON_LINK);
		horizontalLayout.addComponent(buttonFields);
		return horizontalLayout;
	    }
	});

	layout.addComponent(table);
    }

    public class WorkflowMetaTypeMaker implements ItemConstructor<Object> {
	private static final long serialVersionUID = 1L;

	@Override
	public Object[] getOrderedArguments() {
	    return new Object[] { "name", "firstDescription", "organizationalModel" };
	}

	public WorkflowMetaType construct(String name, String description, OrganizationalModel model) throws SourceException {
	    return new WorkflowMetaType(name, description, model);
	}
    }

    private void manageStatesInterface(WorkflowMetaType metaType) {
	VerticalLayout header = new VerticalLayout();
	setCompositionRoot(header);
	header.setSpacing(true);

	Label statesTitle = new Label(getMessage("label.metaType.manageStates"));
	statesTitle.addStyleName(BennuTheme.LABEL_H2);
	header.addComponent(statesTitle);

	Button buttonBack = new Button(getMessage("link.back"), new Button.ClickListener() {
	    @Override
	    public void buttonClick(ClickEvent event) {
		manageMetaTypesInterface();
	    }
	});
	buttonBack.addStyleName(BaseTheme.BUTTON_LINK);
	header.addComponent(buttonBack);

	Label metaTypeTitle = new Label(getMessage("label.metaType") + ": " + metaType.getName());
	metaTypeTitle.addStyleName(BennuTheme.LABEL_H3);
	header.addComponent(metaTypeTitle);

	HorizontalLayout content = new HorizontalLayout();
	content.setSpacing(true);
	header.addComponent(content);
	
	DomainItem<MetaProcessState> selectedItem = new DomainItem<MetaProcessState>(MetaProcessState.class);

	Table stateTable = new TransactionalTable(RESOURCE_BUNDLE);

	DomainItem<WorkflowMetaType> metaTypeDI = new DomainItem<WorkflowMetaType>(metaType);
	final DomainContainer<MetaProcessState> states = (DomainContainer<MetaProcessState>) metaTypeDI
		.getItemProperty("processStates");
	states.setContainerProperties("name.content");
	stateTable.setContainerDataSource(states);
	stateTable.setSelectable(true);
	stateTable.setImmediate(true);
	stateTable.setPropertyDataSource(selectedItem);
	content.addComponent(stateTable);

	VerticalLayout stateInfo = new VerticalLayout();
	Label stateName = new Label();
	stateName.setPropertyDataSource(selectedItem);
	stateName.setCaption("Name:");
	stateInfo.addComponent(stateName);
	content.addComponent(stateInfo);

	Button buttonAdd = new Button(getMessage("link.processState.add"), new Button.ClickListener() {
	    @Override
	    public void buttonClick(ClickEvent event) {
		addProcessState(states);
	    }
	});
	buttonAdd.addStyleName(BaseTheme.BUTTON_LINK);
	header.addComponent(buttonAdd);
    }

    private static final String NEW_STATE_NAME = "new.state.name";

    @Service
    private void addProcessState(DomainContainer<MetaProcessState> states) {
	states.addItem(new MetaProcessState(getMessage(NEW_STATE_NAME), 1));
    }

    private String getMessage(String message) {
	return BundleUtil.getFormattedStringFromResourceBundle(RESOURCE_BUNDLE, message);
    }

    @Override
    public boolean isAllowedToOpen(Map<String, String> arguments) {
	return UserView.getCurrentUser().hasRoleType(RoleType.MANAGER);
    }

    @Override
    public void setArguments(Map<String, String> arg0) {
    }
}
