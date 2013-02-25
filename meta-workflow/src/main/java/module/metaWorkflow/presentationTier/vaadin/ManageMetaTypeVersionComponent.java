/**
 * 
 */
package module.metaWorkflow.presentationTier.vaadin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import module.metaWorkflow.domain.MetaProcessState;
import module.metaWorkflow.domain.WorkflowMetaTypeVersion;
import module.vaadin.ui.BennuTheme;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.vaadin.dialogs.ConfirmDialog;

import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.domain.RoleType;
import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.vaadinframework.EmbeddedApplication;
import pt.ist.vaadinframework.annotation.EmbeddedComponent;
import pt.ist.vaadinframework.data.reflect.DomainItem;
import pt.ist.vaadinframework.ui.EmbeddedComponentContainer;
import pt.ist.vaadinframework.ui.TransactionalForm;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author João Antunes (joao.antunes@tagus.ist.utl.pt) - 18 de Jun de 2012
 * 
 *         Used to manage all of the things that are dependent of a {@link WorkflowMetaTypeVersion} i.e. states, fields,
 *         description
 * 
 */
@EmbeddedComponent(path = { "manageMetaTypeVersionComponent" }, args = { "metaTypeVersion" })
public class ManageMetaTypeVersionComponent extends CustomComponent implements EmbeddedComponentContainer {

    /**
     * default serial version
     */
    private static final long serialVersionUID = 1L;
    private static final String RESOURCE_BUNDLE = "resources/MetaWorkflowResources";
    private static final ThemeResource NEW_VERSION_ICON = new ThemeResource("../runo/icons/16/document-add.png");
    private static final ThemeResource REMOVE_VERSION_ICON = new ThemeResource("../runo/icons/16/cancel.png");

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
        manageMetaTypeVersionInterface(metaTypeVersion);

    }

    protected final List<Button> versionActionButtonsList = new ArrayList<Button>();

    public List<Button> getVersionActionButtonsList() {
        return versionActionButtonsList;
    }

    /**
     * 
     * @return A {@link Button} that represent the action possible on this
     *         version. i.e. if published and no draft exists, create a clone;
     *         if a draft, remove it
     */
    protected Button generateVersionActionIconedButton(final WorkflowMetaTypeVersion metaTypeVersion) {
        Button versionActionIconedButton = new Button();
        versionActionIconedButton.setStyleName(BennuTheme.BUTTON_SMALL);
        if (metaTypeVersion.getPublished()) {
            //add the 'create clone' button
            if (metaTypeVersion.getMetaType().hasDraftMetaTypeVersion()) {
                //the button will be disabled as we cannot create any new versions, and the tooltip will explain
                versionActionIconedButton.setEnabled(false);
                versionActionIconedButton.setDescription(getMessage("label.tooltip.cloneVersion.disabled"));

            }
            versionActionIconedButton.setDescription(getMessage("label.tooltip.cloneVersion"));

            versionActionIconedButton.setIcon(NEW_VERSION_ICON);

            versionActionIconedButton.addListener(new Button.ClickListener() {

                @Override
                public void buttonClick(ClickEvent event) {
                    try {
                        metaTypeVersion.createNewUnpublishedVersion();
                    } catch (DomainException e) {
                        System.out.print(e.getMessage());
                        e.printStackTrace(System.out);
                        throw e;
                    }
                    if (metaTypeVersion.getMetaType().hasDraftMetaTypeVersion()) {
                        for (Button button : getVersionActionButtonsList()) {
                            button.setEnabled(false);
                            button.setDescription(getMessage("label.tooltip.cloneVersion.disabled"));

                        }
                    }
                }
            });

        } else {
            //add the 'remove draft' button

            versionActionIconedButton.setDescription(getMessage("label.tooltip.removeDraft"));
            versionActionIconedButton.setIcon(REMOVE_VERSION_ICON);

            //let's add a confirmation message in case they click it

            versionActionIconedButton.addListener(new Button.ClickListener() {

                @Override
                public void buttonClick(ClickEvent event) {
                    ConfirmDialog.show(getWindow(), getMessage("label.deleteVersionConfirm.title"),
                            getMessage("label.confirmDialog.content"), getMessage("label.yes"), getMessage("label.no"),
                            new ConfirmDialog.Listener() {

                                @Override
                                public void onClose(ConfirmDialog dialog) {
                                    if (dialog.isConfirmed()) {
                                        // Confirmed to continue - delete the version
                                        metaTypeVersion.delete();
                                        EmbeddedApplication.back(getApplication());

                                    }
                                }
                            });

                }
            });
        }
        versionActionButtonsList.add(versionActionIconedButton);
        return versionActionIconedButton;
    }

    /**
     * Interface - creates the whole interface
     * 
     * @param metaTypeVersion
     */
    private void manageMetaTypeVersionInterface(WorkflowMetaTypeVersion metaTypeVersion) {

        //the root layout
        VerticalLayout layout = new VerticalLayout();
        setCompositionRoot(layout);
        layout.setSpacing(true);

        //the label and actions on the version
        HorizontalLayout titleAndActionLinksLayout = new HorizontalLayout();
        titleAndActionLinksLayout.setSpacing(true);

        Label versionTitle = new Label(getMessage("label.metaType.version") + " " + metaTypeVersion.getVersion());
        versionTitle.addStyleName(BennuTheme.LABEL_H1);
        titleAndActionLinksLayout.addComponent(versionTitle);

        Label stateLabel =
                metaTypeVersion.getPublished() ? new Label("- " + getMessage("label.published") + " -") : new Label("- "
                        + getMessage("label.draft") + " -");
        stateLabel.addStyleName(BennuTheme.LABEL_H3);
        titleAndActionLinksLayout.addComponent(stateLabel);

        titleAndActionLinksLayout.addComponent(generateVersionActionIconedButton(metaTypeVersion));

        //let's add the title and action links to the main layout
        layout.addComponent(titleAndActionLinksLayout);

        //let's add the back link button
        Button backButton = new Button(getMessage("label.back"));
        backButton.setStyleName(BennuTheme.BUTTON_LINK);
        backButton.setDescription(getMessage("tooltip.back.onManageMetaTypeVersion"));
        backButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                EmbeddedApplication.back(getApplication());

            }
        });

        layout.addComponent(backButton);

        Label separatorLabel = new Label("<hr/>");
        separatorLabel.setContentMode(Label.CONTENT_XHTML);
        //the separator to prettify
        layout.addComponent(separatorLabel);

        VersionInfoPanelComponent infoPanelComponent = new VersionInfoPanelComponent(metaTypeVersion);
        ManageMetaProcessStatesComponent processStatesComponent = new ManageMetaProcessStatesComponent(metaTypeVersion);

        ManageMetaFieldsComponent metaFieldsComponent = new ManageMetaFieldsComponent(metaTypeVersion);

        TabSheet mainTabSheet = new TabSheet();
        mainTabSheet.addTab(infoPanelComponent, getMessage("label.info"));
        mainTabSheet.addTab(processStatesComponent, getMessage("ManageMetaTypeVersionComponent.label.proccessStates"));
        mainTabSheet.addTab(metaFieldsComponent, getMessage("label.fields"));

        layout.addComponent(mainTabSheet);

    }

    static void readOnlyOrDisabledIfPublished(Component component, WorkflowMetaTypeVersion metaTypeVersion) {
        if (metaTypeVersion.getPublished()) {
            component.setEnabled(false);
        }

    }

    /**
     * Adds to the given layout, the two labels with the name of the operation
     * name, provided by the operationKey, and the name of the metatype. e.g.: <br/>
     * --<br/>
     * <h1>Manage states</h1>
     * <p>
     * Meta type name: xpto
     * </p>
     * --<br/>
     * 
     * @param operationKey
     * @param layout
     */
    static void addOperationTitleAndMetaTypeName(VerticalLayout layout, String operationKey,
            WorkflowMetaTypeVersion metaTypeVersion) {
        layout.setSpacing(true);

        Label statesTitle = new Label(getMessage(operationKey));
        statesTitle.addStyleName(BennuTheme.LABEL_H2);
        layout.addComponent(statesTitle);

        Label metaTypeTitle = new Label(getMessage("label.metaType") + ": " + metaTypeVersion.getMetaType().getName());
        metaTypeTitle.addStyleName(BennuTheme.LABEL_H3);
        layout.addComponent(metaTypeTitle);
    }

    /**
     * 
     * @author João Antunes (joao.antunes@tagus.ist.utl.pt) - 27 de Jun de 2012
     * 
     *         Class that represent the tab with version info on the
     *         ManageMetaTypeVersionComponent
     * 
     */
    private class VersionInfoPanelComponent extends CustomComponent {

        private final VerticalLayout mainLayout;

        public VersionInfoPanelComponent(WorkflowMetaTypeVersion metaTypeVersion) {
            mainLayout = new VerticalLayout();
            this.setCompositionRoot(mainLayout);
            //we will have two different versions of this panel depending if the metaTypeVersion is published or not
            if (metaTypeVersion.getPublished()) {
                initVersionPublished(metaTypeVersion);
            } else {
                initVersionUnpublished(metaTypeVersion);
            }

        }

        private void addStateLabelAndButton(WorkflowMetaTypeVersion metaTypeVersion, Layout stateLayout) {
            Label stateLabel = new Label(getMessage("label.state") + ":");
            stateLabel.addStyleName(BennuTheme.LABEL_H3);
            stateLayout.addComponent(stateLabel);
            Label stateResult = null;
            if (metaTypeVersion.getPublished()) {
                stateResult = new Label("<em>" + getMessage("label.published") + "</em>");
            } else {
                stateResult = new Label("<em>" + getMessage("label.draft") + "</em>");
            }
            stateResult.setContentMode(Label.CONTENT_XHTML);
            stateLayout.addComponent(stateResult);
            //TODO (?or not seen this is backend) to be more user friendly, add a help explaining this
            //add the action icon to publish this (which is the same for the header)
            stateLayout.addComponent(generateVersionActionIconedButton(metaTypeVersion));

        }

        private void initVersionUnpublished(final WorkflowMetaTypeVersion metaTypeVersion) {
            mainLayout.setSpacing(true);

            //State layout
            HorizontalLayout stateLayout = new HorizontalLayout();
            stateLayout.setSpacing(true);

            //the state of the thing
            addStateLabelAndButton(metaTypeVersion, stateLayout);

            mainLayout.addComponent(stateLayout);

            //the publish form window
            final Window publishWindow = new Window(getMessage("label.publishVersion"));
            publishWindow.setModal(true);
            VerticalLayout publishWindowLayout = (VerticalLayout) publishWindow.getContent();
            publishWindowLayout.setSizeUndefined();

            //the publish form
            final TransactionalForm publishForm = new TransactionalForm(RESOURCE_BUNDLE);
            DomainItem<WorkflowMetaTypeVersion> metaTypeVersionItem = new DomainItem<WorkflowMetaTypeVersion>(metaTypeVersion);
            publishForm.setRequired(true);
            publishForm.setItemDataSource(metaTypeVersionItem);
            publishForm.setWriteThrough(false);
            publishForm.setInvalidCommitted(false);

            //	    publishForm.getField("publicationMotive").setRequired(true);

            publishForm.setVisibleItemProperties(Arrays.asList(new String[] { "publicationMotive" }));

            //adding the form to the modal window
            publishWindowLayout.addComponent(publishForm);

            //the apply and cancel buttons
            HorizontalLayout buttonsLayout = new HorizontalLayout();
            buttonsLayout.setSpacing(true);
            Button discardChangesButton = new Button(getMessage("label.cancel"), new Button.ClickListener() {

                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(ClickEvent event) {
                    publishForm.discard();
                    publishWindow.getParent().removeWindow(publishWindow);

                }
            });
            buttonsLayout.addComponent(discardChangesButton);

            Button applyChangesButton = new Button(getMessage("label.publish"), new Button.ClickListener() {

                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(ClickEvent event) {
                    publishForm.commit();
                    metaTypeVersion.setPublished(true);
                    //let's now remove all components and add them accordingly :)
                    if (metaTypeVersion.getPublished()) {
                        mainLayout.removeAllComponents();
                        initVersionPublished(metaTypeVersion);

                    }
                    if (publishWindow.getParent() != null) {
                        publishWindow.getParent().removeWindow(publishWindow);
                    }

                }
            });
            buttonsLayout.addComponent(applyChangesButton);
            publishWindowLayout.addComponent(buttonsLayout);

            //the button to publish the version
            Button publishButton = new Button(getMessage("label.publish"), new Button.ClickListener() {

                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(ClickEvent event) {
                    if (publishWindow.getParent() != null) {
                        getWindow().showNotification(getMessage("label.windowAlreadyOpen"));
                    } else {
                        getWindow().addWindow(publishWindow);
                    }

                }
            });
            publishButton.setStyleName(BennuTheme.BUTTON_SMALL);
            mainLayout.addComponent(publishButton);

        }

        private void initVersionPublished(WorkflowMetaTypeVersion metaTypeVersion) {
            mainLayout.setSpacing(true);

            //State layout
            HorizontalLayout stateLayout = new HorizontalLayout();
            stateLayout.setSpacing(true);

            //the state of the thing
            addStateLabelAndButton(metaTypeVersion, stateLayout);

            mainLayout.addComponent(stateLayout);

            //Date and user that published this
            HorizontalLayout publicationDetailsLayout = new HorizontalLayout();
            publicationDetailsLayout.setSpacing(true);

            Label publicationDateLabel = new Label(getMessage("label.publicationDate") + ":");
            publicationDateLabel.addStyleName(BennuTheme.LABEL_H3);
            publicationDetailsLayout.addComponent(publicationDateLabel);
            String dateOfPublication =
                    metaTypeVersion.getDatePublication() == null ? getMessage("label.publicationDateUnavailable") : metaTypeVersion
                            .getDatePublication().toString();
            Label publicationDateValue = new Label("<em>" + dateOfPublication + "</em>");
            publicationDateValue.setContentMode(Label.CONTENT_XHTML);
            publicationDetailsLayout.addComponent(publicationDateValue);
            Label userLabel = new Label(getMessage("label.user") + ":");
            userLabel.addStyleName(BennuTheme.LABEL_H3);
            String username =
                    metaTypeVersion.getPublisherOfVersion() == null ? null : metaTypeVersion.getPublisherOfVersion()
                            .getPresentationName();
            Label userValue = new Label("<em>" + StringUtils.defaultIfEmpty(username, getMessage("label.noUser")) + "</em>");
            userValue.setContentMode(Label.CONTENT_XHTML);
            publicationDetailsLayout.addComponent(userLabel);
            publicationDetailsLayout.addComponent(userValue);
            mainLayout.addComponent(publicationDetailsLayout);

            //the motive
            HorizontalLayout motiveLayout = new HorizontalLayout();
            motiveLayout.setSpacing(true);
            Label motiveLabel = new Label(getMessage("label.motive") + ":");
            motiveLabel.addStyleName(BennuTheme.LABEL_H3);
            motiveLayout.addComponent(motiveLabel);

            Label motiveValue =
                    new Label("<em>" + StringEscapeUtils.escapeHtml(metaTypeVersion.getPublicationMotive()) + "</em>");
            motiveValue.setContentMode(Label.CONTENT_XHTML);
            motiveLayout.addComponent(motiveValue);
            mainLayout.addComponent(motiveLayout);

            //statistics layout - nr Processes and nr States/Configs
            HorizontalLayout statisticsLayout = new HorizontalLayout();
            statisticsLayout.setSpacing(true);
            Label numberProcessesLabel = new Label(getMessage("label.numberProcesses") + ":");
            numberProcessesLabel.addStyleName(BennuTheme.LABEL_H3);
            statisticsLayout.addComponent(numberProcessesLabel);

            Label numberProcessesValue = new Label("<em>" + metaTypeVersion.getMetaProcessesCount() + "</em>");
            numberProcessesValue.setContentMode(Label.CONTENT_XHTML);
            statisticsLayout.addComponent(numberProcessesValue);

            Label numberStatesLabel = new Label(getMessage("label.numberStates") + ":");
            numberStatesLabel.addStyleName(BennuTheme.LABEL_H3);
            statisticsLayout.addComponent(numberStatesLabel);

            Label numberStatesValue = new Label("<em>" + metaTypeVersion.getProcessStatesCount() + "</em>");
            numberStatesValue.setContentMode(Label.CONTENT_XHTML);
            statisticsLayout.addComponent(numberStatesValue);

            Label numberConfigurationsLabel = new Label(getMessage("label.numberStateConfigs") + ":");
            numberConfigurationsLabel.addStyleName(BennuTheme.LABEL_H3);
            statisticsLayout.addComponent(numberConfigurationsLabel);

            int nrProcessConfigurations = 0;
            for (MetaProcessState processState : metaTypeVersion.getProcessStates()) {
                nrProcessConfigurations += processState.getConfigsCount();
            }
            Label numberConfigurationsValue = new Label("<em>" + nrProcessConfigurations + "</em>");
            numberConfigurationsValue.setContentMode(Label.CONTENT_XHTML);
            statisticsLayout.addComponent(numberConfigurationsValue);
            mainLayout.addComponent(statisticsLayout);

        }

    }

    private static String getMessage(String message, String... arguments) {
        return BundleUtil.getFormattedStringFromResourceBundle(RESOURCE_BUNDLE, message, arguments);
    }

}
