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

import jvstm.cps.ConsistencyPredicate;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * 
 * @author João Neves
 * @author David Martinho
 * 
 */
public class MetaProcessState extends MetaProcessState_Base {

    public MetaProcessState() {
        super();
    }
    
    public MetaProcessState(String name, Integer position) {
	this();
	setName(new MultiLanguageString(name));
	setPosition(position);
    }

    @ConsistencyPredicate
    public boolean checkHasMetaType() {
	return hasWorkflowMetaType();
    }


    public boolean isActive(WorkflowMetaProcess process) {
	for (MetaProcessStateConfig configuration : getConfigs()) {
	    if (configuration.isActive(process)) {
		return true;
	    }
	}
	return false;
    }
}
