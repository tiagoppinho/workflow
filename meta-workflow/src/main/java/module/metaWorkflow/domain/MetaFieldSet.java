/*
 * @(#)MetaFieldSet.java
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

import java.util.Set;
import java.util.TreeSet;

import module.metaWorkflow.exceptions.MetaWorkflowDomainException;
import module.metaWorkflow.presentationTier.dto.MetaFieldBean;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * 
 * @author João Neves
 * @author Anil Kassamali
 * 
 */
public class MetaFieldSet extends MetaFieldSet_Base {

    /**
     * Note should be avoided its use (only used by {@link MetaField#duplicatedMetaField()}
     */
    @Deprecated
    public MetaFieldSet() {
        super();
    }

    protected MetaFieldSet(MultiLanguageString name, Integer order) {
        this();
        setName(name);
        setFieldOrder(order);
    }

    public MetaFieldSet(MultiLanguageString name, Integer order, WorkflowMetaType metaType) {
        this(name, order);
        setMetaType(metaType);
    }

    public MetaFieldSet(MetaFieldBean bean, MetaFieldSet parentFieldSet) {
        this(bean.getName(), bean.getOrder(), parentFieldSet);
    }

    public MetaFieldSet(MultiLanguageString name, Integer order, MetaFieldSet parentFieldSet) {
        this(name, order);
        setParentFieldSet(parentFieldSet);
    }

    //disabled so that we can clone them
    //    @Override
    //    @ConsistencyPredicate
    //    public final boolean checkHasParent() {
    //	return super.checkHasParent() || hasMetaType();
    //    }

    public Set<MetaField> getOrderedChildFields() {
        Set<MetaField> orderedFields = new TreeSet<MetaField>(MetaField.COMPARATOR_BY_FIELD_ORDER);
        orderedFields.addAll(getChildFields());
        return orderedFields;
    }

    @Override
    public FieldSetValue createFieldValue() {
        FieldSetValue rootSet = new FieldSetValue(this);
        for (MetaField childField : getChildFields()) {
            rootSet.addChildFieldValues(childField.createFieldValue());
        }
        return rootSet;
    }

    @Override
    @Atomic
    public void delete() {
        if (isPublished()) {
            throw new MetaWorkflowDomainException("cant.delete.published.metaFields");
        }
        if (hasMetaType()) {
            throw new Error("Cannot delete the root MetaFieldSet");
        }
        if (hasAnyChildFields()) {
            throw new RuntimeException(BundleUtil.getStringFromResourceBundle("resources/MetaWorkflowResources",
                    "label.error.cannotDelete.FieldSet.before.childFields"));
        }

        removeParentFieldSet();
        removeMetaTypeVersion();
        if (!hasAnyFieldValues()) {
            deleteDomainObject();
        }
    }

    public boolean isRoot() {
        return hasMetaTypeVersion();
    }

    @Override
    @Atomic
    public void deleteItselfAndAllChildren() throws MetaWorkflowDomainException {
        for (MetaField metaField : getChildFields()) {
            metaField.deleteItselfAndAllChildren();
        }
        delete();

    }

    @Override
    public boolean isPublished() {
        boolean parentFieldSetPublished = false;
        boolean metaTypeVersionPublished = false;
        if (getParentFieldSet() != null) {
            parentFieldSetPublished = getParentFieldSet().isPublished();
        }
        if (getMetaTypeVersion() != null) {
            metaTypeVersionPublished = getMetaTypeVersion().getPublished();
        }
        return parentFieldSetPublished || metaTypeVersionPublished;
    }

}
