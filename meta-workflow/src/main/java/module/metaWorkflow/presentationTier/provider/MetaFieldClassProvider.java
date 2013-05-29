/*
 * @(#)MetaFieldClassProvider.java
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
package module.metaWorkflow.presentationTier.provider;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import module.metaWorkflow.domain.MetaField;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.dml.DomainClass;

/**
 * 
 * @author João Neves
 * 
 */
public class MetaFieldClassProvider implements DataProvider {

    public static Comparator<Class<?>> CLASS_COMPARATOR_BY_SIMPLE_NAME_OR_FULL_PACKAGE_NAME = new Comparator<Class<?>>() {
        @Override
        public int compare(Class<?> class0, Class<?> class1) {
            int comparison = class0.getSimpleName().compareTo(class1.getSimpleName());
            if (comparison != 0) {
                return comparison;
            }
            return class0.getName().compareTo(class1.getName());
        }
    };

    @Override
    public Object provide(Object source, Object currentValue) {
        return getMetaFieldClassesSet();
    }

    public static Set<Class<? extends MetaField>> getMetaFieldClassesSet() {
        Set<Class<? extends MetaField>> metaFieldClasses =
                new TreeSet<Class<? extends MetaField>>(CLASS_COMPARATOR_BY_SIMPLE_NAME_OR_FULL_PACKAGE_NAME);
        for (DomainClass domainClass : FenixFramework.getDomainModel().getDomainClasses()) {
            if (isSubclassOfMetaField(domainClass)) {
                try {
                    metaFieldClasses.add((Class<? extends MetaField>) Class.forName(domainClass.getFullName()));
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException("Domain class not found: " + domainClass.getFullName(), ex);
                }
            }
        }
        return metaFieldClasses;
    }

    private static boolean isSubclassOfMetaField(DomainClass domainClass) {
        return (domainClass.hasSuperclass() && domainClass.getSuperclass().getFullName().equals(MetaField.class.getName()));
    }

    @Override
    public Converter getConverter() {
        return null;
    }
}
