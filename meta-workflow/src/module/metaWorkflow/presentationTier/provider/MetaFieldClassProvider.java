package module.metaWorkflow.presentationTier.provider;

import java.util.HashSet;
import java.util.Set;

import module.metaWorkflow.domain.MetaField;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixframework.FenixFramework;
import dml.DomainClass;

public class MetaFieldClassProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
	Set<Class<? extends MetaField>> metaFieldClasses = new HashSet<Class<? extends MetaField>>();
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

    private boolean isSubclassOfMetaField(DomainClass domainClass) {
	return (domainClass.hasSuperclass() && domainClass.getSuperclass().getFullName().equals(MetaField.class.getName()));
    }

    @Override
    public Converter getConverter() {
	return null;
    }
}
