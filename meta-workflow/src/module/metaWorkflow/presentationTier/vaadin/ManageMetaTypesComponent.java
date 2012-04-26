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

import module.metaWorkflow.domain.WorkflowMetaType;
import module.vaadin.ui.BennuTheme;
import module.workflow.domain.ProcessFile;
import module.workflow.domain.WorkflowSystem;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.RoleType;
import myorg.util.BundleUtil;
import pt.ist.vaadinframework.EmbeddedApplication;
import pt.ist.vaadinframework.annotation.EmbeddedComponent;
import pt.ist.vaadinframework.data.reflect.DomainContainer;
import pt.ist.vaadinframework.ui.EmbeddedComponentContainer;
import pt.ist.vaadinframework.ui.TransactionalTable;

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
	VerticalLayout layout = new VerticalLayout();
	setCompositionRoot(layout);
	layout.setSpacing(true);

	Label title = new Label(getMessage("label.metaType.managament"));
	title.addStyleName(BennuTheme.LABEL_H2);
	layout.addComponent(title);

	Table table = new TransactionalTable(RESOURCE_BUNDLE) {
	    private static final long serialVersionUID = 1L;

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
	    private static final long serialVersionUID = 1L;

	    @Override
	    public Object generateCell(Table source, final Object itemId, Object columnId) {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);

		Button statesButton = new Button(getMessage("link.metaType.manageStates"), new Button.ClickListener() {
		    private static final long serialVersionUID = 1L;

		    @Override
		    public void buttonClick(ClickEvent event) {
			EmbeddedApplication.open(getApplication(), ManageMetaProcessStatesComponent.class,
				((WorkflowMetaType) itemId).getExternalId());
		    }
		});
		statesButton.addStyleName(BaseTheme.BUTTON_LINK);
		horizontalLayout.addComponent(statesButton);
		return horizontalLayout;
	    }
	});

	layout.addComponent(table);
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
