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

import module.metaWorkflow.exceptions.MetaWorkflowDomainException;
import module.organization.domain.OrganizationalModel;
import module.workflow.domain.ProcessFile;
import module.workflow.domain.WorkflowQueue;
import module.workflow.domain.WorkflowSystem;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.fenixframework.Atomic;
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

    protected WorkflowMetaType(String name, OrganizationalModel model, WorkflowMetaTypeVersion version) {
        super();
        super.setWorkflowSystem(WorkflowSystem.getInstance());
        super.setName(name);
        super.setProcessCounter(0);
        super.setOrganizationalModel(model);
        super.setSuporttedFileClasses(new Strings(Collections.EMPTY_LIST));

        MultiLanguageString rootFieldSetName =
                new MultiLanguageString(Language.pt, BundleUtil.getStringFromResourceBundle("resources/MetaWorkflowResources",
                        "label.rootFieldSetPT")).with(Language.en,
                        BundleUtil.getStringFromResourceBundle("resources/MetaWorkflowResources", "label.rootFieldSetEN"));
        super.setFieldSet(new MetaFieldSet(rootFieldSetName, 1));
        super.addVersions(version);
    }

    @Override
    public void setSuporttedFileClasses(Strings suporttedFileClasses) {
        throw new UnsupportedOperationException("error.use.setSupportedFileClasses.with.stringList");
    }

    @Atomic
    public void setSuporttedFileClasses(List<String> classes) {
        Strings classNames = new Strings(classes);
        super.setSuporttedFileClasses(classNames);
    }

    public WorkflowMetaTypeDescription getCurrentDescriptionOld() {
        return Collections.max(getDescriptions());
    }

    public String getCurrentDescription() {
        WorkflowMetaTypeVersion currentPublishedWMTVersion = getCurrentPublishedWMTVersion();
        return currentPublishedWMTVersion == null ? null : currentPublishedWMTVersion.getMetaTypeDescription();
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

    @Atomic
    public static WorkflowMetaType createNewMetaType(String name, String description, OrganizationalModel model,
            List<Class<? extends ProcessFile>> classNames) {
        WorkflowMetaTypeVersion workflowMetaTypeVersion = new WorkflowMetaTypeVersion(description);
        WorkflowMetaType type = new WorkflowMetaType(name, model, workflowMetaTypeVersion);
        type.setFileClasses(classNames);

        return type;
    }

    public void addDescription(String description) {
        getLatestWorkflowMetaTypeVersion().setMetaTypeDescription(description);
    }

    public FieldSetValue initValuesOfFields() {
        return getFieldSet().createFieldValue();
    }

    public int getCurrentPublishedVersionNumber() {
        return getCurrentPublishedWMTVersion().getVersion();
    }

    public List<MetaProcessState> getProcessStatesByOrder() {
        List<MetaProcessState> states = new ArrayList<MetaProcessState>(getProcessStates());
        Collections.sort(states, MetaProcessState.COMPARATOR_BY_POSITION);
        return states;
    }

    public List<WorkflowMetaTypeDescription> getOrderedDescriptionHistoryOld() {
        List<WorkflowMetaTypeDescription> list = new ArrayList<WorkflowMetaTypeDescription>(getDescriptions());
        Collections.sort(list);
        return Collections.unmodifiableList(list);
    }

    public List<WorkflowMetaTypeVersion> getOrderedDescriptionHistory() {
        List<WorkflowMetaTypeVersion> list = new ArrayList<WorkflowMetaTypeVersion>(getVersions());
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

    public WorkflowMetaTypeDescription getDescriptionAtVersionOld(int version) {
        List<WorkflowMetaTypeDescription> orderedDescriptionHistory = getOrderedDescriptionHistoryOld();
        return (orderedDescriptionHistory.size() < version) ? null : orderedDescriptionHistory.get(version - 1);
    }

    public String getDescriptionAtVersion(int version) {
        List<WorkflowMetaTypeVersion> orderedDescriptionHistory = getOrderedDescriptionHistory();
        return (orderedDescriptionHistory.size() < version) ? null : orderedDescriptionHistory.get(version - 1)
                .getMetaTypeDescription();
    }

    public Integer getNextIdentifier() {
        setProcessCounter(getProcessCounter() + 1);
        return getProcessCounter();
    }

    @Atomic
    public void removeObserver(User user) {
        super.removeMetaTypeObservers(user);
    }

    @Atomic
    public void addObserver(User user) {
        addMetaTypeObservers(user);
    }

    @Override
    @Atomic
    public void addQueues(WorkflowQueue queues) {
        if (queues.getMetaType() != null) {
            throw new DomainException("error.queue.already.has.metaType");
        }

        super.addQueues(queues);
    }

    @Override
    @Atomic
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

    /**
     * @author João Antunes
     * @return the latest {@link WorkflowMetaTypeVersion}, either published or
     *         unpublished
     */
    public WorkflowMetaTypeVersion getLatestWorkflowMetaTypeVersion() {
        return Collections.max(getVersions());
    }

    /**
     * @author João Antunes
     * @return the most recent published {@link WorkflowMetaTypeVersion}
     */
    public WorkflowMetaTypeVersion getCurrentPublishedWMTVersion() {
        WorkflowMetaTypeVersion metaTypeVersionToReturn = null;
        for (WorkflowMetaTypeVersion metaTypeVersion : getVersions()) {
            if (metaTypeVersion.getPublished()
                    && (metaTypeVersionToReturn == null || metaTypeVersion.getVersion() > metaTypeVersionToReturn.getVersion())) {
                metaTypeVersionToReturn = metaTypeVersion;
            }
        }
        return metaTypeVersionToReturn;
    }

    //FENIX-345: Migrated getter and setter methods

    //MetaProcesses START TODO: Sets and so on ?! (not really if not used)

    @Override
    public Set<WorkflowMetaProcess> getMetaProcesses() {
        Set<WorkflowMetaProcess> listMetaProcesses = new HashSet<WorkflowMetaProcess>();
        for (WorkflowMetaTypeVersion version : getVersions()) {
            listMetaProcesses.addAll(version.getMetaProcesses());
        }
        return listMetaProcesses;
    }

    //FENIX-345: TODO remove method after migration
    public Set<WorkflowMetaProcess> getMetaProcessesOld() {
        return super.getMetaProcesses();
    }

    @Override
    public void addMetaProcesses(WorkflowMetaProcess metaProcesses) {
        WorkflowMetaTypeVersion currentPublishedWMTVersion = getCurrentPublishedWMTVersion();
        if (currentPublishedWMTVersion == null) {
            throw new MetaWorkflowDomainException("there.are.no.published.versions.yet");
        }

        currentPublishedWMTVersion.addMetaProcesses(metaProcesses);
    }

    //FENIX-345: TODO remove method after migration
    public void addMetaProcessesOld(WorkflowMetaProcess metaProcesses) {
        super.addMetaProcesses(metaProcesses);
    }

    @Override
    public void removeMetaProcesses(WorkflowMetaProcess metaProcesses) {
        for (WorkflowMetaTypeVersion version : getVersions()) {
            version.removeMetaProcesses(metaProcesses);
        }
    }

    //FENIX-345: TODO remove method after migration
    public void removeMetaProcessesOld(WorkflowMetaProcess metaProcesses) {
        super.removeMetaProcesses(metaProcesses);
    }

    //MetaProcesses END

    //MetaProcessState START TODO: Sets and so on ?! (not really if not used)

    @Override
    public Set<MetaProcessState> getProcessStates() {
        return getProcessStates(getLatestWorkflowMetaTypeVersion());
    }

    public Set<MetaProcessState> getProcessStatesOfCurrentPublishedVersion() {
        return getProcessStates(getCurrentPublishedWMTVersion());
    }

    public Set<MetaProcessState> getProcessStates(WorkflowMetaTypeVersion version) {
        return version.getProcessStates();
    }

    //FENIX-345: TODO remove method after migration
    public Set<MetaProcessState> getProcessStatesOld() {
        return super.getProcessStates();
    }

    @Override
    public void addProcessStates(MetaProcessState processStates) {
        //by default let's add to the latest, if it is already published, then we have a problem
        //TODO maybe create a new unpublished version if the last version is published (?)
        addProcessStates(processStates, getLatestWorkflowMetaTypeVersion());
    }

    public void addProcessStates(MetaProcessState processState, WorkflowMetaTypeVersion version) {
        version.addProcessStates(processState);
    }

    //FENIX-345: TODO remove method after migration
    public void addProcessStatesOld(MetaProcessState processStates) {
        super.addProcessStates(processStates);
    }

    /**
     * Removes the {@link MetaProcessState} from the last version that is
     * available
     */
    @Override
    public void removeProcessStates(MetaProcessState processStates) {
        getLatestWorkflowMetaTypeVersion().removeProcessStates(processStates);
    }

    //FENIX-345: TODO remove method after migration
    public void removeProcessStatesOld(MetaProcessState processStates) {
        super.removeProcessStates(processStates);
    }

    //MetaProcessState END

    //MetaFieldSet START TODO (sets and all ? not really if not really used)
    @Override
    public MetaFieldSet getFieldSet() {
        return getLatestWorkflowMetaTypeVersion().getFieldSet();
    }

    //FENIX-345: TODO remove method after migration
    public MetaFieldSet getFieldSetOld() {
        return super.getFieldSet();
    }

    @Override
    public void setFieldSet(MetaFieldSet fieldSet) {
        getLatestWorkflowMetaTypeVersion().setFieldSet(fieldSet);
    }

    //FENIX-345: TODO remove method after migration
    public void setFieldSetOld(MetaFieldSet fieldSet) {
        super.setFieldSet(fieldSet);
    }

    //MetaFieldSet END

    //protecting the adds and removes of versions
    @Override
    public void addVersions(WorkflowMetaTypeVersion versions) {
        throw new MetaWorkflowDomainException("illegal.usage.of.version.creation.use."
                + "WorkflowMetaTypeVersion.createNewUnpublishedVersion");
    }

    /**
     * 
     * @return true if there is already a MetaTypeVersion on draft
     */
    public boolean hasDraftMetaTypeVersion() {
        return !getLatestWorkflowMetaTypeVersion().getPublished();
    }
}
