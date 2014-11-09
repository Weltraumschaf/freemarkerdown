/*
 *  LICENSE
 *
 * "THE BEER-WARE LICENSE" (Revision 43):
 * "Sven Strittmatter" <weltraumschaf@googlemail.com> wrote this file.
 * As long as you retain this notice you can do whatever you want with
 * this stuff. If we meet some day, and you think this stuff is worth it,
 * you can buy me a non alcohol-free beer in return.
 *
 * Copyright (C) 2012 "Sven Strittmatter" <weltraumschaf@googlemail.com>
 */
package de.weltraumschaf.freemarkerdown;

import java.util.Map;

/**
 * Provides some handy pre processors.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
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
}
