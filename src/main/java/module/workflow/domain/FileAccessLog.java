package module.workflow.domain;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class FileAccessLog extends FileAccessLog_Base {

    public FileAccessLog(WorkflowProcess process, User person, String... argumentsDescription) {
        super();
        init(process, person, argumentsDescription);
    }

    @Override
    public String getDescription() {
        return BundleUtil.getString("resources/WorkflowResources", "label.description.FileAccessLog", getDescriptionArguments()
                .toArray(new String[] {}));
    }

}
