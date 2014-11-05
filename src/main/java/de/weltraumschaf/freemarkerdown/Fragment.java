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
 * 
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Fragment extends BaseTemplate {

    /**
     * Convenience constructor which sets the encoding to {@link Defaults#ENCODING}.
     *
     * @param template must not be {@code null}
     */
    public Fragment(final String template) {
        this(template, Defaults.ENCODING.getValue());
    }

    /**
     * Dedicated constructor.
     *
     * @param template must not be {@code null}
     * @param encoding must not be {@code null} or empty
     */
    public Fragment(final String template, final String encoding) {
        super(template, encoding);
    }

}
