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

/**
 * Signals any error in a parsed template such as FreeMarker syntax errors.
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class TemplateError extends Error {

    /**
     * For serialization's sake
     */
    static final long serialVersionUID = 1L;

    /**
     * Dedicated constructor.
     *
     * @param message must not be {@code null} or empty
     * @param cause may be {@code null}
     */
    public TemplateError(final String message, final Throwable cause) {
        super(Validate.notEmpty(message, "message"), cause);
    }

}
