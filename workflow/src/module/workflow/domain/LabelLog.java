package module.workflow.domain;

import pt.utl.ist.fenix.tools.util.Strings;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class LabelLog extends LabelLog_Base {

    protected LabelLog() {
	super();
    }

    public LabelLog(WorkflowProcess process, User person, String label, String bundle, String... arguments) {
	super();
	init(process, person, arguments);
	setLabel(label);
	setBundle(bundle);

    }

    @Override
    public String getDescription() {
	Strings arguments = getDescriptionArguments();
	return arguments != null ? BundleUtil.getFormattedStringFromResourceBundle(getBundle(), getLabel(), arguments
		.toArray(new String[] {})) : BundleUtil.getFormattedStringFromResourceBundle(getBundle(), getLabel());
    }
}
