package module.metaWorkflow.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import module.metaWorkflow.exceptions.MetaWorkflowDomainException;
import pt.ist.fenixWebFramework.services.Service;

public class WorkflowMetaTypeVersion extends WorkflowMetaTypeVersion_Base implements Comparable<WorkflowMetaTypeVersion> {
    private static final Integer FIRST_VERSION = 1;

    public WorkflowMetaTypeVersion() {
	super();
	super.setVersion(FIRST_VERSION);
	super.setPublished(false);
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
	    super.setPublished(true);
	}

	setMetaType(metaType);

    }

    private WorkflowMetaTypeVersion(WorkflowMetaTypeVersion workflowMetaTypeVersion) {
	super();
	setVersion(workflowMetaTypeVersion.getVersion() + 1);
	if (workflowMetaTypeVersion.getMetaType() == null)
	    throw new MetaWorkflowDomainException(
		    "invalid.workflowMetaTypeVersion.given.which.hasnt.a.WorkflowMetaType.associated");
	setMetaType(workflowMetaTypeVersion.getMetaType());
	setPublished(false);
    }

    /* START: Protecting setters and getters section */
    @Override
    public void setMetaType(WorkflowMetaType metaType) {
	if (getMetaType() != null)
	    throw new MetaWorkflowDomainException("can.only.set.metaType.once");
	super.setMetaType(metaType);
    }

    @Override
    public void setVersion(Integer version) {
	throw new IllegalAccessError("protected method, not for direct use");
    }

    @Override
    public void setFieldSet(MetaFieldSet fieldSet) {
	if (getPublished())
	    throw new MetaWorkflowDomainException("cant.change.fieldSet.on.published.version");
	super.setFieldSet(fieldSet);
    }

    @Override
    public void setPublished(boolean published) {
	if (getPublished() && !published && hasAnyMetaProcesses())
	    throw new MetaWorkflowDomainException("cant.unpublish.with.existing.metaprocess.instances");
	super.setPublished(published);
    }

    @Override
    public void addMetaProcesses(WorkflowMetaProcess metaProcesses) {
	if (!getPublished())
	    throw new MetaWorkflowDomainException("cant.add.meta.processes.to.unpublished.metaTypeVersion");
	super.addMetaProcesses(metaProcesses);
    }

    @Override
    public void addProcessStates(MetaProcessState processStates) {
	if (getPublished())
	    throw new MetaWorkflowDomainException("cant.add.process.states.to.published.version");
	super.addProcessStates(processStates);
    }

    @Override
    public void removeFieldSet() {
	if (getPublished())
	    throw new MetaWorkflowDomainException("cant.change.fieldSet.on.published.version");
	super.removeFieldSet();
    }

    @Override
    public void removeMetaProcesses(WorkflowMetaProcess metaProcesses) {
	if (!getPublished())
	    throw new MetaWorkflowDomainException("shouldnt.be.removing.meta.processes.on.unpublished.metaTypeVersion");
	super.removeMetaProcesses(metaProcesses);
    }

    @Override
    public void removeMetaType() {
	throw new MetaWorkflowDomainException("cant.use.outside.only.on.delete.");
    }

    @Override
    public void removeProcessStates(MetaProcessState processStates) {
	if (getPublished())
	    throw new MetaWorkflowDomainException("cant.remove.meta.processes.in.published.metaTypeVersion");
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

    @Service
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

	    if (newProcessState == null)
		throw new MetaWorkflowDomainException("mps.copy.went.wrong");

	    workflowMetaTypeVersion.addProcessStates(newProcessState);

	    for (MetaProcessStateConfig config : oldMetaProcessState.getConfigs()) {
		//copy all of the configs and use the new MetaFields that were created
		MetaProcessStateConfig newConfig = new MetaProcessStateConfig(newProcessState);
		for (MetaField oldMetaField : config.getDependedFields()) {
		    MetaField newMetaField = oldToNewMFMap.get(oldMetaField);
		    if (newMetaField == null)
			throw new MetaWorkflowDomainException("illegal.state.no.new.mf.assigned.on.copying");

		    newConfig.addDependedFields(newMetaField);
		}

		//copy all of the depended states as well and use the new ones
		for (MetaProcessState oldDependedMPS : config.getDependedStates()) {
		    newConfig.addDependedStates(oldToNewMProcessStateMap.get(oldDependedMPS));
		}
	    }
	}
	return workflowMetaTypeVersion;
    }

    private void cloneMetaProcessStates(List<MetaProcessState> processStates,
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

}
