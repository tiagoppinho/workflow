/**
 * 
 */
package module.metaWorkflow.presentationTier.vaadin;

import java.util.List;
import java.util.Map;

import module.metaWorkflow.domain.WorkflowMetaProcess;
import module.metaWorkflow.domain.WorkflowMetaType;
import module.metaWorkflow.domain.WorkflowMetaTypeVersion;
import module.vaadin.ui.BennuTheme;
import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.domain.RoleType;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.vaadinframework.EmbeddedApplication;
import pt.ist.vaadinframework.annotation.EmbeddedComponent;
import pt.ist.vaadinframework.data.reflect.DomainContainer;
import pt.ist.vaadinframework.ui.EmbeddedComponentContainer;
import pt.ist.vaadinframework.ui.TransactionalTable;

import com.vaadin.data.Property;
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
 * @author João Antunes (joao.antunes@tagus.ist.utl.pt) - 18 de Jun de 2012
 * 
 *         Used to manage a specific MetaTypeComponent - it renders the several
 *         versions of the parameters/description, as well as the links to
 *         manage the associated queues and filetypes
 * 
 */
@EmbeddedComponent(path = { "manageMetaTypeComponent" }, args = { "metaType" })
public class ManageMetaTypeComponent extends CustomComponent implements EmbeddedComponentContainer {

    /**
     * default serial version
     */
    private static final long serialVersionUID = 1L;
    private static final String RESOURCE_BUNDLE = "resources/MetaWorkflowResources";

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
        WorkflowMetaType metaType = FenixFramework.getDomainObject(arg0.get("metaType"));
        manageMetaTypeInterface(metaType);

    }

    /**
     * Interface - creates the whole interface
     * 
     * @param metaType
     */
    private void manageMetaTypeInterface(WorkflowMetaType metaType) {
        VerticalLayout layout = new VerticalLayout();
        setCompositionRoot(layout);
        layout.setSpacing(true);

        Label title = new Label(getMessage("label.ManageMetaTypeComponent.title.with.metaTypeName", metaType.getName()));
        title.addStyleName(BennuTheme.LABEL_H2);
        layout.addComponent(title);

        //let's add the back link button
        Button backButton = new Button(getMessage("label.back"));
        backButton.setStyleName(BennuTheme.BUTTON_LINK);
        backButton.setDescription(getMessage("tooltip.back.onManageMetaTypeComponent"));
        backButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                EmbeddedApplication.back(getApplication());

            }
        });

        layout.addComponent(backButton);

        Label versions = new Label(getMessage("label.ManageMetaTypeComponent.versions") + ":");
        versions.addStyleName(BennuTheme.LABEL_H3);
        layout.addComponent(versions);

        Table table = new TransactionalTable(RESOURCE_BUNDLE) {
            @Override
            protected String formatPropertyValue(Object rowId, Object colId, Property property) {
                if ("version".equals(colId)) {
                    return getMessage("label.metaType.version") + " " + property.getValue();
                }
                return super.formatPropertyValue(rowId, colId, property);
            }
        };
        DomainContainer<WorkflowMetaTypeVersion> container =
                new DomainContainer<WorkflowMetaTypeVersion>(metaType.getVersions(), WorkflowMetaTypeVersion.class);
        container.setContainerProperties("version", "metaProcesses");

        table.setContainerDataSource(container);

        table.addGeneratedColumn("version", new ColumnGenerator() {

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                HorizontalLayout horizontalLayout = new HorizontalLayout();
                horizontalLayout.setSpacing(true);

                final WorkflowMetaTypeVersion metaTypeVersion = (WorkflowMetaTypeVersion) itemId;
                Property prop = source.getItem(itemId).getItemProperty(columnId);

                Button statesButton =
                        new Button(getMessage("label.metaType.version") + " " + prop.getValue(), new Button.ClickListener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void buttonClick(ClickEvent event) {
                                EmbeddedApplication.open(getApplication(), ManageMetaTypeVersionComponent.class,
                                        metaTypeVersion.getExternalId());
                            }
                        });
                statesButton.addStyleName(BaseTheme.BUTTON_LINK);
                horizontalLayout.addComponent(statesButton);
                return horizontalLayout;

            }
        });
        //	table.setColumnHeader("metaProcessesCount")
        table.addGeneratedColumn("metaProcesses", new ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                WorkflowMetaTypeVersion metaTypeVersion = (WorkflowMetaTypeVersion) itemId;
                if (!metaTypeVersion.getPublished()) {
                    Label draftLabel = new Label("<p>" + getMessage("ManageMetaTypeComponent.label.draft") + "</p>");
                    draftLabel.setContentMode(Label.CONTENT_XHTML);
                    return draftLabel;
                } else {
                    Property prop = source.getItem(itemId).getItemProperty(columnId);
                    @SuppressWarnings("unchecked")
                    List<WorkflowMetaProcess> workflowMetaProcesses = (List<WorkflowMetaProcess>) prop.getValue();
                    return new Label(String.valueOf(workflowMetaProcesses.size()));
                }
            }
        });

        layout.addComponent(table);

    }

    private String getMessage(String message, String... arguments) {
        return BundleUtil.getFormattedStringFromResourceBundle(RESOURCE_BUNDLE, message, arguments);
    }

}
