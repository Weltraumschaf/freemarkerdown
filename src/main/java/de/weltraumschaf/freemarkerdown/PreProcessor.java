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

import net.jcip.annotations.NotThreadSafe;

/**
 * A pre processor is invoked for each instruction it is registered for.
 * <p>
 * Example: A preprocessor which has the {@link #getTarget() target} {@code "foo"} will be invoked for all
 * instructions introduced by {@code <?foo ... ?>}. So if a template with this content is rendered:
 * </p>
 * <pre>
 * Lorem ipsum dolor sit amet
 * <?foo some instructions ?>
 * consetetur sadipscing elitr
 * <?foo something else ?>
 * sed diam nonumy eirmod tempor
 * </pre>
 * <p>
 * The preprocessor will {@link #process(java.lang.String) receive} two strings:
 * </p>
 * <ol>
 * <li>{@code " some instructions "}</li>
 * <li>{@code " something else "}</li>
 * </ol>
 * <p>
 * The whole processing instruction will be replaced by the return value of the processor. So if the pre processor
 * here in the example will return always the string {@code "FOO"} the result will be:
 * </p>
 * <pre>
 * Lorem ipsum dolor sit amet
 * FOO
 * consetetur sadipscing elitr
 * FOO
 * sed diam nonumy eirmod tempor
 * </pre>
 * <p>
 * See this <a href="http://en.wikipedia.org/wiki/Processing_Instruction">Wikipedia article</a> for more informations
 * about processing instructions.
 * </p>
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
@NotThreadSafe
public interface PreProcessor {

    /**
     * Processes the given input string.
     *
     * @param input must not be {@code null}
     * @return never {@code null}
     */
    String process(String input);

    /**
     * Get the target name of the processor.
     * <p>
     * If you want to process instructions introduced with {@code <?foo} then you must return {@code foo} in
     * your implementation of this method.
     * <p>
     *
     * @return never {@code null}, never empty
     */
    String getTarget();

}
