package de.weltraumschaf.freemarkerdown;

import java.util.Map;

/**
 * Provides some handy pre processors.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public final class PreProcessors {

    /**
     * Hidden for pure static class.
     */
    private PreProcessors() {
        super();
        throw new UnsupportedOperationException("Do not call via reflection!");
    }

    /**
     * Provides a pre processor which parses key value pairs.
     * <p>
     * Example:
     * </p>
     * <pre>
     * ...
     * &gt;?fdm-keyvalue
     *   key1: value1
     *   key2: value2
     *   // ...
     *   keyN: valueN
     * ?&lt;
     * ...
     * </pre>
     * <p>
     * These pairs are stored in the result map.
     * </p>
     *
     * @param result must not be {@code null}
     * @return never {@code null}, always new instance
     */
    public static PreProcessor createKeyValueProcessor(final Map<String, String> result) {
        return new KeyValueProcessor(result);
    }

    /**
     * Provides a pre processor which parses key value pairs for custom target.
     * <p>
     * Example:
     * </p>
     * <pre>
     * ...
     * &gt;?target
     *   key1: value1
     *   key2: value2
     *   // ...
     *   keyN: valueN
     * ?&lt;
     * ...
     * </pre>
     * <p>
     * These pairs are stored in the result map.
     * </p>
     *
    * @param result must not be {@code null}
     * @param target must not be {@code null} or empty
     * @return never {@code null}, always new instance
     */
    public static PreProcessor createKeyValueProcessor(final Map<String, String> result, final String target) {
        return new KeyValueProcessor(result, target);
    }
}
