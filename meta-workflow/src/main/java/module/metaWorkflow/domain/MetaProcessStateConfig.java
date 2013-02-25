/*
 * @(#)MetaProcessStateConfig.java
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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import jvstm.cps.ConsistencyPredicate;
import module.metaWorkflow.exceptions.MetaWorkflowDomainException;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author João Neves
 * @author David Martinho
 * 
 */
public class MetaProcessStateConfig extends MetaProcessStateConfig_Base {

    public MetaProcessStateConfig() {
        super();
    }

    public MetaProcessStateConfig(MetaProcessState state) {
        this();
        setMetaProcessState(state);
    }

    @ConsistencyPredicate
    public boolean checkHasState() {
        return hasMetaProcessState();
    }

    public boolean isActive(WorkflowMetaProcess process) {
        if (getDependedStates().isEmpty() && getDependedFields().isEmpty()) {
            return true;
        }

        for (MetaProcessState state : getDependedStates()) {
            if (!state.isActive(process)) {
                return false;
            }
        }

        Set<MetaField> definedMetaFields = new HashSet<MetaField>();
        for (FieldValue value : process.getAllFields()) {
            if (value.isDefined()) {
                definedMetaFields.add(value.getMetaField());
            }
        }

        return definedMetaFields.containsAll(getDependedFields());
    }

    /* TODO START: protection against published things FENIX-345: */
    @Atomic
    public static MetaProcessStateConfig create(MetaProcessState state) {
        return new MetaProcessStateConfig(state);
    }

    @Atomic
    public void delete() {
        if (isPublished()) {
            throw new MetaWorkflowDomainException("cant.delete.a.published.state.config");
        }
        for (MetaField field : getDependedFields()) {
            removeDependedFields(field);
        }
        for (MetaProcessState dependedState : getDependedStates()) {
            removeDependedStates(dependedState);
        }
        removeMetaProcessState();

        deleteDomainObject();
    }

    /**
     * 
     * @return true if the {@link WorkflowMetaTypeVersion} associated with this
     *         config is published or any of the dependent MetaProcessStates
     *         NOTE: the depended MetaProcessStates should all belong to a
     *         version, but we check for the case it doesn't. Just in case..
     */
    public boolean isPublished() {
        for (MetaProcessState dependedProcessState : getDependedStates()) {
            if (dependedProcessState.isPublished()) {
                return true;
            }
        }
        return getMetaProcessState().isPublished();

    }

    @Atomic
    @Override
    public void addDependedStates(MetaProcessState dependedStates) {
        super.addDependedStates(dependedStates);
    }

    @Atomic
    public void updateDependedStates(Collection<MetaProcessState> newStates) {
        Collection<MetaProcessState> oldStates = getDependedStates();
        if (!oldStates.containsAll(newStates) || !newStates.containsAll(oldStates)) {
            //There are changes to make
            Collection<MetaProcessState> statesToRem = new HashSet<MetaProcessState>();
            statesToRem.addAll(oldStates);
            statesToRem.removeAll(newStates);
            for (MetaProcessState stateToRem : statesToRem) {
                removeDependedStates(stateToRem);
            }

            Collection<MetaProcessState> statesToAdd = new HashSet<MetaProcessState>();
            statesToAdd.addAll(newStates);
            statesToAdd.removeAll(oldStates);
            for (MetaProcessState stateToAdd : statesToAdd) {
                addDependedStates(stateToAdd);
            }
        }
    }

    @Atomic
    public void updateDependedFields(Collection<MetaField> newFields) {
        Collection<MetaField> oldFields = getDependedFields();
        if (!oldFields.containsAll(newFields) || !newFields.containsAll(oldFields)) {
            //There are changes to make
            Collection<MetaField> fieldsToRem = new HashSet<MetaField>();
            fieldsToRem.addAll(oldFields);
            fieldsToRem.removeAll(newFields);
            for (MetaField stateToRem : fieldsToRem) {
                removeDependedFields(stateToRem);
            }

            Collection<MetaField> fieldsToAdd = new HashSet<MetaField>();
            fieldsToAdd.addAll(newFields);
            fieldsToAdd.removeAll(oldFields);
            for (MetaField stateToAdd : fieldsToAdd) {
                addDependedFields(stateToAdd);
            }
        }

    }
}
