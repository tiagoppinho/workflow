package module.metaWorkflow.util;

import java.util.HashMap;
import java.util.Map;

import module.metaWorkflow.domain.WorkflowQueue;
import myorg.util.BundleUtil;

public class QueueNameResolver {

    private static Map<Class<? extends WorkflowQueue>, Resolver> nameMap = new HashMap<Class<? extends WorkflowQueue>, Resolver>();

    public static void registerType(Class<? extends WorkflowQueue> queueType, String bundle, String key) {
	nameMap.put(queueType, new Resolver(bundle, key));
    }

    public static String getNameFor(Class<? extends WorkflowQueue> queueType) {
	Resolver resolver = nameMap.get(queueType);
	return BundleUtil.getStringFromResourceBundle(resolver.getBundle(), resolver.getKey());
    }

    private static class Resolver {
	private String bundle;
	private String key;

	public Resolver(String bundle, String key) {
	    this.bundle = bundle;
	    this.key = key;
	}

	public String getBundle() {
	    return this.bundle;
	}

	public String getKey() {
	    return this.key;
	}
    }
}
