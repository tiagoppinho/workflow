package module.workflow.domain;

import myorg.domain.exceptions.DomainException;
import myorg.util.BundleUtil;

public class ProcessFileValidationException extends DomainException {

    private String key;
    private String bundle;
    private String[] arguments;

    public ProcessFileValidationException(String bundle, String key, String... arguments) {
	setKey(key);
	setBundle(bundle);
	setArguments(arguments);
    }

    public String getKey() {
	return key;
    }

    public void setKey(String key) {
	this.key = key;
    }

    public String getBundle() {
	return bundle;
    }

    public void setBundle(String bundle) {
	this.bundle = bundle;
    }

    public String[] getArguments() {
	return arguments;
    }

    public void setArguments(String[] arguments) {
	this.arguments = arguments;
    }

    @Override
    public String getLocalizedMessage() {
	return getArguments() != null ? BundleUtil.getFormattedStringFromResourceBundle(getBundle(), getKey(), getArguments())
		: BundleUtil.getFormattedStringFromResourceBundle(getBundle(), getKey());
    }

    @Override
    public String getMessage() {
	return getKey();
    }
}
