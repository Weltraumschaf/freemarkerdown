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
 * Implementations trigger events on the registered consumers.
 *
 * <p>
 * This is not an interface, because due to stupid language design interface methods are always public.
 * This will lead to private API leakage, so i decided to use abstract classes as workaround.
 * </p>
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
abstract class EventProducer {

    /**
     * Triggers all events on the registered consumer.
     *
     * @param consumer must not be {@code null}
     */
    abstract void register(EventConsumer consumer);

    /**
     * Do not trigger events any more on unregistered consumer.
     *
     * @param consumer must not be {@code null}
     */
    abstract void unregister(EventConsumer consumer);

}
