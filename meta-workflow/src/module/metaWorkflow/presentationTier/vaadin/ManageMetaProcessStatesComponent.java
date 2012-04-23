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
import java.util.Map;

import module.metaWorkflow.domain.MetaProcessState;
import module.metaWorkflow.domain.WorkflowMetaType;
import module.vaadin.ui.BennuTheme;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.RoleType;
import myorg.util.BundleUtil;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.ist.vaadinframework.annotation.EmbeddedComponent;
import pt.ist.vaadinframework.data.ItemWriter;
import pt.ist.vaadinframework.data.reflect.DomainContainer;
import pt.ist.vaadinframework.data.reflect.DomainItem;
import pt.ist.vaadinframework.ui.DefaultFieldFactory;
import pt.ist.vaadinframework.ui.EmbeddedComponentContainer;
import pt.ist.vaadinframework.ui.TransactionalForm;
import pt.ist.vaadinframework.ui.TransactionalTable;

import com.vaadin.data.Buffered.SourceException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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

    private final DomainItem<MetaProcessState> selectedItem = new DomainItem<MetaProcessState>(MetaProcessState.class);

    private final TransactionalTable stateTable = new TransactionalTable(RESOURCE_BUNDLE);

    public class MetaProcessStateWriter implements ItemWriter<Object> {
	@Override
	public Object[] getOrderedArguments() {
	    return new Object[] { "position" };
	}

	public MetaProcessState write(MetaProcessState state, Integer position) throws SourceException {
	    state.setPosition(position);
	    return state;
	}
    }

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
	VerticalLayout header = new VerticalLayout();
	setCompositionRoot(header);
	header.setSpacing(true);

	Label statesTitle = new Label(getMessage("label.metaType.manageStates"));
	statesTitle.addStyleName(BennuTheme.LABEL_H2);
	header.addComponent(statesTitle);

	Label metaTypeTitle = new Label(getMessage("label.metaType") + ": " + metaType.getName());
	metaTypeTitle.addStyleName(BennuTheme.LABEL_H3);
	header.addComponent(metaTypeTitle);

	final HorizontalLayout content = new HorizontalLayout();
	content.setSpacing(true);
	header.addComponent(content);
	


	DomainItem<WorkflowMetaType> metaTypeDI = new DomainItem<WorkflowMetaType>(metaType);
	final DomainContainer<MetaProcessState> states = (DomainContainer<MetaProcessState>) metaTypeDI
		.getItemProperty("processStates");
	states.setContainerProperties("name");
	stateTable.setContainerDataSource(states);
	stateTable.setSelectable(true);
	stateTable.setImmediate(true);
	stateTable.setPropertyDataSource(selectedItem);
	stateTable.addGeneratedColumn("", new ColumnGenerator() {
	    @Override
	    public Object generateCell(Table source, final Object itemId, Object columnId) {
		Button buttonRemove = new Button(getMessage("state.remove"), new Button.ClickListener() {
		    @Override
		    public void buttonClick(ClickEvent event) {
			final MetaProcessState state = ((MetaProcessState) itemId);
			ConfirmationWindow confirmationWindow = new ConfirmationWindow(getMessage("state.remove.confirm", state
				.getName().getContent())) {
			    @Override
			    public void onConfirm() {
				states.removeItem(state);
				state.delete();
			    }
			};
			getWindow().addWindow(confirmationWindow);
		    }
		});
		return buttonRemove;
	    }
	});
	
	content.addComponent(stateTable);
	stateTable.addListener(new ValueChangeListener() {
	    @Override
	    public void valueChange(ValueChangeEvent event) {
		getWindow().showNotification("TABELA", Window.Notification.TYPE_TRAY_NOTIFICATION);
	    }
	});

	selectedItem.addListener(new ValueChangeListener() {
	    @Override
	    public void valueChange(ValueChangeEvent event) {
		getWindow().showNotification("ITEM");
		if (event.getProperty().getValue() != null) {
		    content.addComponent(form);
		} else {
		    content.removeComponent(form);
		}
	    }
	});

	MetaProcessStateWriter stateWriter = new MetaProcessStateWriter();
	selectedItem.setWriter(stateWriter);

	form.setFormFieldFactory(new DefaultFieldFactory(RESOURCE_BUNDLE));
	form.setWriteThrough(false);
	form.setItemDataSource(selectedItem, Arrays.asList(new Object[] { "position" }));
	form.addSubmitButton();
	form.addClearButton();

	Button buttonAdd = new Button(getMessage("link.processState.add"), new Button.ClickListener() {
	    @Override
	    public void buttonClick(ClickEvent event) {
		addProcessState(metaType, states);
	    }
	});
	buttonAdd.addStyleName(BaseTheme.BUTTON_LINK);
	header.addComponent(buttonAdd);
    }

    private static final String NEW_STATE_NAME = "new.state.name";

    private void addProcessState(WorkflowMetaType metaType, DomainContainer<MetaProcessState> states) {
	MetaProcessState newState = MetaProcessState.create(metaType, getMessage(NEW_STATE_NAME), 1);
	states.addItem(newState);
	selectedItem.setValue(newState);
	stateTable.sort();
	TextField positionField = (TextField) form.getField("position");
	positionField.focus();
	positionField.setSelectionRange(0, ((String) positionField.getValue()).length());
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
