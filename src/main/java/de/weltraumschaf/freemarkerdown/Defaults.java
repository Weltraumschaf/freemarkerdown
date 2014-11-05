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
 * Default values used in the library.
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
enum Defaults {
    /**
     * USed default encoding.
     */
    ENCODING("utf-8");

    /**
     * Value of the default.
     */
    private final String value;

    /**
     * Dedicated constructor.
     *
     * @param value must not be {@code null} or empty
     */
    private Defaults(final String value) {
        this.value = Validate.notEmpty(value, "value");
    }

    /**
     * Get default value.
     *
     * @return never {@code null} or empty
     */
    public String getValue() {
        return value;
    }

}
