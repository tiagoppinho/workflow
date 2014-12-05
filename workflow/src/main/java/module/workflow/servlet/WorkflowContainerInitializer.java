package module.workflow.servlet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;

import module.workflow.domain.WorkflowProcess;
import module.workflow.util.WorkflowProcessViewer;

import org.fenixedu.bennu.portal.model.Functionality;
import org.fenixedu.bennu.struts.portal.RenderersAnnotationProcessor;

@HandlesTypes(WorkflowProcessViewer.class)
public class WorkflowContainerInitializer implements ServletContainerInitializer {

    private static final Map<Class<? extends WorkflowProcess>, Class<?>> functionalityMap = new HashMap<>();

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        if (c != null) {
            for (Class<?> type : c) {
                WorkflowProcessViewer viewer = type.getAnnotation(WorkflowProcessViewer.class);
                if (viewer != null) {
                    for (Class<? extends WorkflowProcess> process : viewer.value()) {
                        if (functionalityMap.containsKey(process)) {
                            throw new Error("Multiple Viewers for process type " + process.getName());
                        } else {
                            functionalityMap.put(process, type);
                        }
                    }
                }
            };
        }
    }

    public static Functionality getFunctionalityForProcess(Class<? extends WorkflowProcess> type) {
        return RenderersAnnotationProcessor.getFunctionalityForType(functionalityMap.get(type));
    }
}
