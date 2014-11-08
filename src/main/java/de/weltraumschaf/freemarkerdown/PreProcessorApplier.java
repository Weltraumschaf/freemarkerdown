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
 * Apply a processor to a given string.
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
@NotThreadSafe
interface PreProcessorApplier {

    /**
     * Applies the processor on the subject and returns the processed subject.
     *
     * @param subject must not be {@code null}
     * @param processor must not be {@code null}
     * @return never {@code null}
     */
    String apply(final String subject, final PreProcessor processor);

}
