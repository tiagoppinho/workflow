/*
 * @(#)MetaFieldBean.java
 *
 * Copyright 2011 Instituto Superior Tecnico
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
package module.metaWorkflow.presentationTier.dto;

import java.io.Serializable;

import module.metaWorkflow.domain.MetaField;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * 
 * @author João Neves
 * @author Anil Kassamali
 * 
 */
public class MetaFieldBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Class<? extends MetaField> fieldClass;

    private MultiLanguageString name;

    private int order;

    public void setFieldClass(Class<? extends MetaField> fieldClass) {
        this.fieldClass = fieldClass;
    }

    public Class<? extends MetaField> getFieldClass() {
        return fieldClass;
    }

    public void setName(MultiLanguageString name) {
        this.name = name;
    }

    public MultiLanguageString getName() {
        return name;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

}
