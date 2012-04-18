/*
 * @(#)WorkflowMetaType.java
 *
 * Copyright 2009 Instituto Superior Tecnico
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
package module.metaWorkflow.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import module.organization.domain.OrganizationalModel;
import module.workflow.domain.ProcessFile;
import module.workflow.domain.WorkflowQueue;
import module.workflow.domain.WorkflowSystem;
import myorg.domain.User;
import myorg.domain.exceptions.DomainException;
import myorg.util.BundleUtil;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.Strings;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * 
 * @author Pedro Santos
 * @author João Neves
 * @author Anil Kassamali
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class WorkflowMetaType extends WorkflowMetaType_Base {

    public WorkflowMetaType(String name, String description, OrganizationalModel model) {
	super();
	super.setWorkflowSystem(WorkflowSystem.getInstance());
	super.setName(name);
	super.setProcessCounter(0);
	super.setOrganizationalModel(model);
	addDescription(description, 1);
	super.setSuporttedFileClasses(new Strings(Collections.EMPTY_LIST));

	MultiLanguageString rootFieldSetName = new MultiLanguageString(Language.pt, BundleUtil.getStringFromResourceBundle(
		"resources/MetaWorkflowResources", "label.rootFieldSetPT")).with(Language.en,
		BundleUtil.getStringFromResourceBundle("resources/MetaWorkflowResources", "label.rootFieldSetEN"));
	super.setFieldSet(new MetaFieldSet(rootFieldSetName, 1));
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

    public void setFileClasses(List<Class<? extends ProcessFile>> classNames) {
	List<String> fileTypes = new ArrayList<String>();
	for (Class clazz : classNames) {
	    fileTypes.add(clazz.getName());
	}
	setSuporttedFileClasses(fileTypes);
    }

    public List<Class<? extends ProcessFile>> getFileClasses() {
	List<Class<? extends ProcessFile>> fileClasses = new ArrayList<Class<? extends ProcessFile>>();
	for (String className : getSuporttedFileClasses().getUnmodifiableList()) {
	    Class<? extends ProcessFile> clazz = null;
	    try {
		clazz = (Class<? extends ProcessFile>) Class.forName(className);
	    } catch (ClassNotFoundException e) {
		e.printStackTrace();
	    }
	    if (clazz != null) {
		fileClasses.add(clazz);
	    }

	}
	return fileClasses;
    }

    public WorkflowMetaProcess getProcess(String OID) {
	for (WorkflowMetaProcess process : getMetaProcesses()) {
	    if (process.getExternalId().equals(OID)) {
		return process;
	    }
	}
	return null;
    }

    public static WorkflowMetaType readMetaType(String OID) {
	for (WorkflowMetaType type : WorkflowSystem.getInstance().getMetaTypes()) {
	    if (type.getExternalId().equals(OID)) {
		return type;
	    }
	}
	return null;
    }

    @Service
    public static WorkflowMetaType createNewMetaType(String name, String description, OrganizationalModel model,
	    List<Class<? extends ProcessFile>> classNames) {
	WorkflowMetaType type = new WorkflowMetaType(name, description, model);
	type.setFileClasses(classNames);

	return type;
    }

    public FieldSetValue initValuesOfFields() {
	return getFieldSet().createFieldValue();
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
	for (String fileClass : getSuporttedFileClasses().getUnmodifiableList()) {
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

    @Override
    @Service
    public void addQueues(WorkflowQueue queues) {
	if (queues.getMetaType() != null) {
	    throw new DomainException("error.queue.already.has.metaType");
	}

	super.addQueues(queues);
    }

    @Override
    @Service
    public void removeQueues(WorkflowQueue queues) {
	super.removeQueues(queues);
    }

    public Set<WorkflowQueue> getQueuesForUser(User user) {
	Set<WorkflowQueue> queuesForUser = new HashSet<WorkflowQueue>();
	for (WorkflowQueue queue : getQueues()) {
	    if (queue.isUserAbleToAccessQueue(user)) {
		queuesForUser.add(queue);
	    }
	}
	return queuesForUser;
    }

    public static Set<WorkflowQueue> getAllQueuesForUser(User user) {
	Set<WorkflowQueue> queuesForUser = new HashSet<WorkflowQueue>();
	for (WorkflowMetaType type : WorkflowSystem.getInstance().getMetaTypes()) {
	    queuesForUser.addAll(type.getQueuesForUser(user));
	}
	return queuesForUser;
    }
}
