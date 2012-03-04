/*
 * @(#)StringMetaField.java
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
package module.metaWorkflow.domain;

import module.metaWorkflow.presentationTier.dto.MetaFieldBean;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * 
 * @author João Neves
 * @author Anil Kassamali
 * 
 */
public class StringMetaField extends StringMetaField_Base {

    public StringMetaField() {
	super();
    }

    public StringMetaField(MultiLanguageString name, int order, MetaFieldSet parentFieldSet) {
	this();
	init(name, order, parentFieldSet);
    }

    public StringMetaField(MetaFieldBean bean, MetaFieldSet parentFieldSet) {
	this(bean.getName(), bean.getOrder(), parentFieldSet);
    }

    @Override
    public StringFieldValue createFieldValue() {
	return new StringFieldValue(this);
    }

    @Override
    @Service
    public void delete() {
	removeParentFieldSet();
	if (!hasAnyFieldValues()) {
	    deleteDomainObject();
	}
    }

}
