/*
 * @(#)QueueNameResolver.java
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
package module.metaWorkflow.util;

import java.util.HashMap;
import java.util.Map;

import module.workflow.domain.WorkflowQueue;
import pt.ist.bennu.core.util.BundleUtil;

// SWITCH MODULE

/**
 * 
 * @author Paulo Abrantes
 * 
 */
public class QueueNameResolver {

    private static Map<Class<? extends WorkflowQueue>, Resolver> nameMap = new HashMap<Class<? extends WorkflowQueue>, Resolver>();

    public static void registerType(Class<? extends WorkflowQueue> queueType, String bundle, String key) {
	nameMap.put(queueType, new Resolver(bundle, key));
    }

    public static String getNameFor(Class<? extends WorkflowQueue> queueType) {
	Resolver resolver = nameMap.get(queueType);
	return BundleUtil.getStringFromResourceBundle(resolver.getBundle(), resolver.getKey());
    }

    private static class Resolver {
	private String bundle;
	private String key;

	public Resolver(String bundle, String key) {
	    this.bundle = bundle;
	    this.key = key;
	}

	public String getBundle() {
	    return this.bundle;
	}

	public String getKey() {
	    return this.key;
	}
    }
}
