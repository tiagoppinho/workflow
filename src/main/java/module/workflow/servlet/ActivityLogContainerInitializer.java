package module.workflow.servlet;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import module.workflow.activities.WorkflowActivity;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
@HandlesTypes({WorkflowActivity.class})
public class ActivityLogContainerInitializer implements ServletContainerInitializer {
    
    private static final Logger logger = LoggerFactory.getLogger(ActivityLogContainerInitializer.class);

    private static final Set<WorkflowActivity> workflowActivityClassSet = new HashSet<>();
    
    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext ctx) throws ServletException {
        logger.debug("Register activity classes:");
        classes.forEach(clazz -> {
            if (!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
                try {
                    logger.debug("\t {}", clazz.getName());
                    WorkflowActivity activity = (WorkflowActivity) clazz.newInstance();
                    workflowActivityClassSet.add(activity);
                } catch (InstantiationException | IllegalAccessException e) {
                    logger.error("\t can't create activity class instance",e);
                }
            }
        });
    }

    public static Optional<WorkflowActivity> getWorkflowActivity(String name) {
        return workflowActivityClassSet.stream().filter(c -> c.getName().equals(name)).findAny();
    }
}
