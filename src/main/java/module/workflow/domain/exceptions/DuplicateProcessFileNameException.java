/**
 * 
 */
package module.workflow.domain.exceptions;

/**
 * @author João Antunes (joao.antunes@tagus.ist.utl.pt) - 30 de Nov de 2012
 * 
 * 
 */
public class DuplicateProcessFileNameException extends WorkflowDomainException {

    /**
     * @param key key
     * @param args args
     */
    public DuplicateProcessFileNameException(String key, String... args) {
        super(key, args);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param key key
     * @param cause cause
     * @param args args
     */
    public DuplicateProcessFileNameException(String key, Throwable cause, String... args) {
        super(key, cause, args);
        // TODO Auto-generated constructor stub
    }

}
