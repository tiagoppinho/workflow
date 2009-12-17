package module.workflow.domain;

import myorg.domain.exceptions.DomainException;

public class ProcessFileValidationException extends DomainException {

    public ProcessFileValidationException(String bundle, String key, String... args) {
	super(key, DomainException.getResourceFor(bundle), args);
    }

}
