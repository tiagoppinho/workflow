package module.metaWorkflow.domain;

public class ExternalRequestor extends ExternalRequestor_Base {

    public ExternalRequestor(String name, String email) {
	super();
	setName(name);
	setEmail(email);
    }

    @Override
    public String getShortName() {
	return getName();
    }
}
