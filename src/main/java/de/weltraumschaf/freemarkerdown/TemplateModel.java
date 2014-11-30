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

/**
 * A template model is the base to render templates.
 * <p>
 * A model can {@link #render() render} itself and apply {@link #apply(de.weltraumschaf.freemarkerdown.PreProcessor) pre
 * processors}. Also you can {@link #assignVariable(java.lang.String, java.lang.Object) assign variables}.
 * </p>
 * <p>
 * Maybe you ask: "Why didn't call this thing simply Template?" Yes I considered that, but I didn't want a name
 * conflicting with the name of the FreeMarker template class. This was the best idea I came up with.
 * </p>
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public interface TemplateModel {

    /**
     * Renders the template in a string.
     * <p>
     * Throws {@link TemplateError} if template can't be parsed.
     * </p>
     *
     * @return never {@code null}
     */
    String render();

    /**
     * Apply a pre processor on a renderable.
     *
     * @param processor must not be {@code null}
     */
    void apply(PreProcessor processor);

    /**
     * Assign any object as template variable.
     *
     * @param name must not be {@code null} or empty
     * @param value must not be {@code null}
     */
    void assignVariable(final String name, final Object value);

}
