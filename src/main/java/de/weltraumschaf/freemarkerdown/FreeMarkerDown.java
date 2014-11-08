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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import net.jcip.annotations.NotThreadSafe;

/**
 * This is the main API entry point to render stuff.
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
@NotThreadSafe
public final class FreeMarkerDown {

    /**
     * This is the default encoding used for all string operations.
     */
    public static final String DEFAULT_ENCODING = Defaults.ENCODING.getValue();

    /**
     * Holds the pre processors keyed by name.
     */
    private final List<PreProcessor> preProcessors = new ArrayList<>();

    /**
     * Use {@link #create()} to create new instances.
     */
    private FreeMarkerDown() {
        super();
    }

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

    /**
     * Get a copy of the registered pre processors.
     *
     * @return never {@code null}, immutable
     */
    Collection<PreProcessor> getPreProcessors() {
        return Collections.unmodifiableList(preProcessors);
    }

    /**
     * Render the given template.
     *
     * @param template must not be {@code null}
     * @return never {@code null}
     */
    public String render(final Renderable template) {
        Validate.notNull(template, "template");

        for (final PreProcessor preProcessor : preProcessors) {
            template.apply(preProcessor);
        }

        // TODO Add Markdown generation here.
        return template.render();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.preProcessors);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof FreeMarkerDown)) {
            return false;
        }

        final FreeMarkerDown other = (FreeMarkerDown) obj;
        return Objects.equals(preProcessors, other.preProcessors);
    }

    @Override
    public String toString() {
        return "FreeMarkerDown{" + "preProcessors=" + preProcessors + '}';
    }

    /**
     * Creates a new {@link Fragment} with {@link #DEFAULT_ENCODING default encoding}.
     *
     * @param template must not be {@code null}
     * @return never {@code null}, always new instance
     */
    public Fragment newFragemnt(final String template) {
        return newFragemnt(template, DEFAULT_ENCODING);
    }

    /**
     * Creates a new {@link Fragment}.
     *
     * @param template must not be {@code null}
     * @param encoding or empty
     * @return never {@code null}, always new instance
     */
    public Fragment newFragemnt(final String template, final String encoding) {
        return new FragmentImpl(template, encoding);
    }

    /**
     * Creates a new {@link Layout} with {@link #DEFAULT_ENCODING default encoding}.
     *
     * @param template must not be {@code null}
     * @return never {@code null}, always new instance
     */
    public Layout newLayout(final String template) {
        return newLayout(template, DEFAULT_ENCODING);
    }

    /**
     * Creates a new {@link Layout}.
     *
     * @param template must not be {@code null}
     * @param encoding or empty
     * @return never {@code null}, always new instance
     */
    public Layout newLayout(final String template, final String encoding) {
        return new LayoutImpl(template, encoding);
    }

    /**
     * Factory method.
     *
     * @return never {@code null}, always new instance
     */
    public static FreeMarkerDown create() {
        return new FreeMarkerDown();
    }
}
