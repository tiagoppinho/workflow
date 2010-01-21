package module.metaWorkflow.domain;

import myorg.domain.User;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class MetaWorkflowUserDashBoardPanel extends MetaWorkflowUserDashBoardPanel_Base {

    public MetaWorkflowUserDashBoardPanel() {
	super();
    }

    public MetaWorkflowUserDashBoardPanel(MultiLanguageString name, User user) {
	super();
	setName(name);
	setUser(user);
    }

}
