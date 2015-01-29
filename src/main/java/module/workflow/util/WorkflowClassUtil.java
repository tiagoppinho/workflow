package module.workflow.util;

import org.fenixedu.bennu.core.i18n.BundleUtil;

public class WorkflowClassUtil {

    public static String getNameForType(Class<?> type) {
        ClassNameBundle name = type.getAnnotation(ClassNameBundle.class);
        if (name != null) {
            return BundleUtil.getString("resources." + name.bundle(), "label." + type.getName());
        }
        return "!!" + type.getName() + "!!";
    }

}
