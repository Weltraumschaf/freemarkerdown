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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.jcip.annotations.NotThreadSafe;
import org.pegdown.PegDownProcessor;

/**
 * This is the main API entry point to render stuff.
 * <p>
 * Use the {@link #create() factory method} to create an instance.
 * </p>
 * <p>
 * For examples see the <a href="http://weltraumschaf.github.io/freemarkerdown/examples.html">this site</a>
 * </p>
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
     * Used to convert Markdown to HTML.
     * <p>
     * Not included into {@link #hashCode()} and {@link #equals(java.lang.Object)}
     * because not a value, but service object.
     * </p>
     */
    private final PegDownProcessor markdown;

    /**
     * Use {@link #create()} to create new instances.
     */
    private FreeMarkerDown() {
        this(new PegDownProcessor());
    }

    /**
     * Dedicated constructor.
     *
     * @param markdown must not be {@code null}
     */
    private FreeMarkerDown(final PegDownProcessor markdown) {
        super();
        this.markdown = Validate.notNull(markdown, "markdown");
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
     * @param options optional options
     * @return never {@code null}
     */
    public String render(final TemplateModel template, final Options ... options) {
        Validate.notNull(template, "template");
        final Set<Options> opt = options == null
                ? Collections.<Options>emptySet()
                : new HashSet<>(Arrays.asList(options));

        for (final PreProcessor preProcessor : preProcessors) {
            template.apply(preProcessor);
        }

        final String rendered = template.render();

        if (opt.contains(Options.WITHOUT_MARKDOWN)) {
            return rendered;
        }

        return markdown.markdownToHtml(rendered);
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
    public Fragment createFragemnt(final String template) {
        return FreeMarkerDown.this.createFragemnt(template, DEFAULT_ENCODING);
    }

    /**
     * Creates a new {@link Fragment}.
     *
     * @param template must not be {@code null}
     * @param encoding or empty
     * @return never {@code null}, always new instance
     */
    public Fragment createFragemnt(final String template, final String encoding) {
        return new FragmentImpl(template, encoding);
    }

    /**
     * Creates a new {@link Layout} with {@link #DEFAULT_ENCODING default encoding}.
     *
     * @param template must not be {@code null}
     * @return never {@code null}, always new instance
     */
    public Layout createLayout(final String template) {
        return FreeMarkerDown.this.createLayout(template, DEFAULT_ENCODING);
    }

    /**
     * Creates a new {@link Layout}.
     *
     * @param template must not be {@code null}
     * @param encoding or empty
     * @return never {@code null}, always new instance
     */
    public Layout createLayout(final String template, final String encoding) {
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
