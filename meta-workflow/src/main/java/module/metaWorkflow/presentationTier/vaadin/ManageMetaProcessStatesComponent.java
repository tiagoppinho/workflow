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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import module.metaWorkflow.domain.MetaField;
import module.metaWorkflow.domain.MetaFieldSet;
import module.metaWorkflow.domain.MetaProcessState;
import module.metaWorkflow.domain.MetaProcessStateConfig;
import module.metaWorkflow.domain.WorkflowMetaType;
import module.metaWorkflow.domain.WorkflowMetaTypeVersion;
import module.vaadin.ui.BennuTheme;
import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.domain.RoleType;
import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.fenixframework.FenixFramework;
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
import com.vaadin.data.util.ContainerHierarchicalWrapper;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbstractSelect.MultiSelectMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
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
@EmbeddedComponent(path = { "metaProcessStatesManagement" }, args = { "metaTypeVersion" })
public class ManageMetaProcessStatesComponent extends CustomComponent implements EmbeddedComponentContainer {
    private static final String RESOURCE_BUNDLE = "resources/MetaWorkflowResources";
    private static final long serialVersionUID = 1L;

    private final TransactionalForm form = new TransactionalForm(RESOURCE_BUNDLE);

    private final TransactionalTable stateTable = new TransactionalTable(RESOURCE_BUNDLE);

    private MetaProcessState selectedState;

    private VerticalLayout operationsLayout;

    private DomainContainer<MetaProcessState> states;

    public ManageMetaProcessStatesComponent(WorkflowMetaTypeVersion metaTypeVersion) {
        manageStatesInterface(metaTypeVersion);
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

    private void manageStatesInterface(final WorkflowMetaTypeVersion metaTypeVersion) {
        VerticalLayout headerLayout = new VerticalLayout();
        setCompositionRoot(headerLayout);
        ManageMetaTypeVersionComponent.addOperationTitleAndMetaTypeName(headerLayout, "label.metaType.manageStates",
                metaTypeVersion);

        final HorizontalLayout content = new HorizontalLayout();
        content.setSpacing(true);
        headerLayout.addComponent(content);
        initContent(content, metaTypeVersion);
    }

    private void initContent(final HorizontalLayout content, final WorkflowMetaTypeVersion metaTypeVersion) {
        DomainItem<WorkflowMetaTypeVersion> metaTypeDI = new DomainItem<WorkflowMetaTypeVersion>(metaTypeVersion);
        states = (DomainContainer<MetaProcessState>) metaTypeDI.getItemProperty("processStates");
        states.setContainerProperties("name", "position");

        VerticalLayout tableLayout = new VerticalLayout();
        tableLayout.setSpacing(true);
        content.addComponent(tableLayout);
        initTableLayout(tableLayout, metaTypeVersion);

        operationsLayout = new VerticalLayout();
        operationsLayout.setSpacing(true);
        content.addComponent(operationsLayout);

        form.setFormFieldFactory(new DefaultFieldFactory(RESOURCE_BUNDLE));
        form.addSubmitButton();

        ManageMetaTypeVersionComponent.readOnlyOrDisabledIfPublished(form, metaTypeVersion);

        stateTable.addListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                selectedState = (MetaProcessState) event.getProperty().getValue();
                if (selectedState != null) {
                    initOperationsLayout(metaTypeVersion);
                } else {
                    operationsLayout.removeAllComponents();
                }
            }
        });
    }

    private void initTableLayout(VerticalLayout tableLayout, final WorkflowMetaTypeVersion metaTypeVersion) {
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
                        ConfirmationWindow confirmationWindow =
                                new ConfirmationWindow(getMessage("state.remove.confirm", state.getName().getContent())) {
                                    @Override
                                    public void onConfirm() {
                                        try {
                                            state.delete();
                                            source.unselect(state);
                                            states.removeItem(state);
                                        } catch (DomainException ex) {
                                            ManageMetaProcessStatesComponent.this.getWindow().showNotification(
                                                    getMessage("error.while.deleting.state") + "<br/>",
                                                    getMessage(ex.getMessage()), Notification.TYPE_ERROR_MESSAGE);
                                        }
                                    }
                                };
                        getWindow().addWindow(confirmationWindow);
                    }
                });
                buttonRemove.setIcon(new ThemeResource("../runo/icons/16/cancel.png"));
                buttonRemove.addStyleName(BennuTheme.BUTTON_SMALL);
                ManageMetaTypeVersionComponent.readOnlyOrDisabledIfPublished(buttonRemove, metaTypeVersion);
                return buttonRemove;
            }
        });
        tableLayout.addComponent(stateTable);

        Button buttonAdd = new Button(getMessage("link.processState.add"), new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                addProcessState(metaTypeVersion, states);
            }
        });
        buttonAdd.addStyleName(BaseTheme.BUTTON_LINK);
        ManageMetaTypeVersionComponent.readOnlyOrDisabledIfPublished(buttonAdd, metaTypeVersion);
        tableLayout.addComponent(buttonAdd);
    }

    private void initOperationsLayout(final WorkflowMetaTypeVersion metaTypeVersion) {
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
                initConfigLayout(config, configLayout, metaTypeVersion);

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
                initOperationsLayout(metaTypeVersion);
            }
        });
        addConfigButton.addStyleName(BaseTheme.BUTTON_LINK);
        ManageMetaTypeVersionComponent.readOnlyOrDisabledIfPublished(addConfigButton, metaTypeVersion);
        operationsLayout.addComponent(addConfigButton);
    }

    private void initConfigLayout(final MetaProcessStateConfig config, HorizontalLayout configLayout,
            final WorkflowMetaTypeVersion metaTypeVersion) {
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
                initOperationsLayout(metaTypeVersion);
            }
        });
        deleteConfigButton.addStyleName(BaseTheme.BUTTON_LINK);
        ManageMetaTypeVersionComponent.readOnlyOrDisabledIfPublished(deleteConfigButton, metaTypeVersion);
        configLayout.addComponent(deleteConfigButton);

        configLayout.addComponent(new Label(", "));

        Button editConfigButton =
                new Button(getMessage("state.activation.conditions.depended.change"), new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        openEditConfigWindow(config, metaTypeVersion);
                    }
                });
        editConfigButton.addStyleName(BaseTheme.BUTTON_LINK);
        ManageMetaTypeVersionComponent.readOnlyOrDisabledIfPublished(editConfigButton, metaTypeVersion);
        configLayout.addComponent(editConfigButton);
    }

    private static final String NEW_STATE_LABEL = "new.state.name";

    private void openEditConfigWindow(final MetaProcessStateConfig config, final WorkflowMetaTypeVersion metaTypeVersion) {
        final Window editConfigWindow = new Window(getMessage("state.activation.conditions.depended.change"));
        getWindow().addWindow(editConfigWindow);
        editConfigWindow.setModal(true);
        editConfigWindow.setHeight("550px");
        editConfigWindow.setWidth("550px");

        VerticalLayout content = new VerticalLayout();
        editConfigWindow.addComponent(content);
        content.setSpacing(true);
        content.addComponent(new Label(getMessage("state.activation.conditions.choose.dependencies")));

        TabSheet dependenciesTabs = new TabSheet();
        dependenciesTabs.setHeight("400px");
        content.addComponent(dependenciesTabs);

        VerticalLayout fieldsLayout = new VerticalLayout();
        fieldsLayout.setSpacing(true);
        fieldsLayout.setMargin(true);
        dependenciesTabs.addTab(fieldsLayout, getMessage("state.activation.conditions.depended.fields"));

        final TreeTable fieldsTable = new TreeTable();
        fieldsLayout.addComponent(fieldsTable);
        fieldsTable.setSelectable(true);
        fieldsTable.setMultiSelect(true);
        fieldsTable.setMultiSelectMode(MultiSelectMode.SIMPLE);

        DomainContainer<MetaField> fieldContainer = new DomainContainer<MetaField>(MetaField.class);
        fieldContainer.setContainerProperties("name", "localizedClassName", "fieldOrder");

        ContainerHierarchicalWrapper fieldContainerWrapper = new ContainerHierarchicalWrapper(fieldContainer);
        MetaFieldSet fieldSet = config.getMetaProcessState().getWorkflowMetaType().getFieldSet();
        fieldContainerWrapper.addItem(fieldSet);
        addFieldHierarchy(fieldContainerWrapper, fieldSet);

        fieldsTable.setContainerDataSource(fieldContainerWrapper);
        fieldsTable.setColumnHeader("name", getMessage("module.metaWorkflow.domain.MetaField.name"));
        fieldsTable.setColumnHeader("localizedClassName", getMessage("module.metaWorkflow.domain.MetaField.localizedClassName"));
        fieldsTable.setColumnHeader("fieldOrder", getMessage("module.metaWorkflow.domain.MetaField.fieldOrder"));
        fieldsTable.setCollapsed(fieldSet, false);

        // Pre-select already depended fields
        for (MetaField dependedField : config.getDependedFields()) {
            fieldsTable.select(dependedField);
        }

        VerticalLayout statesLayout = new VerticalLayout();
        statesLayout.setSpacing(true);
        statesLayout.setMargin(true);
        dependenciesTabs.addTab(statesLayout, getMessage("state.activation.conditions.depended.states"));

        WorkflowMetaType metaType = config.getMetaProcessState().getWorkflowMetaType();
        Set<MetaProcessState> possibleStates = new HashSet<MetaProcessState>();
        possibleStates.addAll(metaType.getProcessStates());
        // A state cannot depend on itself
        possibleStates.remove(config.getMetaProcessState());

        if (possibleStates.isEmpty()) {
            statesLayout.addComponent(new Label(getMessage("state.activation.conditions.depended.none.to.add")));
            return;
        }

        final TransactionalTable dependedStatesTable = new TransactionalTable(RESOURCE_BUNDLE);
        DomainContainer<MetaProcessState> dependedStates =
                new DomainContainer<MetaProcessState>(possibleStates, MetaProcessState.class);
        dependedStates.setContainerProperties("name", "position");

        dependedStatesTable.setContainerDataSource(dependedStates);
        dependedStatesTable.setSelectable(true);
        dependedStatesTable.setMultiSelect(true);
        dependedStatesTable.setMultiSelectMode(MultiSelectMode.SIMPLE);

        // Pre-select already depended states
        for (MetaProcessState dependedState : config.getDependedStates()) {
            dependedStatesTable.select(dependedState);
        }
        statesLayout.addComponent(dependedStatesTable);

        Button saveButton = new Button(getMessage("state.activation.conditions.save"));

        saveButton.addListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                config.updateDependedStates((Collection<MetaProcessState>) dependedStatesTable.getValue());
                config.updateDependedFields((Collection<MetaField>) fieldsTable.getValue());
                getWindow().removeWindow(editConfigWindow);
                initOperationsLayout(metaTypeVersion);
            }
        });
        content.addComponent(saveButton);
    }

    public void addFieldHierarchy(ContainerHierarchicalWrapper container, MetaFieldSet fieldSet) {
        for (MetaField childField : fieldSet.getOrderedChildFields()) {
            container.addItem(childField);
            container.setParent(childField, fieldSet);
            if (childField instanceof MetaFieldSet) {
                addFieldHierarchy(container, (MetaFieldSet) childField);
            } else {
                container.setChildrenAllowed(childField, false);
            }
        }
    }

    private void addProcessState(WorkflowMetaTypeVersion metaTypeVersion, DomainContainer<MetaProcessState> states) {
        MetaProcessState newState = MetaProcessState.create(metaTypeVersion, getMessage(NEW_STATE_LABEL), 1);
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

    private static String getMessage(String message, String... args) {
        return BundleUtil.getFormattedStringFromResourceBundle(RESOURCE_BUNDLE, message, args);
    }

    @Override
    public boolean isAllowedToOpen(Map<String, String> arguments) {
        return UserView.getCurrentUser().hasRoleType(RoleType.MANAGER);
    }

    @Override
    public void setArguments(Map<String, String> arg0) {
        WorkflowMetaTypeVersion metaTypeVersion = FenixFramework.getDomainObject(arg0.get("metaTypeVersion"));
        manageStatesInterface(metaTypeVersion);
    }
}
