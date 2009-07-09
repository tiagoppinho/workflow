package module.metaWorkflow.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import module.workflow.domain.GenericFile;

public class WorkflowMetaTypeBean implements Serializable {

    String name;
    String description;
    List<Class<? extends GenericFile>> classNames;
    
    public List<Class<? extends GenericFile>> getFileClassNames() {
        return classNames;
    }

    public void setFileClassNames(List<Class<? extends GenericFile>> classNames) {
        this.classNames = classNames;
    }

    public WorkflowMetaTypeBean() {
	this(null, null);
    }

    public WorkflowMetaTypeBean(String name, String description) {
	setName(name);
	setDescription(description);
	setFileClassNames(new ArrayList<Class<? extends GenericFile>>());
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
