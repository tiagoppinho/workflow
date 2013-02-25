package module.metaWorkflow.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import module.metaWorkflow.exceptions.MetaWorkflowDomainException;
import module.metaWorkflow.util.versioning.DiffUtil;
import module.metaWorkflow.util.versioning.DiffUtil.Revision;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.fenixframework.Atomic;

public class WorkflowMetaTypeVersion extends WorkflowMetaTypeVersion_Base implements Comparable<WorkflowMetaTypeVersion> {
    private static final Integer FIRST_VERSION = 1;

    public WorkflowMetaTypeVersion() {
        super();
        super.setVersion(FIRST_VERSION);
        super.setPublished(false);
    }

    public WorkflowMetaTypeVersion(String description) {
        this();
        setMetaTypeDescription(description);
    }

    /**
     * Note: Deprecated, use the TODO
     */
    @Deprecated
    public WorkflowMetaTypeVersion(WorkflowMetaType metaType) {
        this();

        /*
         * if the given metaType doesn't have any versions, then let's
         * initialize this instance with the fields of the old relations with
         * WorkflowMetaType
         */

        if (!metaType.hasAnyVersions()) {
            for (MetaProcessState processState : metaType.getProcessStatesOld()) {
                super.addProcessStates(processState);
            }

            for (WorkflowMetaProcess metaProcess : metaType.getMetaProcessesOld()) {
                super.addMetaProcesses(metaProcess);
            }

            super.setFieldSet(metaType.getFieldSetOld());
            super.setMetaTypeDescription(metaType.getCurrentDescriptionOld().getDescription());
        }

        setMetaType(metaType);

    }

    private WorkflowMetaTypeVersion(WorkflowMetaTypeVersion workflowMetaTypeVersion) {
        super();
        super.setVersion(workflowMetaTypeVersion.getVersion() + 1);
        if (workflowMetaTypeVersion.getMetaType() == null) {
            throw new MetaWorkflowDomainException(
                    "invalid.workflowMetaTypeVersion.given.which.hasnt.a.WorkflowMetaType.associated");
        }
        setMetaType(workflowMetaTypeVersion.getMetaType());
        super.setPublished(false);
        setMetaTypeDescription(workflowMetaTypeVersion.getMetaTypeDescription());
    }

    /**
     * 
     * @param publicationMotive
     *            the motive of this version, why it was created
     */
    public void publish(String publicationMotive) {
        if (getPublished()) {
            throw new MetaWorkflowDomainException("already.published");
        }
        super.setPublisherOfVersion(UserView.getCurrentUser());
        super.setPublicationMotive(publicationMotive);
        super.setDatePublication(new DateTime());
        super.setPublished(true);
    }

    /* START: Protecting setters and getters section */

    @Override
    public void setMetaTypeDescription(String metaTypeDescription) {
        if (getPublished()) {
            throw new MetaWorkflowDomainException("cant.change.metaTypeDescription.on.published.version");
        }
        super.setMetaTypeDescription(metaTypeDescription);
    }

    //ignore the publisherOfVersion
    @Override
    public void setPublisherOfVersion(pt.ist.bennu.core.domain.User publisherOfVersion) {
        if (getPublished()) {
            throw new MetaWorkflowDomainException("cant.change.metaTypeDescription.on.published.version");
        }
        super.setPublisherOfVersion(UserView.getCurrentUser());
    }

    @Override
    public void setPublicationMotive(String publicationMotive) {
        if (getPublished()) {
            throw new MetaWorkflowDomainException("cant.change.metaTypeDescription.on.published.version");
        }
        super.setPublicationMotive(publicationMotive);
    }

    @Override
    public void setDatePublication(DateTime datePublication) {
        if (getPublished()) {
            throw new MetaWorkflowDomainException("cant.change.metaTypeDescription.on.published.version");
        }
        super.setDatePublication(datePublication);
    }

    @Override
    public void setMetaType(WorkflowMetaType metaType) {
        if (getMetaType() != null) {
            throw new MetaWorkflowDomainException("can.only.set.metaType.once");
        }
        super.setMetaType(metaType);
    }

    @Override
    public void setVersion(Integer version) {
        throw new MetaWorkflowDomainException("protected method, not for direct use");
    }

    @Override
    public void setFieldSet(MetaFieldSet fieldSet) {
        if (getPublished()) {
            throw new MetaWorkflowDomainException("cant.change.fieldSet.on.published.version");
        }
        super.setFieldSet(fieldSet);
    }

    @Override
    @Atomic
    public void setPublished(boolean published) {
        if (!published) {
            throw new IllegalArgumentException("can't unpublish a version");
        }
        if (StringUtils.isEmpty(getPublicationMotive())) {
            throw new MetaWorkflowDomainException("must.have.a.motive");
        }
        publish(getPublicationMotive());
    }

    @Override
    public void addMetaProcesses(WorkflowMetaProcess metaProcesses) {
        if (!getPublished()) {
            throw new MetaWorkflowDomainException("cant.add.meta.processes.to.unpublished.metaTypeVersion");
        }
        super.addMetaProcesses(metaProcesses);
    }

    @Override
    public void addProcessStates(MetaProcessState processStates) {
        if (getPublished()) {
            throw new MetaWorkflowDomainException("cant.add.process.states.to.published.version");
        }
        super.addProcessStates(processStates);
    }

    @Override
    public void removeFieldSet() {
        if (getPublished()) {
            throw new MetaWorkflowDomainException("cant.change.fieldSet.on.published.version");
        }
        super.removeFieldSet();
    }

    @Override
    public void removeMetaProcesses(WorkflowMetaProcess metaProcesses) {
        if (!getPublished()) {
            throw new MetaWorkflowDomainException("shouldnt.be.removing.meta.processes.on.unpublished.metaTypeVersion");
        }
        super.removeMetaProcesses(metaProcesses);
    }

    @Override
    public void removeMetaType() {
        throw new MetaWorkflowDomainException("cant.use.outside.only.on.delete.");
    }

    @Override
    public void removeProcessStates(MetaProcessState processStates) {
        if (getPublished()) {
            throw new MetaWorkflowDomainException("cant.remove.meta.processes.in.published.metaTypeVersion");
        }
        super.removeProcessStates(processStates);
    }

    @Override
    public void removeWorkflowSystem() {
        throw new MetaWorkflowDomainException("cant.use.outside.only.on.delete.");
    }

    /* END Protecting setters and getters section */

    private void cloneMetaField(MetaField toClone, Map<MetaField, MetaField> oldNewMap) {
        if (oldNewMap.get(toClone) == null) {
            //clone it
            MetaField newMetaField = toClone.duplicatedMetaField();
            oldNewMap.put(toClone, newMetaField);
            if (toClone instanceof MetaFieldSet) {
                MetaFieldSet oldMetaFieldSet = (MetaFieldSet) toClone;
                MetaFieldSet newMetaFieldSet = (MetaFieldSet) newMetaField;
                for (MetaField childField : oldMetaFieldSet.getChildFields()) {
                    cloneMetaField(childField, oldNewMap);
                    //let's also set the children of this new MetaField
                    newMetaFieldSet.addChildFields(oldNewMap.get(childField));

                }
            }
        }

    }

    @Atomic
    public WorkflowMetaTypeVersion createNewUnpublishedVersion() {
        WorkflowMetaTypeVersion workflowMetaTypeVersion = new WorkflowMetaTypeVersion(this);

        //let's create a Map for the old and the new MetaField to be used to create the deep clone
        HashMap<MetaField, MetaField> oldToNewMFMap = new HashMap<MetaField, MetaField>();

        //also for the MetaProcessState, so that we can use that for the configurations
        HashMap<MetaProcessState, MetaProcessState> oldToNewMProcessStateMap = new HashMap<MetaProcessState, MetaProcessState>();

        //let's clone all of the metafields directly and indirectly connected to this version
        cloneMetaField(getFieldSet(), oldToNewMFMap);
        workflowMetaTypeVersion.setFieldSet((MetaFieldSet) oldToNewMFMap.get(getFieldSet()));

        //let's clone all of the MetaProcessState(s)
        cloneMetaProcessStates(getProcessStates(), oldToNewMProcessStateMap);

        //let's copy all of the MetaProcessState objects
        for (MetaProcessState oldMetaProcessState : getProcessStates()) {

            //get the new version of MetaProcessState
            MetaProcessState newProcessState = oldToNewMProcessStateMap.get(oldMetaProcessState);

            if (newProcessState == null) {
                throw new MetaWorkflowDomainException("mps.copy.went.wrong");
            }

            workflowMetaTypeVersion.addProcessStates(newProcessState);

            for (MetaProcessStateConfig config : oldMetaProcessState.getConfigs()) {
                //copy all of the configs and use the new MetaFields that were created
                MetaProcessStateConfig newConfig = new MetaProcessStateConfig(newProcessState);
                for (MetaField oldMetaField : config.getDependedFields()) {
                    MetaField newMetaField = oldToNewMFMap.get(oldMetaField);
                    if (newMetaField == null) {
                        throw new MetaWorkflowDomainException("illegal.state.no.new.mf.assigned.on.copying");
                    }

                    newConfig.addDependedFields(newMetaField);
                }

                //copy all of the depended states as well and use the new ones
                for (MetaProcessState oldDependedMPS : config.getDependedStates()) {
                    MetaProcessState metaProcessState = oldToNewMProcessStateMap.get(oldDependedMPS);
                    if (metaProcessState == null) {
                        throw new MetaWorkflowDomainException("illegal.state.no.new.metaProcessState.assigned.on.copying");
                    }
                    newConfig.addDependedStates(oldToNewMProcessStateMap.get(oldDependedMPS));
                }
            }
        }
        return workflowMetaTypeVersion;
    }

    private void cloneMetaProcessStates(Collection<MetaProcessState> processStates,
            HashMap<MetaProcessState, MetaProcessState> oldToNewMProcessStateMap) {
        for (MetaProcessState oldProcessState : processStates) {
            MetaProcessState newProcessState = oldToNewMProcessStateMap.get(oldProcessState);
            if (newProcessState == null) {
                newProcessState = new MetaProcessState(oldProcessState.getName(), oldProcessState.getPosition());
                oldToNewMProcessStateMap.put(oldProcessState, newProcessState);
            }
        }

    }

    @Override
    public int compareTo(WorkflowMetaTypeVersion otherMetaTypeVersion) {
        return Integer.valueOf(getVersion().compareTo(otherMetaTypeVersion.getVersion()));
    }

    /*
     * metaWorkflowDescription - ex WorkflowMetaTypeDescription methods
     */

    public Revision getDiffWithVersion(int version) {
        return getDiffWith(getMetaType().getDescriptionAtVersion(version - 1));
    }

    public Revision getDiffWith(String description) {
        return DiffUtil.diff(getMetaTypeDescription(), description);
    }

    /**
     * Deletes the metaTypeVersion and all of the associated {@link MetaProcessState}, {@link MetaProcessStateConfig}, and
     * {@link MetaField} as long as it is a draft version
     */
    @Atomic
    public void delete() {
        if (getPublished()) {
            throw new MetaWorkflowDomainException("delete.published.WorkflowMetaTypeVersion.error");
        }

        //order should be: configs before states; and fields

        for (MetaProcessState processState : getProcessStates()) {
            for (MetaProcessStateConfig config : processState.getConfigs()) {
                config.delete();
            }
        }
        for (MetaProcessState processState : getProcessStates()) {
            processState.delete();
        }

        getFieldSet().deleteItselfAndAllChildren();

        super.setMetaType(null);

        deleteDomainObject();

    }
}
