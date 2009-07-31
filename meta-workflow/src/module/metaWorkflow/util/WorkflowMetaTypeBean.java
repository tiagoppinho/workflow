package module.metaWorkflow.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import module.organization.domain.OrganizationalModel;
import module.workflow.domain.ProcessFile;
import pt.ist.fenixWebFramework.util.DomainReference;

public class WorkflowMetaTypeBean implements Serializable {

    private String name;
    private String description;
    private List<Class<? extends ProcessFile>> classNames;
    private DomainReference<OrganizationalModel> organizationModel;

    public OrganizationalModel getOrganizationModel() {
	return organizationModel.getObject();
    }

    public void setOrganizationModel(OrganizationalModel organizationModel) {
	this.organizationModel = new DomainReference<OrganizationalModel>(organizationModel);
    }

    public List<Class<? extends ProcessFile>> getFileClassNames() {
	return classNames;
    }

    public void setFileClassNames(List<Class<? extends ProcessFile>> classNames) {
	this.classNames = classNames;
    }

    public WorkflowMetaTypeBean() {
	this(null, null);
    }

    public WorkflowMetaTypeBean(String name, String description) {
	setName(name);
	setDescription(description);
	setFileClassNames(new ArrayList<Class<? extends ProcessFile>>());
	setOrganizationModel(null);
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }
}
