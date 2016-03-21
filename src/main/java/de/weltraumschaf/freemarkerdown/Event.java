package de.weltraumschaf.freemarkerdown;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.freemarkerdown.Interceptor.ExecutionPoint;

/**
 * Used to signal events between {@link EventConsumer consumers} and {@link EventProducer producers}.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
final class Event {
    /**
     * When did it happened.
     */
    private final ExecutionPoint point;
    /**
     * Where did it happened.
     */
    private final TemplateModel source;
    /**
     * Some content string from the source.
     */
    private final String content;

    /**
     * Dedicated constructor.
     *
     * @param point must not be {@code null}
     * @param source must not be {@code null}
     * @param content must not be {@code null}
     */
    Event(final Interceptor.ExecutionPoint point, final TemplateModel source, final String content) {
        super();
        this.point = Validate.notNull(point, "point");
        this.source = Validate.notNull(source, "source");
        this.content = Validate.notNull(content, "content");
    }

    /**
     * Get the point when the event happened.
     *
     * @return never {@code null}
     */
    ExecutionPoint getPoint() {
        return point;
    }

    /**
     * Get the source where the event happened.
     *
     * @return never {@code null}
     */
    TemplateModel getSource() {
        return source;
    }

    /**
     * Get some optional template content.
     *
     * @return never {@code null}, may be empty
     */
    String getContent() {
        return content;
    }

}
