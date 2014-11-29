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
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import net.jcip.annotations.NotThreadSafe;

/**
 * Provides scope for variables.
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
@NotThreadSafe
final class VariableScope {

    /**
     * Parent scope (may be {@code null}).
     */
    private VariableScope parent;
    /**
     * Variables of this scope.
     */
    private final Variables data = new Variables();

    /**
     * Whether this scope has a parent scope.
     *
     * @return {@code true} if {@link #parent} is not {@code null}, else {@code false}
     */
    boolean hasParent() {
        return null != parent;
    }

    /**
     * Set a parent scope.
     * <p>
     * Throws {@link IllegalArgumentException} if self is passed in.
     * </p>
     *
     * @param parent may be {@code null}
     */
    void setParent(final VariableScope parent) {
        if (this == parent) {
            throw new IllegalArgumentException("Do not set self!");
        }

        this.parent = parent;
    }

    /**
     * Get all variables for all scopes.
     * <p>
     * Collects the variables up all parents. But does not overwrite local variables.
     * </p>
     *
     * @return never {@code null}, unmodifiable
     */
    Map<String, Object> getData() {
        final Map<String, Object> result = Maps.newHashMap();
        result.putAll(data.getVariables());

        if (hasParent()) {
            for (final Map.Entry<String, Object> entry : parent.getData().entrySet()) {
                if (!result.containsKey(entry.getKey())) {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
        }

        return Collections.unmodifiableMap(result);
    }

    /**
     * Assign any object as variable.
     *
     * @param name must not be {@code null}
     * @param value must not be {@code null}
     */
    void assignVariable(final String name, final Object value) {
        data.assignVariable(name, value);
    }

    @Override
    public String toString() {
        return "VariableScope{"
                + "parent=" + parent + ", "
                + "data=" + data
                + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, data);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof VariableScope)) {
            return false;
        }

        final VariableScope other = (VariableScope) obj;
        return Objects.equals(parent, other.parent)
                && Objects.equals(data, other.data);
    }

}
