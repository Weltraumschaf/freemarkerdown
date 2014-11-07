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
import java.util.ArrayList;
import java.util.List;
import net.jcip.annotations.NotThreadSafe;

/**
 * This is the main API entry point to render stuff.
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
@NotThreadSafe
public final class FreeMarkerDown {

    public static final String DEFAULT_ENCODING = Defaults.ENCODING.getValue();

    /**
     * Holds the pre processors keyed by name.
     */
    private final List<PreProcessor> preProcessors = new ArrayList<>();

    /**
     * Registers a pre processor.
     * <p>
     * Preprocessors are called before any rendering.
     * </p>
     *
     * @param processor must not be {@code null}
     */
    public void register(final PreProcessor processor) {
        Validate.notNull(processor, "processor");
        preProcessors.add(processor);
    }

    public String render(final Renderable template) {
        for (final PreProcessor preProcessor : preProcessors) {
            template.apply(preProcessor);
        }

        // TODO Add Markdown generation here.
        return template.render();
    }

    /**
     * Creates a new {@link Fragment} with {@link #DEFAULT_ENCODING default encoding}.
     *
     * @param template must not be {@code null}
     * @return never {@code null}, always new instance
     */
    public static Fragment newFragemnt(final String template) {
        return newFragemnt(template, DEFAULT_ENCODING);
    }

    /**
     * Creates a new {@link Fragment}.
     *
     * @param template  must not be {@code null}
     * @param encoding or empty
     * @return never {@code null}, always new instance
     */
    public static Fragment newFragemnt(final String template, final String encoding) {
        return new FragmentImpl(template, encoding);
    }

    /**
     * Creates a new {@link Layout} with {@link #DEFAULT_ENCODING default encoding}.
     *
     * @param template must not be {@code null}
     * @return never {@code null}, always new instance
     */
    public static Layout newLayout(final String template) {
        return newLayout(template, DEFAULT_ENCODING);
    }

    /**
     * Creates a new {@link Layout}.
     *
     * @param template  must not be {@code null}
     * @param encoding or empty
     * @return never {@code null}, always new instance
     */
    public static Layout newLayout(final String template, final String encoding) {
        return new LayoutImpl(template, encoding);
    }

}
