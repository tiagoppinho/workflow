/*
 * @(#)MetaField.java
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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Comparator;

import jvstm.cps.ConsistencyPredicate;
import module.metaWorkflow.exceptions.MetaWorkflowDomainException;
import module.metaWorkflow.presentationTier.dto.MetaFieldBean;
import pt.ist.bennu.core.util.BundleUtil;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * 
 * @author João Neves
 * @author Anil Kassamali
 * 
 */
public abstract class MetaField extends MetaField_Base {

    public static Comparator<MetaField> COMPARATOR_BY_FIELD_ORDER = new Comparator<MetaField>() {
	@Override
	public int compare(MetaField field0, MetaField field1) {
	    int comparison = field0.getFieldOrder().compareTo(field1.getFieldOrder());
	    if (comparison != 0) {
		return comparison;
	    }
	    return field0.getExternalId().compareTo(field1.getExternalId());
	}
    };

    protected MetaField() {
	super();
	setOjbConcreteClass(getClass().getName());
    }

    protected void init(MultiLanguageString name, Integer order, MetaFieldSet parentFieldSet) {
	this.setName(name);
	this.setFieldOrder(order);
	this.setParentFieldSet(parentFieldSet);
    }

    @Service
    public static MetaField createMetaField(MetaFieldBean bean, MetaFieldSet parentFieldSet) {
	if (StringMetaField.class == bean.getFieldClass()) {
	    return new StringMetaField(bean, parentFieldSet);
	}
	if (StringsMetaField.class == bean.getFieldClass()) {
	    return new StringsMetaField(bean, parentFieldSet);
	}
	if (DateTimeMetaField.class == bean.getFieldClass()) {
	    return new DateTimeMetaField(bean, parentFieldSet);
	}
	if (LocalDateMetaField.class == bean.getFieldClass()) {
	    return new LocalDateMetaField(bean, parentFieldSet);
	}
	if (MetaFieldSet.class == bean.getFieldClass()) {
	    return new MetaFieldSet(bean, parentFieldSet);
	}

	throw new MetaWorkflowDomainException("error.invalid.meta.field.class", bean.getFieldClass().getName());
    }

    public boolean isRootMetaFieldSet() {
	return getParentFieldSet() == null && this instanceof MetaFieldSet && ((MetaFieldSet) this).getMetaTypeVersion() != null;
    }

    /* TODO START: protection against published things FENIX-345: */

    //Disabled to be able to 'clone' MetaFields
    //    @ConsistencyPredicate
    //    public boolean checkHasParent() {
    //	return hasParentFieldSet();
    //    }

    @ConsistencyPredicate
    public boolean checkHasName() {
	Collection<Language> allLanguages = getName().getAllLanguages();

	for (Language language : allLanguages) {
	    String content = getName().getContent(language);
	    if (StringUtils.isEmpty(content)) {
		return false;
	    }
	}

	return true;
    }

    public String getPresentationName() {
	return getLocalizedClassName() + " - " + getName().getContent();
    }

    public String getLocalizedClassName() {
	return BundleUtil.getStringFromResourceBundle("resources/MetaWorkflowResources", "label." + getClass().getName());
    }

    /**
     * @author João Antunes
     * @return a {@link MetaField} with its inner values replicated. The
     *         relations of the MetaField are not replicated
     */
    protected final MetaField duplicatedMetaField() {
	Constructor<? extends MetaField> constructor;
	MetaField newInstance;
	try {
	    constructor = this.getClass().getConstructor();
	    newInstance = constructor.newInstance();
	} catch (InstantiationException e) {
	    throw new MetaWorkflowDomainException("couldnt.create.duplicateMetaField", e);
	} catch (IllegalAccessException e) {
	    throw new MetaWorkflowDomainException("couldnt.create.duplicateMetaField", e);
	} catch (IllegalArgumentException e) {
	    throw new MetaWorkflowDomainException("couldnt.create.duplicateMetaField", e);
	} catch (InvocationTargetException e) {
	    throw new MetaWorkflowDomainException("couldnt.create.duplicateMetaField", e);
	} catch (NoSuchMethodException e) {
	    throw new MetaWorkflowDomainException("couldnt.create.duplicateMetaField", e);
	} catch (SecurityException e) {
	    throw new MetaWorkflowDomainException("couldnt.create.duplicateMetaField", e);
	}
	if (newInstance != null) {
	newInstance.setName(getName());
	newInstance.setFieldOrder(getFieldOrder());
	}
	return newInstance;
    }

    /**
     * @return a new instance of the FieldValue that will contain the field's
     *         data
     */
    public abstract FieldValue createFieldValue();

    public abstract void delete();

    /**
     * Deletes all of the children, and then deletes itself NOTE: If the
     * WorkflowMetaTypeVersion which is associated with it is published, it
     * throws an exception If it has no children, it deletes itself
     * 
     * @throws MetaWorkflowDomainException
     *             if any of the children can't be deleted
     */
    abstract public void deleteItselfAndAllChildren() throws MetaWorkflowDomainException;

    /**
     * 
     * @return true if the WorkflowMetaTypeVersion associated with it is
     *         published. False otherwise
     */
    public abstract boolean isPublished();


}
