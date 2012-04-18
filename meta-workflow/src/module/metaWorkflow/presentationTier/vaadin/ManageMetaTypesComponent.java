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

import java.util.Arrays;
import java.util.Map;

import module.metaWorkflow.domain.WorkflowMetaType;
import module.organization.domain.OrganizationalModel;
import module.vaadin.ui.BennuTheme;
import module.workflow.domain.WorkflowSystem;
import myorg.domain.MyOrg;
import myorg.util.BundleUtil;
import pt.ist.vaadinframework.annotation.EmbeddedComponent;
import pt.ist.vaadinframework.data.ItemConstructor;
import pt.ist.vaadinframework.data.reflect.DomainContainer;
import pt.ist.vaadinframework.data.reflect.DomainItem;
import pt.ist.vaadinframework.ui.DefaultFieldFactory;
import pt.ist.vaadinframework.ui.EmbeddedComponentContainer;
import pt.ist.vaadinframework.ui.TransactionalForm;
import pt.ist.vaadinframework.ui.TransactionalTable;
import pt.utl.ist.fenix.tools.util.Strings;

import com.vaadin.data.Buffered.SourceException;
import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.Select;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

/**
 * 
 * @author David Martinho
 * @author João Neves
 * 
 */
@EmbeddedComponent(path = { "metaTypeManagement" })
public class ManageMetaTypesComponent extends CustomComponent implements EmbeddedComponentContainer {
    private static final String RESOURCE_BUNDLE = "resources/MetaWorkflowResources";
    private static final long serialVersionUID = 1L;

    public ManageMetaTypesComponent() {
	home();
    }

    private void home() {
	manageMetaTypes();
    }

    private void manageMetaTypes() {
	VerticalLayout layout = new VerticalLayout();
	setCompositionRoot(layout);
	layout.setSpacing(true);

	Label title = new Label(getMessage("label.metaType.managament"));
	title.addStyleName(BennuTheme.LABEL_H2);
	layout.addComponent(title);

	Button buttonCreate = new Button(getMessage("link.metaType.create"), new Button.ClickListener() {
	    @Override
	    public void buttonClick(ClickEvent event) {
		createMetaType();
	    }
	});
	buttonCreate.addStyleName(BaseTheme.BUTTON_LINK);
	layout.addComponent(buttonCreate);

	Table table = new TransactionalTable(RESOURCE_BUNDLE) {
	    @Override
	    protected String formatPropertyValue(Object rowId, Object colId, com.vaadin.data.Property property) {
		if (property.getType().isAssignableFrom(Strings.class)) {
		    StringBuilder strBuilder = new StringBuilder();
		    Strings fileClasses = (Strings) property.getValue();
		    for (String fileClass : fileClasses.getUnmodifiableList()) {
			try {
			    strBuilder.append(BundleUtil.getLocalizedNamedFroClass(Class.forName(fileClass)));
			} catch (ClassNotFoundException ex) {
			    strBuilder.append("!" + ex.getClass() + "!");
			}
			strBuilder.append(", ");
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
	    public Object generateCell(Table source, Object itemId, Object columnId) {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		Button buttonView = new Button(BundleUtil.getFormattedStringFromResourceBundle("resources/MyorgResources",
			"link.view"), new Button.ClickListener() {
		    @Override
		    public void buttonClick(ClickEvent event) {
			viewMetaType();
		    }
		});
		Button buttonEdit = new Button(getMessage("link.edit.metaType"), new Button.ClickListener() {
		    @Override
		    public void buttonClick(ClickEvent event) {
			editMetaType();
		    }
		});
		Button buttonFields = new Button(getMessage("link.manage.metaFields"), new Button.ClickListener() {
		    @Override
		    public void buttonClick(ClickEvent event) {
			manageFields();
		    }
		});
		Button buttonQueues = new Button(getMessage("link.manageQueuesInMetaType"), new Button.ClickListener() {
		    @Override
		    public void buttonClick(ClickEvent event) {
			manageQueues();
		    }
		});
		buttonView.addStyleName(BaseTheme.BUTTON_LINK);
		buttonEdit.addStyleName(BaseTheme.BUTTON_LINK);
		buttonFields.addStyleName(BaseTheme.BUTTON_LINK);
		buttonQueues.addStyleName(BaseTheme.BUTTON_LINK);
		horizontalLayout.addComponent(buttonView);
		horizontalLayout.addComponent(buttonEdit);
		horizontalLayout.addComponent(buttonFields);
		horizontalLayout.addComponent(buttonQueues);
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

    private void createMetaType() {
	VerticalLayout layout = new VerticalLayout();
	setCompositionRoot(layout);
	layout.setSpacing(true);

	Label title = new Label(getMessage("label.metaType.create"));
	title.addStyleName(BennuTheme.LABEL_H2);
	layout.addComponent(title);

	TransactionalForm form = new TransactionalForm(RESOURCE_BUNDLE);
	DomainItem<WorkflowMetaType> source = new DomainItem<WorkflowMetaType>(WorkflowMetaType.class);
	source.addItemProperty("firstDescription", new ObjectProperty<String>("", String.class));
	source.setConstructor(new WorkflowMetaTypeMaker());
	form.setFormFieldFactory(new DefaultFieldFactory(RESOURCE_BUNDLE) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected Field makeField(Item item, Object propertyId, Component uiContext) {
		if (propertyId.equals("firstDescription")) {
		    return new RichTextArea();
		}
		return super.makeField(item, propertyId, uiContext);
	    }
	});
	form.setItemDataSource(source, Arrays.asList("name", "firstDescription", "organizationalModel"));
	Select select = (Select) form.getField("organizationalModel");
	for (OrganizationalModel organization : MyOrg.getInstance().getOrganizationalModels()) {
	    select.addItem(organization);
	}
	//source.getItemProperty("organizationalModel").setValue("");
	//((HintedProperty) source.getItemProperty("firstDescription")).addHint(arg0);
    }

    private void viewMetaType() {
    }

    private void editMetaType() {

    }

    private void manageFields() {

    }

    private void manageQueues() {

    }

    private String getMessage(String message) {
	return BundleUtil.getFormattedStringFromResourceBundle(RESOURCE_BUNDLE, message);
    }

    @Override
    public boolean isAllowedToOpen(Map<String, String> arguments) {
	return true;
    }

    @Override
    public void setArguments(Map<String, String> arguments) {
    }
}
