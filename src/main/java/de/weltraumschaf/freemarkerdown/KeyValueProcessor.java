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

import de.weltraumschaf.commons.validate.Validate;
import java.util.Map;
import net.jcip.annotations.NotThreadSafe;

/**
 * Pre processor blocks are opened by {@code <?fdm-keyvalue} and closed by {@code ?>}.
 * <p>
 * Between these markers key value pairs are recognized. Example:
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
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
@NotThreadSafe
final class KeyValueProcessor implements PreProcessor {

    /**
     * Listens for {@code <?fmd-keyvalue ... ?>}.
     */
    private static final String TARGET = "fdm-keyvalue";
    /**
     * Separates key value pair.
     *
     * Example: {@code key ":" value NL}
     */
    private static final String SPLIT_TOKEN = ":";
    /**
     * Collects the found key value pairs.
     */
    private final Map<String, String> result;

    /**
     * Dedicated constructor.
     *
     * @param result must not be {@code null}
     */
    KeyValueProcessor(final Map<String, String> result) {
        super();
        this.result = Validate.notNull(result, "result");
    }

    @Override
    public String getTarget() {
        return TARGET;
    }

    @Override
    public String process(final String input) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
