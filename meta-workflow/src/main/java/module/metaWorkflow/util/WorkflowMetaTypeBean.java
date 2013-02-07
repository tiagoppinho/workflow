/*
 * @(#)WorkflowMetaTypeBean.java
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import module.organization.domain.OrganizationalModel;
import module.workflow.domain.ProcessFile;

/**
 * 
 * @author Pedro Santos
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class WorkflowMetaTypeBean implements Serializable {

    private String name;
    private String description;
    private List<Class<? extends ProcessFile>> classNames;
    private OrganizationalModel organizationModel;

    public OrganizationalModel getOrganizationModel() {
        return organizationModel;
    }

    public void setOrganizationModel(OrganizationalModel organizationModel) {
        this.organizationModel = organizationModel;
    }

    public List<Class<? extends ProcessFile>> getFileClassNames() {
        return classNames;
    }

    public void setFileClassNames(List<Class<? extends ProcessFile>> classNames) {
        this.classNames = classNames;
    }

    public WorkflowMetaTypeBean() {
        this(null, null);
    }

    public WorkflowMetaTypeBean(String name, String description) {
        setName(name);
        setDescription(description);
        setFileClassNames(new ArrayList<Class<? extends ProcessFile>>());
        setOrganizationModel(null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
