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

import java.util.HashSet;
import java.util.Set;

import jvstm.cps.ConsistencyPredicate;
import pt.ist.fenixWebFramework.services.Service;

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

	return definedMetaFields.contains(getDependedFields());
    }

    @Service
    public static MetaProcessStateConfig create(MetaProcessState state) {
	return new MetaProcessStateConfig(state);
    }

    @Service
    public void delete() {
	for (MetaField field : getDependedFields()) {
	    removeDependedFields(field);
	}
	for (MetaProcessState dependedState : getDependedStates()) {
	    removeDependedStates(dependedState);
	}
	removeMetaProcessState();

	deleteDomainObject();
    }

    @Service
    @Override
    public void addDependedStates(MetaProcessState dependedStates) {
	super.addDependedStates(dependedStates);
    }
}
