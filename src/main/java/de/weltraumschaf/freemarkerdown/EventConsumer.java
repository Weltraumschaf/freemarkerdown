package de.weltraumschaf.freemarkerdown;

import de.weltraumschaf.freemarkerdown.Interceptor.ExecutionPoint;

/**
 * Implementations consumes events from the subject they registered.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
interface EventConsumer {

    /**
     * Accepts an event and processes it.
     *
     * @param event must not be {@code null}
     */
    void trigger(Event event);

    /**
     * Register an interceptor which will be invoked for events for given execution point.
     *
     * @param interceptor must not be {@code null}
     * @param point must not be {@code null}
     */
    void register(final Interceptor interceptor, final ExecutionPoint point);

}
