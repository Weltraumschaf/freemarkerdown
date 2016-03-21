package de.weltraumschaf.freemarkerdown;

import de.weltraumschaf.commons.validate.Validate;

/**
 * Default values used in the library.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
enum Defaults {
    /**
     * Used default encoding.
     */
//    ENCODING("utf-8"),
    /**
     * Used default new line separator.
     */
    DEFAULT_NEW_LINE(String.format("%n"));

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
