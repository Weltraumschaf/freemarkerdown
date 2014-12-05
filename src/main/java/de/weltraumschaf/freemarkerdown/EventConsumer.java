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

import de.weltraumschaf.freemarkerdown.Interceptor.ExecutionPoint;

/**
 * Implementations consumes events from the subject they registered.
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
interface EventConsumer {

    /**
     * Accepts an event and processes it.
     *
     * @param event must not be {@code null}
     */
    void trigegr(Event event);

    /**
     * Register an interceptor which will be invoked for events for given execution point.
     *
     * @param interceptor must not be {@code null}
     * @param point must not be {@code null}
     */
    void register(final Interceptor interceptor, final ExecutionPoint point);

}
