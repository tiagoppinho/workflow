/*
 * @(#)DateTimeMetaField.java
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

import module.metaWorkflow.exceptions.MetaWorkflowDomainException;
import module.metaWorkflow.presentationTier.dto.MetaFieldBean;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * 
 * @author João Neves
 * @author Anil Kassamali
 * 
 */
public class DateTimeMetaField extends DateTimeMetaField_Base {

    /**
     * Note should be avoided its use (only used by {@link MetaField#duplicatedMetaField()}
     */
    @Deprecated
    public DateTimeMetaField() {
        super();
    }

    public DateTimeMetaField(MultiLanguageString name, Integer order, MetaFieldSet parentFieldSet) {
        this();
        init(name, order, parentFieldSet);
    }

    public DateTimeMetaField(final MetaFieldBean bean, MetaFieldSet parentFieldSet) {
        this(bean.getName(), bean.getOrder(), parentFieldSet);
    }

    @Override
    public FieldValue createFieldValue() {
        return new DateTimeFieldValue(this);
    }

    @Override
    @Atomic
    public void delete() {
        if (isPublished()) {
            throw new MetaWorkflowDomainException("error.cant.delete.a.published.metaField");
        }
        removeParentFieldSet();
        if (!hasAnyFieldValues()) {
            deleteDomainObject();
        }
    }

    @Override
    @Atomic
    public void deleteItselfAndAllChildren() throws MetaWorkflowDomainException {
        delete();
    }

    @Override
    public boolean isPublished() {
        return getParentFieldSet().isPublished();
    }

}
