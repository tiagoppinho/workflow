package module.workflow.activities;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import module.workflow.domain.WorkflowProcess;

public class ActivityListenerManager {

    private static Map<Class<? extends WorkflowActivity>, Set<ActivityListener>> listeners;

    static {
        listeners = new ConcurrentHashMap<Class<? extends WorkflowActivity>, Set<ActivityListener>>();
    }

    public static void addListener(Class<? extends WorkflowActivity> clazz, ActivityListener listener) {
        if (clazz == null || listener == null) {
            return;
        }

        Set<ActivityListener> set = listeners.get(clazz);
        if (set == null) {
            set = new ConcurrentSkipListSet<ActivityListener>();
            listeners.put(clazz, set);
        }
        set.add(listener);
    }

    protected static void beforeExcecute(WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity,
            ActivityInformation<? extends WorkflowProcess> activityInformation) {
        final Set<ActivityListener> set = listeners.get(activity.getClass());

        if (set != null) {
            for (final ActivityListener l : set) {
                l.beforeExecute(activityInformation);
            }
        }
    }

    protected static void afterExcecute(WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity,
            ActivityInformation<? extends WorkflowProcess> activityInformation) {
        final Set<ActivityListener> set = listeners.get(activity.getClass());

        if (set != null) {
            for (final ActivityListener l : set) {
                l.afterExecute(activityInformation);
            }
        }
    }
}
