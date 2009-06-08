package module.workflow.util;

import java.util.HashMap;
import java.util.Map;

import module.workflow.domain.GenericFile;
import myorg.util.BundleUtil;

public class FileTypeNameResolver {

    private static Map<Class<? extends GenericFile>, Resolver> nameMap = new HashMap<Class<? extends GenericFile>, Resolver>();

    public static void registerType(Class<? extends GenericFile> fileType, String bundle, String key) {
	nameMap.put(fileType, new Resolver(bundle, key));
    }

    public static String getNameFor(Class<? extends GenericFile> fileType) {
	Resolver resolver = nameMap.get(fileType);
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
