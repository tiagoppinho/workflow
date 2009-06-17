/*
 * @(#)GenericFile.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: João Figueiredo, Luis Cruz, Paulo Abrantes, Susana Fernandes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the MyOrg web application infrastructure.
 *
 *   MyOrg is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.*
 *
 *   MyOrg is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with MyOrg. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package module.workflow.domain;

import module.workflow.util.FileTypeNameResolver;
import module.workflow.util.WorkflowFileUploadBean;
import myorg.domain.MyOrg;

public class GenericFile extends GenericFile_Base {

    static {
	FileTypeNameResolver.registerType(GenericFile.class, "resources/WorkflowResources",
		"label.module.workflow.domain.GenericFile");
    }

    public GenericFile() {
	super();
	this.setOjbConcreteClass(getClass().getName());
	this.setMyOrg(MyOrg.getInstance());
    }

    public GenericFile(String displayName, String filename, byte[] content) {
	this();
	setDisplayName(displayName);
	setFilename(filename);
	setContent(new FileContent(content));
    }

    public <T extends WorkflowFileUploadBean> void fillInNonDefaultFields(T bean) {

    }

    public <T extends WorkflowProcess> boolean validUpload(T workflowProcess) {
	return true;
    }
}
