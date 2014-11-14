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

import de.weltraumschaf.commons.guava.Lists;
import de.weltraumschaf.commons.validate.Validate;
import java.util.Collection;
import java.util.Collections;
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
 *   // Comments are skipped.
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
     * Start of single line comment.
     */
    private static final String COMMENT_TOKEN = "//";
    /**
     * Collects the found key value pairs.
     */
    private final Map<String, String> result;
    /**
     * Collects warnings during processing.
     */
    private final Collection<String> warnings = Lists.newArrayList();

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
    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }

    @Override
    public Collection<String> getWarnings() {
        return Collections.unmodifiableCollection(warnings);
    }

    @Override
    public String getTarget() {
        return TARGET;
    }

    @Override
    public String process(final String input) {
        Validate.notNull(input, "input");
        warnings.clear();

        for (final String line : input.split(Defaults.DEFAULT_NEW_LINE.getValue())) {
            if (line.trim().isEmpty()) {
                continue; // Ignore empty lines.
            }

            if (line.trim().startsWith(COMMENT_TOKEN)) {
                continue; // Ignore comments.
            }

            if (!line.contains(SPLIT_TOKEN)) {
                warnings.add(String.format("Malformed line '%s'! Missing split token '%s'. Use format 'key %s value'.",
                        line, SPLIT_TOKEN, SPLIT_TOKEN));
                continue;
            }

            final String[] tokens = line.split(SPLIT_TOKEN);

            if (tokens.length == 0) {
                warnings.add(String.format("No key given: '%s'! Skipping line.", line));
                continue;
            }

            final String name = tokens[0].trim();

            if (name.isEmpty()) {
                warnings.add(String.format("Empty key given: '%s'! Skipping line.", line));
                continue;
            }

            final String value;

            if (tokens.length == 1) {
                warnings.add(String.format("No value given: '%s'! Set vlaue empty.", name));
                value = "";
            } else {
                value = tokens[1].trim();

                if (value.isEmpty()) {
                    warnings.add(String.format("Empty value given: '%s'! Set vlaue empty.", name));
                }
            }

            result.put(name, value);
        }

        return "";
    }


}
