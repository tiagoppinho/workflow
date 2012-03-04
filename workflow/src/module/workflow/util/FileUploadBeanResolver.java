/*
 * @(#)FileUploadBeanResolver.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Case Handleing Based Workflow Module.
 *
 *   The Case Handleing Based Workflow Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Workflow Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Workflow Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.workflow.util;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import module.workflow.domain.ProcessFile;
import module.workflow.domain.WorkflowProcess;

/**
 * 
 * @author Paulo Abrantes
 * 
 */
public class FileUploadBeanResolver {

    private static Map<Class<? extends ProcessFile>, Class<? extends WorkflowFileUploadBean>> beanMap = new HashMap<Class<? extends ProcessFile>, Class<? extends WorkflowFileUploadBean>>();

    @SuppressWarnings("unchecked")
    public static <T extends WorkflowFileUploadBean> T getBeanForProcessFile(WorkflowProcess process,
	    Class<? extends ProcessFile> clazz) {
	Class<? extends WorkflowFileUploadBean> beanClass = beanMap.get(clazz);
	if (beanClass == null) {
	    return (T) new WorkflowFileUploadBean(process);
	} else {
	    try {
		Constructor<? extends WorkflowFileUploadBean> constructor = beanClass.getConstructor(WorkflowProcess.class);
		return (T) constructor.newInstance(process);
	    } catch (Exception e) {
		e.printStackTrace();
		return (T) new WorkflowFileUploadBean(process);
	    }
	}
    }

    public static void registerBeanForProcessFile(Class<? extends ProcessFile> processFileClass,
	    Class<? extends WorkflowFileUploadBean> beanClass) {
	if (beanMap.get(processFileClass) != null) {
	    throw new RuntimeException("error.adding.already.existing.binding");
	}
	beanMap.put(processFileClass, beanClass);
    }

    public static void unregisterBeanForProcessFile(Class<? extends ProcessFile> processFileClass) {
	beanMap.remove(processFileClass);
    }
}
