package module.workflow.activities;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.BiConsumer;

import module.workflow.domain.WorkflowProcess;

public class ActivityListenerManager {

    private static Map<Class<?>, Set<ActivityListener>> listeners;

    static {
        listeners = new ConcurrentHashMap<Class<?>, Set<ActivityListener>>();
    }

    public static void addListener(final Class<?> clazz, final ActivityListener listener) {
        if (clazz == null || listener == null) {
            return;
        }

        listeners.computeIfAbsent(clazz, k -> new CopyOnWriteArraySet<ActivityListener>()).add(listener);
    }

    public static void removeListener(Class<?> clazz, ActivityListener listener) {
        if (clazz == null || listener == null) {
            return;
        }

        final Set<ActivityListener> set = listeners.get(clazz);
        if (set == null) {
            return;
        }
        set.remove(listener);
    }

    private static final BiConsumer<ActivityInformation<? extends WorkflowProcess>, ActivityListener> BEFORE_EX_CONSUMER =
            (i, l) -> l.beforeExecute(i);

    private static final BiConsumer<ActivityInformation<? extends WorkflowProcess>, ActivityListener> BEFORE_P_CONSUMER =
            (i, l) -> l.beforeProcess(i);

    private static final BiConsumer<ActivityInformation<? extends WorkflowProcess>, ActivityListener> AFTER_P_CONSUMER =
            (i, l) -> l.afterProcess(i);

    private static final BiConsumer<ActivityInformation<? extends WorkflowProcess>, ActivityListener> AFTER_EX_CONSUMER =
            (i, l) -> l.afterExecute(i);

    protected static void beforeExcecute(final ActivityInformation<? extends WorkflowProcess> activityInformation) {
        doWithInformation(activityInformation, BEFORE_EX_CONSUMER);
    }

    protected static void beforeProcess(final ActivityInformation<? extends WorkflowProcess> activityInformation) {
        doWithInformation(activityInformation, BEFORE_P_CONSUMER);
    }

    protected static void afterProcess(final ActivityInformation<? extends WorkflowProcess> activityInformation) {
        doWithInformation(activityInformation, AFTER_P_CONSUMER);
    }

    protected static void afterExcecute(final ActivityInformation<? extends WorkflowProcess> activityInformation) {
        doWithInformation(activityInformation, AFTER_EX_CONSUMER);
    }

    private static void doWithInformation(final ActivityInformation<? extends WorkflowProcess> activityInformation,
            final BiConsumer<ActivityInformation<? extends WorkflowProcess>, ActivityListener> consumer) {
        final Class<?> clazz = activityInformation.getActivityClass();
        final Set<ActivityListener> set = listeners.get(clazz);
        if (set != null) {
            for (final ActivityListener l : set) {
                consumer.accept(activityInformation, l);
            }
        }
    }

}
