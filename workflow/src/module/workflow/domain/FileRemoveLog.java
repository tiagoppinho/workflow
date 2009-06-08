package module.workflow.domain;

import myorg.domain.User;
import myorg.util.BundleUtil;

public class FileRemoveLog extends FileRemoveLog_Base {

    public FileRemoveLog(WorkflowProcess process, User person, String... argumentsDescription) {
	super();
	init(process, person, argumentsDescription);
    }

    @Override
    public String getDescription() {
	return BundleUtil.getFormattedStringFromResourceBundle("resources/WorkflowResources", "label.description.FileRemoveLog",
		getDescriptionArguments().toArray(new String[] {}));
    }

}
