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

import net.jcip.annotations.NotThreadSafe;

/**
 * Implementors can render them self to a string.
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public interface Renderable {

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

}
