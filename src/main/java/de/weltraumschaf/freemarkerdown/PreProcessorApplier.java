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
import java.util.StringTokenizer;

/**
 * Apply a processor to a given string.
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
class PreProcessorApplier {

    /**
     * Indicates beginning of preprocessor area.
     */
    private static final String START_TOKEN = "<?";
    /**
     * Indicates end of preprocessor area.
     */
    private static final String END_TOKEN = "?>";
    /**
     * Applied to the processor.
     */
    private final String subject;

    /**
     * Dedicated constructor.
     *
     * @param subject must not be {@code null}
     */
    PreProcessorApplier(final String subject) {
        super();
        this.subject = Validate.notNull(subject, "subject");
    }

    /**
     * Applies the processor on the {@link #subject} and returns the processed subject.
     *
     * @param processor must not be {@code null}
     * @return never {@code null}
     */
    String apply(final PreProcessor processor) {
        Validate.notNull(subject, "subject");
        final String startToken = START_TOKEN + processor.getTarget();
        final StringTokenizer tokenizer = new StringTokenizer(subject, " \t\n\r\f", true);
        final StringBuilder buffer = new StringBuilder();

        StringBuilder instructionBuffer = new StringBuilder();
        boolean insideInstruction = false;

        while (tokenizer.hasMoreTokens()) {
            final String current = tokenizer.nextToken();

            if (startToken.equals(current)) {
                insideInstruction = true;
                continue;
            }

            if (END_TOKEN.equals(current) && insideInstruction) {
                insideInstruction = false;
                buffer.append(processor.process(instructionBuffer.toString()));
                instructionBuffer = new StringBuilder();
                continue;
            }

            if (insideInstruction) {
                instructionBuffer.append(current);
            } else {
                buffer.append(current);
            }
        }

        return buffer.toString();
    }

}
