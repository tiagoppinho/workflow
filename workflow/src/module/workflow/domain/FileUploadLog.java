package module.workflow.domain;

import myorg.domain.User;
import myorg.util.BundleUtil;

public class FileUploadLog extends FileUploadLog_Base {
    
    public FileUploadLog(WorkflowProcess process, User person, String... argumentsDescription) {
	super();
	init(process, person, argumentsDescription);
    }

    @Override
    public String getDescription() {
	return BundleUtil.getFormattedStringFromResourceBundle("resources/WorkflowResources", "label.description.FileUploadLog",
		getDescriptionArguments().toArray(new String[] {}));
    }
    
}
