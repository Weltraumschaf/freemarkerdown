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
import net.jcip.annotations.NotThreadSafe;

/**
 * Implementation of an applier.
 * <p>
 * This implementation finds all parts starting with the processing token ({@code <?TARGET_NAME}) and
 * passes the enclosed srtring to the processor.
 * </p>
 * <p>
 * For example the given string:
 * </p>
 * <pre>
 * Lorem ipsum dolor
 * <?foo instruction ?>
 * Lorem ipsum dolor
 * </pre>
 * <p>
 * Applied on a processor with the {@link PreProcessor#getTarget() target} {@code foo} will receive
 * the string {@code "instruction" as argument. If the processor reutns {@code "foobar"} the resulting
 * string from the applier will be:
 * </p>
 * <pre>
 * Lorem ipsum dolor
 * foobar
 * Lorem ipsum dolor
 * </pre>
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
@NotThreadSafe
final class PreProcessorApplierImpl implements PreProcessorApplier {
/**
     * Indicates beginning of preprocessor area.
     */
    private static final String START_TOKEN = "<?";
    /**
     * Indicates end of preprocessor area.
     */
    private static final String END_TOKEN = "?>";

    @Override
    public String apply(final String subject, final PreProcessor processor) {
        Validate.notNull(subject, "subject");
        Validate.notNull(processor, "processor");
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
