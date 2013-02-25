/**
 * 
 */
package module.metaWorkflow.presentationTier.vaadin;

import java.util.Arrays;
import java.util.Map;

import module.metaWorkflow.domain.MetaField;
import module.metaWorkflow.domain.MetaFieldSet;
import module.metaWorkflow.domain.WorkflowMetaTypeVersion;
import module.metaWorkflow.exceptions.MetaWorkflowDomainException;
import module.metaWorkflow.presentationTier.dto.MetaFieldBean;
import module.metaWorkflow.presentationTier.provider.MetaFieldClassProvider;
import module.vaadin.ui.BennuTheme;

import org.vaadin.dialogs.ConfirmDialog;

import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.domain.RoleType;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.AbstractDomainObject;
import pt.ist.vaadinframework.annotation.EmbeddedComponent;
import pt.ist.vaadinframework.data.reflect.DomainContainer;
import pt.ist.vaadinframework.ui.DefaultFieldFactory;
import pt.ist.vaadinframework.ui.EmbeddedComponentContainer;
import pt.ist.vaadinframework.ui.TransactionalForm;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.ContainerHierarchicalWrapper;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

/**
 * @author João Antunes (joao.antunes@tagus.ist.utl.pt) - 9 de Jul de 2012
 * 
 *         Used to manage all of the {@link MetaField} present in a given {@link WorkflowMetaTypeVersion}. This component is
 *         currently a tab of
 *         the {@link ManageMetaTypeVersionComponent}.
 */
@EmbeddedComponent(path = { "manageMetaFieldsComponent" }, args = { "metaTypeVersion" })
public class ManageMetaFieldsComponent extends CustomComponent implements EmbeddedComponentContainer {

    private static final String RESOURCE_BUNDLE = "resources/MetaWorkflowResources";
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see
     * pt.ist.vaadinframework.ui.EmbeddedComponentContainer#isAllowedToOpen(
     * java.util.Map)
     */
    @Override
    public boolean isAllowedToOpen(Map<String, String> arg0) {
        return UserView.getCurrentUser().hasRoleType(RoleType.MANAGER);
    }

    ManageMetaFieldsComponent(WorkflowMetaTypeVersion metaTypeVersion) {
        manageMetaFieldsInterface(metaTypeVersion);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * pt.ist.vaadinframework.ui.EmbeddedComponentContainer#setArguments(java
     * .util.Map)
     */
    @Override
    public void setArguments(Map<String, String> arg0) {
        WorkflowMetaTypeVersion metaTypeVersion = FenixFramework.getDomainObject(arg0.get("metaTypeVersion"));
        manageMetaFieldsInterface(metaTypeVersion);
    }

    private static String getMessage(String message, String... args) {
        return BundleUtil.getFormattedStringFromResourceBundle(RESOURCE_BUNDLE, message, args);
    }

    private MetaField selectedMetaField = null;

    private void manageMetaFieldsInterface(final WorkflowMetaTypeVersion metaTypeVersion) {
        VerticalLayout rootVerticalLayout = new VerticalLayout();
        setCompositionRoot(rootVerticalLayout);
        ManageMetaTypeVersionComponent
                .addOperationTitleAndMetaTypeName(rootVerticalLayout, "label.manageFields", metaTypeVersion);

        final HorizontalLayout mainContentPanel = new HorizontalLayout();
        mainContentPanel.setSpacing(true);
        rootVerticalLayout.addComponent(mainContentPanel);

        //let's add the panel that will have the tree and the 'add new metafield' button
        final VerticalLayout metaFieldListingLayout = new VerticalLayout();

        //let's make the tree
        final DomainContainer<MetaField> metaFieldsContainer = new DomainContainer<MetaField>(MetaField.class);
        metaFieldsContainer.setContainerProperties("presentationName");
        final Tree metaFieldTree = new Tree(getMessage("label.fields"));
        metaFieldTree.setImmediate(true);
        final ContainerHierarchicalWrapper metaFieldsWrapper = new ContainerHierarchicalWrapper(metaFieldsContainer);
        metaFieldTree.setContainerDataSource(metaFieldsWrapper);
        metaFieldTree.setItemCaptionPropertyId("presentationName");

        wrapChildFields(metaFieldsWrapper, metaTypeVersion.getFieldSet());
        //to sort the fields: TODO:
        //	childs.setItemSorter(new ItemSorter() {
        //
        //	    @Override
        //	    public void setSortProperties(final Sortable container, final Object[] propertyId, final boolean[] ascending) {
        //	    }
        //
        //	    @Override
        //	    public int compare(final Object itemId1, final Object itemId2) {
        //
        //		final AbstractFileNode node1 = getNodeFromItemId(itemId1);
        //		final AbstractFileNode node2 = getNodeFromItemId(itemId2);
        //
        //		return node1.compareTo(node2);
        //	    }
        //
        //	});
        //	

        metaFieldsWrapper.updateHierarchicalWrapper();

        //add the selection listener
        metaFieldTree.addListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                Object value = event.getProperty().getValue();
                if (value != null) {
                    selectedMetaField = (MetaField) value;
                    setupMFEOperationsLayout(metaTypeVersion, metaFieldTree, metaFieldsWrapper);
                } else {
                    metaFieldEditOperationsLayout.removeAllComponents();
                }
            }
        });

        //let's expand the tree fields
        for (Object id : metaFieldTree.rootItemIds()) {
            metaFieldTree.expandItemsRecursively(id);
        }
        metaFieldListingLayout.addComponent(metaFieldTree);

        //Tree added, let's take care of the button to add new fields

        //let's make the window that will have the form
        final Window addMetaFieldWindow = new Window(getMessage("label.addNewMetaField"));
        addMetaFieldWindow.setModal(true);

        VerticalLayout addMetaFieldLayout = (VerticalLayout) addMetaFieldWindow.getContent();
        addMetaFieldLayout.setSizeUndefined();

        //let's get all of the types of meta fields that exist
        final NativeSelect metaFieldTypeSelect =
                new NativeSelect("label.selectMetaFieldType", MetaFieldClassProvider.getMetaFieldClassesSet());

        addMetaFieldLayout.addComponent(metaFieldTypeSelect);

        //the button that reads that and creates a new field
        Button createMetaFieldButton = new Button(getMessage("label.createField"), new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                MetaFieldBean metaFieldBean = new MetaFieldBean();
                metaFieldBean.setFieldClass((Class<? extends MetaField>) metaFieldTypeSelect.getValue());
                MetaFieldSet parentMetaFieldSet = null;
                MultiLanguageString undefinedMLS = new MultiLanguageString();
                metaFieldBean.setName(undefinedMLS.withDefault(getMessage("label.change.please")));

                //let's get where the mouse was selected
                if (selectedMetaField != null) {
                    if (!(selectedMetaField instanceof MetaFieldSet)) {
                        parentMetaFieldSet = selectedMetaField.getParentFieldSet();
                    } else {
                        parentMetaFieldSet = (MetaFieldSet) selectedMetaField;
                    }
                } else {
                    parentMetaFieldSet = metaTypeVersion.getFieldSet();
                }
                try {
                    MetaField newMetaField = MetaField.createMetaField(metaFieldBean, parentMetaFieldSet);
                    wrapChildFields(metaFieldsWrapper, newMetaField);
                    //		    metaFieldsContainer.addItem(newMetaField);
                    metaFieldsWrapper.updateHierarchicalWrapper();
                    metaFieldTree.setValue(newMetaField);

                    addMetaFieldWindow.getParent().removeWindow(addMetaFieldWindow);
                } catch (MetaWorkflowDomainException ex) {
                    getWindow().showNotification(getMessage("error.creating.field.caption"), ex.getLocalizedMessage(),
                            Notification.TYPE_ERROR_MESSAGE);
                }
            }
        });

        Button cancelButton = new Button(getMessage("label.cancel"), new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                addMetaFieldWindow.getParent().removeWindow(addMetaFieldWindow);
            }
        });
        addMetaFieldLayout.addComponent(createMetaFieldButton);
        addMetaFieldLayout.addComponent(cancelButton);

        Button addMetaFieldButton = new Button(getMessage("label.addNewMetaField"), new Button.ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                //let's invoke the window, if it isn't already shown
                if (addMetaFieldWindow.getParent() != null) {
                    getWindow().showNotification(getMessage("label.windowAlreadyOpen"));
                } else {
                    getWindow().addWindow(addMetaFieldWindow);
                }
            }
        });
        addMetaFieldButton.addStyleName(BennuTheme.BUTTON_LINK);
        ManageMetaTypeVersionComponent.readOnlyOrDisabledIfPublished(addMetaFieldButton, metaTypeVersion);

        metaFieldListingLayout.addComponent(addMetaFieldButton);

        mainContentPanel.addComponent(metaFieldListingLayout);

        //now let's take care of the panel where you can actually edit the metafields
        setupMFEOperationsLayout(metaTypeVersion, metaFieldTree, metaFieldsWrapper);
        mainContentPanel.addComponent(metaFieldEditOperationsLayout);

        rootVerticalLayout.addComponent(mainContentPanel);
    }

    private void setupMFEOperationsLayout(WorkflowMetaTypeVersion metaTypeVersion, final Tree metaFieldTree,
            final ContainerHierarchicalWrapper metaFieldsWrapper) {
        if (metaFieldEditOperationsLayout == null) {
            metaFieldEditOperationsLayout = new VerticalLayout();
        } else {
            metaFieldEditOperationsLayout.removeAllComponents();
        }
        if (selectedMetaField != null && !isRootMetaFieldSet(selectedMetaField)) {
            //the form to change the name
            TransactionalForm metaFieldForm = new TransactionalForm(RESOURCE_BUNDLE);
            metaFieldForm.setFormFieldFactory(new DefaultFieldFactory(RESOURCE_BUNDLE));
            metaFieldForm.addSubmitButton();

            //	    ((HintedProperty<?>) metaFieldTree.getItem(selectedMetaField).getItemProperty("name")).addHint(new Required());

            metaFieldForm.setItemDataSource(metaFieldTree.getItem(selectedMetaField),
                    Arrays.asList(new Object[] { "name", "fieldOrder" }));
            metaFieldForm.setWriteThrough(false);

            ManageMetaTypeVersionComponent.readOnlyOrDisabledIfPublished(metaFieldForm, metaTypeVersion);
            metaFieldEditOperationsLayout.addComponent(metaFieldForm);

            Button removeField = new Button(getMessage("label.removeField"), new Button.ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    MetaFieldSet parentFieldSet = selectedMetaField.getParentFieldSet();
                    if (selectedMetaField instanceof MetaFieldSet && ((MetaFieldSet) selectedMetaField).hasAnyChildFields()) {
                        ConfirmDialog.show(getWindow(), getMessage("label.confirm.deletion.fieldSet.with.children.title"),
                                getMessage("label.confirm.deletion.fieldSet.with.children"), getMessage("label.yes"),
                                getMessage("label.no"), new ConfirmDialog.Listener() {
                                    @Override
                                    public void onClose(ConfirmDialog dialog) {
                                        if (dialog.isConfirmed()) {
                                            selectedMetaField.deleteItselfAndAllChildren();
                                        }
                                        metaFieldsWrapper.removeItemRecursively(selectedMetaField);
                                        metaFieldTree.removeItem(selectedMetaField);
                                    }
                                });
                    } else {
                        selectedMetaField.deleteItselfAndAllChildren();
                        metaFieldTree.removeItem(selectedMetaField);
                    }
                    if (parentFieldSet != null) {
                        metaFieldsWrapper.setChildrenAllowed(parentFieldSet, parentFieldSet.hasAnyChildFields());
                    }
                }
            });
            ManageMetaTypeVersionComponent.readOnlyOrDisabledIfPublished(removeField, metaTypeVersion);
            metaFieldEditOperationsLayout.addComponent(removeField);
        }
    }

    private boolean isRootMetaFieldSet(MetaField metaField) {
        return (metaField instanceof MetaFieldSet) && ((MetaFieldSet) metaField).isRoot();
    }

    private VerticalLayout metaFieldEditOperationsLayout = null;

    private void wrapChildFields(ContainerHierarchicalWrapper metaFieldsWrapper, MetaField field) {
        if (field instanceof MetaFieldSet) {
            MetaFieldSet fieldSet = (MetaFieldSet) field;
            metaFieldsWrapper.addItem(field);
            if (fieldSet.getParentFieldSet() != null) {
                metaFieldsWrapper.setParent(field, fieldSet.getParentFieldSet());
            }
            metaFieldsWrapper.setChildrenAllowed(field, fieldSet.hasAnyChildFields());
            for (MetaField childField : fieldSet.getChildFields()) {
                wrapChildFields(metaFieldsWrapper, childField);
            }
        } else {
            metaFieldsWrapper.addItem(field);
            if (field.getParentFieldSet() != null) {
                MetaFieldSet parentFieldSet = field.getParentFieldSet();
                metaFieldsWrapper.setChildrenAllowed(parentFieldSet, parentFieldSet.hasAnyChildFields());
            }
            metaFieldsWrapper.setParent(field, field.getParentFieldSet());
            metaFieldsWrapper.setChildrenAllowed(field, false);
        }
    }
}
