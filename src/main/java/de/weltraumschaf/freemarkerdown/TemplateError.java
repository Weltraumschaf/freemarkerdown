package de.weltraumschaf.freemarkerdown;

import de.weltraumschaf.commons.validate.Validate;

/**
 * Signals any error in a parsed template such as FreeMarker syntax errors.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public final class TemplateError extends Error {

    /**
     * For serialization's sake.
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
