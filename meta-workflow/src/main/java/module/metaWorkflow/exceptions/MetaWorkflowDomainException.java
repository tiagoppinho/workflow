/**
 * 
 */
package module.metaWorkflow.exceptions;

import java.util.ResourceBundle;

import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * @author João Antunes (joao.antunes@tagus.ist.utl.pt) - 14 de Jun de 2012
 * 
 * 
 */
public class MetaWorkflowDomainException extends DomainException {

    @Override
    public ResourceBundle getBundle() {
        return ResourceBundle.getBundle("resources/MetaWorkflowResources", Language.getLocale());
    }

    /**
     * 
     */
    public MetaWorkflowDomainException() {
        super();
    }

    /**
     * @param key
     * @param args
     */
    public MetaWorkflowDomainException(String key, String... args) {
        super(key, args);
    }

    /**
     * @param key
     * @param bundle
     * @param args
     */
    public MetaWorkflowDomainException(String key, ResourceBundle bundle, String... args) {
        super(key, bundle, args);
    }

    /**
     * @param key
     * @param cause
     * @param args
     */
    public MetaWorkflowDomainException(String key, Throwable cause, String... args) {
        super(key, cause, args);
    }

    /**
     * @param key
     * @param cause
     * @param bundle
     * @param args
     */
    public MetaWorkflowDomainException(String key, Throwable cause, ResourceBundle bundle, String... args) {
        super(key, cause, bundle, args);
    }

}
