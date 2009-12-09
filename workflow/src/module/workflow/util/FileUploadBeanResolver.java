package module.workflow.util;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import module.workflow.domain.ProcessFile;
import module.workflow.domain.WorkflowProcess;

public class FileUploadBeanResolver {

    private static Map<Class<? extends ProcessFile>, Class<? extends WorkflowFileUploadBean>> beanMap = new HashMap<Class<? extends ProcessFile>, Class<? extends WorkflowFileUploadBean>>();

    @SuppressWarnings("unchecked")
    public static <T extends WorkflowFileUploadBean> T getBeanForProcessFile(WorkflowProcess process,
	    Class<? extends ProcessFile> clazz) {
	Class<? extends WorkflowFileUploadBean> beanClass = beanMap.get(clazz);
	if (beanClass == null) {
	    return (T) new WorkflowFileUploadBean(process);
	} else {
	    try {
		Constructor<? extends WorkflowFileUploadBean> constructor = beanClass.getConstructor(WorkflowProcess.class);
		return (T) constructor.newInstance(process);
	    } catch (Exception e) {
		e.printStackTrace();
		return (T) new WorkflowFileUploadBean(process);
	    }
	}
    }

    public static void registerBeanForProcessFile(Class<? extends ProcessFile> processFileClass,
	    Class<? extends WorkflowFileUploadBean> beanClass) {
	if (beanMap.get(processFileClass) != null) {
	    throw new RuntimeException("error.adding.already.existing.binding");
	}
	beanMap.put(processFileClass, beanClass);
    }

    public static void unregisterBeanForProcessFile(Class<? extends ProcessFile> processFileClass) {
	beanMap.remove(processFileClass);
    }
}
