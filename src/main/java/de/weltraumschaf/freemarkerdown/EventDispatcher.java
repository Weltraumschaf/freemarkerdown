package de.weltraumschaf.freemarkerdown;

import de.weltraumschaf.commons.guava.Lists;
import de.weltraumschaf.commons.guava.Maps;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.freemarkerdown.Interceptor.ExecutionPoint;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Dispatches events generated from the someone and delegates it to registered interceptors.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
final class EventDispatcher implements EventConsumer {

    /**
     * Holds interceptors.
     */
    private final Map<Interceptor.ExecutionPoint, Collection<Interceptor>> interceptors = Maps.newHashMap();

    @Override
    public void trigger(final Event event) {
        final Interceptor.ExecutionPoint point = event.getPoint();

        if (interceptors.containsKey(point)) {
            for (final Interceptor interceptor : interceptors.get(point)) {
                interceptor.intercept(point, event.getSource(), event.getContent());
            }
        }
    }

    /**
     * Registers an interceptor for an execution point.
     *
     * @param interceptor must not be {@code null}
     * @param point must not be {@code null}
     */
    @Override
    public void register(final Interceptor interceptor, final ExecutionPoint point) {
        Validate.notNull(interceptor, "interceptor");
        Validate.notNull(point, "point");

        if (!interceptors.containsKey(point)) {
            interceptors.put(point, Lists.<Interceptor>newArrayList());
        }

        interceptors.get(point).add(interceptor);
    }

    @Override
    public String toString() {
        return "EventDispatcher{" + "interceptors=" + interceptors + '}';
    }


    /**
     * Get the registered interceptors.
     *
     * @return never {@code null}, unmodifiable
     */
    Map<ExecutionPoint, Collection<Interceptor>> getInterceptors() {
        return Collections.unmodifiableMap(interceptors);
    }

}
