/*
 * @(#)MetaProcessState.java
 *
 * Copyright 2012 Instituto Superior Tecnico
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

import java.util.Comparator;

import jvstm.cps.ConsistencyPredicate;
import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * 
 * @author João Neves
 * @author David Martinho
 * 
 */
public class MetaProcessState extends MetaProcessState_Base {

    public static Comparator<MetaProcessState> COMPARATOR_BY_POSITION = new Comparator<MetaProcessState>() {
        @Override
        public int compare(MetaProcessState state1, MetaProcessState state2) {
            int orderComp = state1.getPosition().compareTo(state2.getPosition());
            return (orderComp != 0) ? orderComp : COMPARATOR_BY_NAME.compare(state1, state2);
        }
    };

    public static Comparator<MetaProcessState> COMPARATOR_BY_NAME = new Comparator<MetaProcessState>() {
        @Override
        public int compare(MetaProcessState state1, MetaProcessState state2) {
            int nameComp = state1.getName().getContent().compareTo(state2.getName().getContent());
            return (nameComp != 0) ? nameComp : state1.getExternalId().compareTo(state2.getExternalId());
        }
    };

    public MetaProcessState() {
        super();
    }

    public MetaProcessState(String name, Integer position) {
        this();
        setName(new MultiLanguageString(name));
        setPosition(position);
    }

    public MetaProcessState(WorkflowMetaTypeVersion metaTypeVersion, String name, Integer position) {
        this(name, position);
        setWorkflowMetaTypeVersion(metaTypeVersion);
    }

    public MetaProcessState(WorkflowMetaTypeVersion metaTypeVersion, MultiLanguageString name, Integer position) {
        this();
        setName(name);
        setPosition(position);
        setWorkflowMetaTypeVersion(metaTypeVersion);
    }

    public MetaProcessState(MultiLanguageString name, Integer position) {
        this();
        setName(name);
        setPosition(position);
    }

    @ConsistencyPredicate
    public boolean checkHasMetaTypeVersion() {
        return hasWorkflowMetaTypeVersion();
    }

    public boolean isActive(WorkflowMetaProcess process) {
        for (MetaProcessStateConfig configuration : getConfigs()) {
            if (configuration.isActive(process)) {
                return true;
            }
        }
        return false;
    }

    /*
     * TODO START: protection against published things FENIX-345:
     * 
     * methods TODO: addConfigs addDependingConfigs
     */
    @Atomic
    public static MetaProcessState create(WorkflowMetaTypeVersion metaTypeVersion, String name, Integer position) {
        return new MetaProcessState(metaTypeVersion, name, position);
    }

    @Atomic
    public void delete() {
        if (hasAnyDependingConfigs()) {
            throw new DomainException("error.state.has.depending.states");
        }
        for (MetaProcessStateConfig config : getConfigs()) {
            config.delete();
        }
        removeWorkflowMetaType();
        removeWorkflowMetaTypeVersion();
        deleteDomainObject();
    }

    @Override
    public WorkflowMetaType getWorkflowMetaType() {
        return getWorkflowMetaTypeVersion().getMetaType();
    }

    public WorkflowMetaType getWorkflowMetaTypeOld() {
        return super.getWorkflowMetaType();
    }

    /**
     * 
     * @return true if the WorkflowMetaTypeVersion associated with this instance
     *         is published
     */
    public boolean isPublished() {
        return getWorkflowMetaTypeVersion().getPublished();

    }

}
