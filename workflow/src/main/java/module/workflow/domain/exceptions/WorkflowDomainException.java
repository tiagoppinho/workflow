/**
 * 
 */
package module.workflow.domain.exceptions;

import org.fenixedu.bennu.core.domain.exceptions.DomainException;

/**
 * @author João Antunes (joao.antunes@tagus.ist.utl.pt) - 11 de Set de 2012
 * 
 *         Convenience class that wraps the {@link DomainException} constructors
 *         with the default workflow resource bundle
 */
public class WorkflowDomainException extends DomainException {
    private static final long serialVersionUID = 1L;

    /**
     * @param key
     * @param args
     */
    public WorkflowDomainException(String key, String... args) {
        super("resources.WorkflowResources", key, args);
    }

    /**
     * @param key
     * @param cause
     * @param args
     */
    public WorkflowDomainException(String key, Throwable cause, String... args) {
        super(cause, "resources.WorkflowResources", key, args);
    }

}
