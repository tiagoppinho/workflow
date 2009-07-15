package module.metaWorkflow.domain;

import myorg.domain.User;

public class UserRequestor extends UserRequestor_Base {

    public UserRequestor(User user) {
	super();
	setUser(user);
    }

    @Override
    public String getEmail() {
	// TODO: implement method
	return null;
    }

    @Override
    public String getName() {
	return getUser().getPresentationName();
    }

}
