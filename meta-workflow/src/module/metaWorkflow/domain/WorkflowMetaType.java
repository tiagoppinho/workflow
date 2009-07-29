package module.metaWorkflow.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import module.fileSupport.domain.GenericFile;
import module.workflow.domain.ProcessFile;
import myorg.domain.MyOrg;
import myorg.domain.User;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.Strings;

public class WorkflowMetaType extends WorkflowMetaType_Base {

    public WorkflowMetaType(String name, String description) {
	super();
	setMyOrg(MyOrg.getInstance());
	setName(name);
	setProcessCounter(0);
	addDescription(description, 1);
	super.setSuporttedFileClasses(new Strings(Collections.EMPTY_LIST));
    }

    @Override
    public void setSuporttedFileClasses(Strings suporttedFileClasses) {
	throw new UnsupportedOperationException("error.use.setSupportedFileClasses.with.stringList");
    }

    @Service
    public void setSuporttedFileClasses(List<String> classes) {
	Strings classNames = new Strings(classes);
	super.setSuporttedFileClasses(classNames);
    }

    public WorkflowMetaTypeDescription getCurrentDescription() {
	return Collections.max(getDescriptions());
    }

    @Service
    public static void createNewMetaType(String name, String description, List<Class<? extends GenericFile>> classNames) {
	WorkflowMetaType type = new WorkflowMetaType(name, description);
	List<String> fileTypes = new ArrayList<String>();
	for (Class clazz : classNames) {
	    fileTypes.add(clazz.getName());
	}
	type.setSuporttedFileClasses(fileTypes);
    }

    public void addDescription(String description) {
	addDescription(description, getCurrentVersion() + 1);
    }

    private void addDescription(String description, int version) {
	new WorkflowMetaTypeDescription(this, description, version);
    }

    public int getCurrentVersion() {
	WorkflowMetaTypeDescription description = getCurrentDescription();
	return description == null ? 1 : description.getVersion();
    }

    public List<WorkflowMetaTypeDescription> getOrderedDescriptionHistory() {
	List<WorkflowMetaTypeDescription> list = new ArrayList<WorkflowMetaTypeDescription>(getDescriptions());
	Collections.sort(list);
	return Collections.unmodifiableList(list);
    }

    public List<Class<? extends ProcessFile>> getAvailableFileTypes() {
	List<Class<? extends ProcessFile>> classes = new ArrayList<Class<? extends ProcessFile>>();
	for (String fileClass : getSuporttedFileClasses()) {
	    try {
		Class<? extends ProcessFile> clazz = (Class<? extends ProcessFile>) Class.forName(fileClass);
		classes.add(clazz);
	    } catch (ClassNotFoundException e) {
		e.printStackTrace();
		// ignore error
	    }
	}

	return classes;
    }

    public WorkflowMetaTypeDescription getDescriptionAtVersion(int version) {
	List<WorkflowMetaTypeDescription> orderedDescriptionHistory = getOrderedDescriptionHistory();
	return (orderedDescriptionHistory.size() < version) ? null : orderedDescriptionHistory.get(version - 1);
    }

    public Integer getNextIdentifier() {
	setProcessCounter(getProcessCounter() + 1);
	return getProcessCounter();
    }

    @Service
    public void removeObserver(User user) {
	super.removeMetaTypeObservers(user);
    }

    @Service
    public void addObserver(User user) {
	addMetaTypeObservers(user);
    }
}
