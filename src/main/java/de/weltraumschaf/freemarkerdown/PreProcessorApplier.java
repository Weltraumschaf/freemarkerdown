package de.weltraumschaf.freemarkerdown;

import net.jcip.annotations.NotThreadSafe;

/**
 * Apply a processor to a given string.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
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
