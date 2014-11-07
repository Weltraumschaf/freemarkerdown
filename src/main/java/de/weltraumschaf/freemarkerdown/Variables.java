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

import de.weltraumschaf.commons.guava.Maps;
import de.weltraumschaf.commons.validate.Validate;
import java.util.Collections;
import java.util.Map;
import net.jcip.annotations.NotThreadSafe;

/**
 * Collects assigned variables.
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
@NotThreadSafe
final class Variables {

    /**
     * Holds the assigned variables.
     */
    private final Map<String, Object> templateVariables = Maps.newHashMap();

    /**
     * Assign any object as template variable.
     *
     * @param name must not be {@code null}
     * @param value must not be {@code null}
     */
    public void assignVariable(final String name, final Object value) {
        templateVariables.put(Validate.notEmpty(name, "name"), Validate.notNull(value, "value"));
    }

    /**
     * Get all assigned variables.
     *
     * @return never {@code null}, unmodifiable
     */
    Map<String, Object> getVariables() {
        return Collections.unmodifiableMap(templateVariables);
    }

    @Override
    public int hashCode() {
        return templateVariables.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Variables)) {
            return false;
        }

        final Variables other = (Variables) obj;
        return templateVariables.equals(other.templateVariables);
    }

    @Override
    public String toString() {
        return "Variables{" + "templateVariables=" + templateVariables + '}';
    }

}
