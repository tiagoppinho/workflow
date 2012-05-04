/*
 * @(#)ManageMetaProcessStatesComponent.java
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import module.metaWorkflow.domain.MetaField;
import module.metaWorkflow.domain.MetaProcessState;
import module.metaWorkflow.domain.MetaProcessStateConfig;
import module.metaWorkflow.domain.WorkflowMetaType;
import module.vaadin.ui.BennuTheme;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.RoleType;
import myorg.util.BundleUtil;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.ist.vaadinframework.annotation.EmbeddedComponent;
import pt.ist.vaadinframework.data.reflect.DomainContainer;
import pt.ist.vaadinframework.data.reflect.DomainItem;
import pt.ist.vaadinframework.ui.DefaultFieldFactory;
import pt.ist.vaadinframework.ui.EmbeddedComponentContainer;
import pt.ist.vaadinframework.ui.TransactionalForm;
import pt.ist.vaadinframework.ui.TransactionalTable;
import pt.ist.vaadinframework.ui.fields.MultiLanguageStringField;
import pt.utl.ist.fenix.tools.util.i18n.Language;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;

/**
 * 
 * @author David Martinho
 * @author João Neves
 * @author João Antunes
 * 
 */
@EmbeddedComponent(path = { "metaProcessStatesManagement" }, args = { "metaType" })
public class ManageMetaProcessStatesComponent extends CustomComponent implements EmbeddedComponentContainer {
    private static final String RESOURCE_BUNDLE = "resources/MetaWorkflowResources";
    private static final long serialVersionUID = 1L;

    private final TransactionalForm form = new TransactionalForm(RESOURCE_BUNDLE);

    private final TransactionalTable stateTable = new TransactionalTable(RESOURCE_BUNDLE);

    private MetaProcessState selectedState;

    private VerticalLayout operationsLayout;

    private DomainContainer<MetaProcessState> states;

    public ManageMetaProcessStatesComponent() {
    }

    public static abstract class ConfirmationWindow extends Window {
	private static final long serialVersionUID = 1L;

	public static final Integer STR_TO_PX_RATIO = 8;

	public ConfirmationWindow(String confirmationMessage) {
	    this(confirmationMessage, getMessage("confirm"));
	}

	public ConfirmationWindow(String confirmationMessage, String windowTitle) {
	    super(windowTitle);
	    setModal(true);
	    Integer width = (confirmationMessage.length() * STR_TO_PX_RATIO);
	    setWidth(width + "px");

	    VerticalLayout content = (VerticalLayout) getContent();
	    content.setSpacing(true);
	    content.addComponent(new Label(confirmationMessage));
	    
	    HorizontalLayout buttons = new HorizontalLayout();
	    buttons.setSpacing(true);
	    Button buttonConfirm = new Button(getMessage("confirm"), new Button.ClickListener() {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
		    getParent().removeWindow(ConfirmationWindow.this);
		    onConfirm();
		}
	    });
	    Button buttonCancel = new Button(getMessage("cancel"), new Button.ClickListener() {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
		    getParent().removeWindow(ConfirmationWindow.this);
		}
	    });
	    buttons.addComponent(buttonConfirm);
	    buttons.addComponent(buttonCancel);
	    content.addComponent(buttons);
	}

	abstract public void onConfirm();
    }

    private void manageStatesInterface(final WorkflowMetaType metaType) {
	VerticalLayout headerLayout = new VerticalLayout();
	setCompositionRoot(headerLayout);
	headerLayout.setSpacing(true);

	Label statesTitle = new Label(getMessage("label.metaType.manageStates"));
	statesTitle.addStyleName(BennuTheme.LABEL_H2);
	headerLayout.addComponent(statesTitle);

	Label metaTypeTitle = new Label(getMessage("label.metaType") + ": " + metaType.getName());
	metaTypeTitle.addStyleName(BennuTheme.LABEL_H3);
	headerLayout.addComponent(metaTypeTitle);

	final HorizontalLayout content = new HorizontalLayout();
	content.setSpacing(true);
	headerLayout.addComponent(content);
	initContent(content, metaType);
    }

    private void initContent(final HorizontalLayout content, final WorkflowMetaType metaType) {
	DomainItem<WorkflowMetaType> metaTypeDI = new DomainItem<WorkflowMetaType>(metaType);
	states = (DomainContainer<MetaProcessState>) metaTypeDI.getItemProperty("processStates");
	states.setContainerProperties("name", "position");

	VerticalLayout tableLayout = new VerticalLayout();
	tableLayout.setSpacing(true);
	content.addComponent(tableLayout);
	initTableLayout(tableLayout, metaType);

	operationsLayout = new VerticalLayout();
	operationsLayout.setSpacing(true);
	content.addComponent(operationsLayout);

	form.setFormFieldFactory(new DefaultFieldFactory(RESOURCE_BUNDLE));
	form.addSubmitButton();

	stateTable.addListener(new ValueChangeListener() {
	    @Override
	    public void valueChange(ValueChangeEvent event) {
		selectedState = (MetaProcessState) event.getProperty().getValue();
		if (selectedState != null) {
		    initOperationsLayout();
		} else {
		    operationsLayout.removeAllComponents();
		}
	    }
	});
    }

    private void initTableLayout(VerticalLayout tableLayout, final WorkflowMetaType metaType) {
	stateTable.setContainerDataSource(states);
	stateTable.setSelectable(true);
	stateTable.setImmediate(true);
	stateTable.addGeneratedColumn("", new ColumnGenerator() {
	    @Override
	    public Object generateCell(final Table source, final Object itemId, Object columnId) {
		Button buttonRemove = new Button("", new Button.ClickListener() {
		    @Override
		    public void buttonClick(ClickEvent event) {
			final MetaProcessState state = ((MetaProcessState) itemId);
			ConfirmationWindow confirmationWindow = new ConfirmationWindow(getMessage("state.remove.confirm", state
				.getName().getContent())) {
			    @Override
			    public void onConfirm() {
				source.unselect(state);
				states.removeItem(state);
				state.delete();
			    }
			};
			getWindow().addWindow(confirmationWindow);
		    }
		});
		buttonRemove.setIcon(new ThemeResource("../runo/icons/16/cancel.png"));
		buttonRemove.addStyleName(BennuTheme.BUTTON_SMALL);
		return buttonRemove;
	    }
	});
	tableLayout.addComponent(stateTable);

	Button buttonAdd = new Button(getMessage("link.processState.add"), new Button.ClickListener() {
	    @Override
	    public void buttonClick(ClickEvent event) {
		addProcessState(metaType, states);
	    }
	});
	buttonAdd.addStyleName(BaseTheme.BUTTON_LINK);
	tableLayout.addComponent(buttonAdd);
    }

    private void initOperationsLayout() {
	operationsLayout.removeAllComponents();
	operationsLayout.addComponent(form);
	form.setItemDataSource(stateTable.getItem(selectedState), Arrays.asList(new Object[] { "name", "position" }));
	form.setWriteThrough(false);

	if (!selectedState.hasAnyConfigs()) {
	    Label noConditions = new Label(getMessage("state.activation.conditions.none"));
	    operationsLayout.addComponent(noConditions);
	} else {
	    Panel conditionsPanel = new Panel(getMessage("state.activation.conditions"));
	    conditionsPanel.setHeight("275px");
	    conditionsPanel.setWidth("620px");
	    operationsLayout.addComponent(conditionsPanel);

	    Iterator<MetaProcessStateConfig> configIterator = selectedState.getConfigs().iterator();
	    while (configIterator.hasNext()) {
		MetaProcessStateConfig config = configIterator.next();
		HorizontalLayout configLayout = new HorizontalLayout();
		configLayout.setSpacing(true);
		conditionsPanel.addComponent(configLayout);
		initConfigLayout(config, configLayout);

		if (configIterator.hasNext()) {
		    Label or = new Label(getMessage("state.activation.conditions.or"));
		    conditionsPanel.addComponent(or);
		}
	    }
	}

	Button addConfigButton = new Button(getMessage("state.activation.conditions.add"), new Button.ClickListener() {
	    @Override
	    public void buttonClick(ClickEvent event) {
		addStateConfig(selectedState);
		initOperationsLayout();
	    }
	});
	addConfigButton.addStyleName(BaseTheme.BUTTON_LINK);
	operationsLayout.addComponent(addConfigButton);
    }

    private void initConfigLayout(final MetaProcessStateConfig config, HorizontalLayout configLayout) {
	Iterator<MetaProcessState> dependedStatesIterator = config.getDependedStates().iterator();
	Iterator<MetaField> dependedFieldsIterator = config.getDependedFields().iterator();

	Label indentationLabel = new Label(" - ");
	configLayout.addComponent(indentationLabel);
	if (!dependedStatesIterator.hasNext() && !dependedFieldsIterator.hasNext()) {
	    Label noDependenciesLabel = new Label(getMessage("state.activation.conditions.depended.none"));
	    configLayout.addComponent(noDependenciesLabel);
	}

	if (dependedStatesIterator.hasNext()) {
	    Label statesLabel = new Label(getMessage("state.activation.conditions.depended.states") + ": ");
	    configLayout.addComponent(statesLabel);
	}
	while (dependedStatesIterator.hasNext()) {
	    MetaProcessState dependedState = dependedStatesIterator.next();
	    Label dependedStateLabel = new Label(dependedState.getName().getContent());
	    configLayout.addComponent(dependedStateLabel);
	    
	    if (dependedStatesIterator.hasNext()) {
		Label separatorLabel = new Label(", ");
		configLayout.addComponent(separatorLabel);
	    } else {
		Label separatorLabel = new Label(" ");
		configLayout.addComponent(separatorLabel);
	    }
	}

	if (dependedFieldsIterator.hasNext()) {
	    Label fieldsLabel = new Label(getMessage("state.activation.conditions.depended.fields") + ": ");
	    configLayout.addComponent(fieldsLabel);
	}
	while (dependedFieldsIterator.hasNext()) {
	    MetaField dependedField = dependedFieldsIterator.next();
	    Label dependedFieldLabel = new Label(dependedField.getName().getContent());
	    configLayout.addComponent(dependedFieldLabel);

	    if (dependedFieldsIterator.hasNext()) {
		Label separatorLabel = new Label(", ");
		configLayout.addComponent(separatorLabel);
	    }
	}

	Button deleteConfigButton = new Button(getMessage("state.activation.conditions.del"), new Button.ClickListener() {
	    @Override
	    public void buttonClick(ClickEvent event) {
		deleteStateConfig(config);
		initOperationsLayout();
	    }
	});
	deleteConfigButton.addStyleName(BaseTheme.BUTTON_LINK);
	configLayout.addComponent(deleteConfigButton);

	configLayout.addComponent(new Label(", "));

	Button editConfigButton = new Button(getMessage("state.activation.conditions.depended.add"), new Button.ClickListener() {
	    @Override
	    public void buttonClick(ClickEvent event) {
		openEditConfigWindow(config);
	    }
	});
	editConfigButton.addStyleName(BaseTheme.BUTTON_LINK);
	configLayout.addComponent(editConfigButton);
    }

    private static final String NEW_STATE_LABEL = "new.state.name";

    private void openEditConfigWindow(final MetaProcessStateConfig config) {
	final Window editConfigWindow = new Window(getMessage("state.activation.conditions.depended.add"));
	getWindow().addWindow(editConfigWindow);
	editConfigWindow.setModal(true);
	editConfigWindow.setHeight("500px");
	editConfigWindow.setWidth("400px");

	VerticalLayout content = new VerticalLayout();
	editConfigWindow.addComponent(content);
	content.setSpacing(true);
	content.addComponent(new Label(getMessage("state.activation.conditions.choose.dependencies")));

	TabSheet dependenciesTabs = new TabSheet();
	dependenciesTabs.setHeight("400px");
	content.addComponent(dependenciesTabs);

	VerticalLayout tableLayout = new VerticalLayout();
	tableLayout.setSpacing(true);
	tableLayout.setMargin(true);
	dependenciesTabs.addTab(tableLayout, getMessage("state.activation.conditions.depended.states"));

	WorkflowMetaType metaType = config.getMetaProcessState().getWorkflowMetaType();
	Set<MetaProcessState> possibleStates = new HashSet<MetaProcessState>();
	possibleStates.addAll(metaType.getProcessStates());
	// A state cannot depend on itself
	possibleStates.remove(config.getMetaProcessState());
	// Cannot add duplicate dependencies
	for (MetaProcessState existingDependedState : config.getDependedStates()) {
	    possibleStates.remove(existingDependedState);
	}
	
	if (possibleStates.isEmpty()) {
	    tableLayout.addComponent(new Label(getMessage("state.activation.conditions.depended.none.to.add")));
	    return;
	}

	TransactionalTable dependedStatesTable = new TransactionalTable(RESOURCE_BUNDLE);
	DomainContainer<MetaProcessState> dependedStates = new DomainContainer<MetaProcessState>(possibleStates,
		MetaProcessState.class);
	dependedStates.setContainerProperties("name", "position");

	dependedStatesTable.setContainerDataSource(dependedStates);
	dependedStatesTable.setSelectable(true);
	dependedStatesTable.setImmediate(true);
	dependedStatesTable.addListener(new ValueChangeListener() {
	    @Override
	    public void valueChange(ValueChangeEvent event) {
		MetaProcessState dependedState = (MetaProcessState) event.getProperty().getValue();
		if (dependedState != null) {
		    config.addDependedStates(dependedState);
		    getWindow().removeWindow(editConfigWindow);
		    initOperationsLayout();
		}
	    }
	});
	tableLayout.addComponent(dependedStatesTable);
    }

    private void addProcessState(WorkflowMetaType metaType, DomainContainer<MetaProcessState> states) {
	MetaProcessState newState = MetaProcessState.create(metaType, getMessage(NEW_STATE_LABEL), 1);
	states.addItem(newState);
	stateTable.setValue(newState);
	stateTable.sort();
	TextField ptText = ((MultiLanguageStringField) form.getField("name")).getTextField(Language.pt);
	ptText.focus();
	ptText.setSelectionRange(0, ((String) ptText.getValue()).length());
    }

    private void addStateConfig(MetaProcessState state) {
	MetaProcessStateConfig.create(state);
    }

    private void deleteStateConfig(MetaProcessStateConfig config) {
	config.delete();
    }

    private static String getMessage(String message, String ... args) {
	return BundleUtil.getFormattedStringFromResourceBundle(RESOURCE_BUNDLE, message, args);
    }

    @Override
    public boolean isAllowedToOpen(Map<String, String> arguments) {
	return UserView.getCurrentUser().hasRoleType(RoleType.MANAGER);
    }

    @Override
    public void setArguments(Map<String, String> arg0) {
	WorkflowMetaType metaType = AbstractDomainObject.fromExternalId(arg0.get("metaType"));
	manageStatesInterface(metaType);
    }
}
