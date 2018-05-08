package module.workflow.domain;

import java.util.HashMap;
import java.util.Map;

import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class ProcessFileSignatureHandler<T extends ProcessFile> {

    public static interface Provider<T extends ProcessFile> {

        public ProcessFileSignatureHandler<T> provide(final T file);

    }

    private static Map<Class<?>, Provider<?>> providers = new HashMap<>();

    public static void register(final Class<?> clazz, final Provider<?> provider) {
        providers.put(clazz, provider);
    }

    public static <T extends ProcessFile> ProcessFileSignatureHandler<T> handlerFor(final T file) {
        final Class<? extends ProcessFile> clazz = file.getClass();
        @SuppressWarnings("unchecked")
        final Provider<T> provider = (Provider<T>) providerFor(clazz);
        return provider == null ? null : provider.provide(file);
    }

    public static Provider<?> providerFor(final Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        final Provider<?> provider = providers.get(clazz);
        return provider == null ? providerFor(clazz.getSuperclass()) : provider;
    }

    static {
        register(ProcessFile.class, (f) -> new ProcessFileSignatureHandler<>(f));
    }

    protected T processFile;

    public ProcessFileSignatureHandler(final T processFile) {
        this.processFile = processFile;
    }

    public String queue() {
        return Authenticate.getUser().getUsername();
    }

    public String filename() {
        return processFile.getFilename();
    }

    public String title() {
        return processFile.getProcess().getProcessNumber() + " " + processFile.getFilename();
    }

    public boolean allowMultipleSignatures() {
        return false;
    }

    public String description() {
        return title();
    }

    public String externalIdentifier() {
        return processFile.getUuid();
    }

    public String signatureField() {
        return "";
    }

    public String callbackUrl(final byte[] jwtSecret) {
        return CoreConfiguration.getConfiguration().applicationUrl() + "/workflow/" + processFile.getExternalId() + "/sign?nounce=" + nounce(jwtSecret);
    }

    protected String nounce(final byte[] jwtSecret) {
        return Jwts.builder()
                .setSubject(processFile.getUuid())
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean canSignFile() {
        return false;
    }

    private static final String SUFFIX = "_signed";

    public String signedFileName() {
        final String filename = filename();
        final int i = filename.lastIndexOf('.');
        return i < 0 ? filename + SUFFIX : filename.substring(0, i) + SUFFIX + filename.substring(i);
    }

}
