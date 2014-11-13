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

import de.weltraumschaf.commons.guava.Sets;
import de.weltraumschaf.commons.validate.Validate;
import freemarker.template.Configuration;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
     * Configures FreeMarker.
     */
    private final Configuration freeMarkerConfig;

    /**
     * Use {@link #create()} to create new instances.
     *
     * @param freeMarkerConfig must not be {@code null}
     */
    private FreeMarkerDown(final Configuration freeMarkerConfig) {
        this(new PegDownProcessor(), freeMarkerConfig);
    }

    /**
     * Dedicated constructor.
     *
     * @param markdown must not be {@code null}
     * @param freeMarkerConfig must not be {@code null}
     */
    private FreeMarkerDown(final PegDownProcessor markdown, final Configuration freeMarkerConfig) {
        super();
        this.markdown = Validate.notNull(markdown, "markdown");
        this.freeMarkerConfig = Validate.notNull(freeMarkerConfig, "freeMarkerConfig");
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
                : Sets.newHashSet(options);

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
        return createFragemnt(template, DEFAULT_ENCODING);
    }

    /**
     * Creates a new {@link Fragment}.
     *
     * @param template must not be {@code null}
     * @param encoding or empty
     * @return never {@code null}, always new instance
     */
    public Fragment createFragemnt(final String template, final String encoding) {
        return new FragmentImpl(template, encoding, freeMarkerConfig);
    }

    /**
     * Creates a new {@link Fragment} with {@link #DEFAULT_ENCODING default encoding}.
     *
     * @param template must not be {@code null}
     * @return never {@code null}, always new instance
     * @throws IOException if file can't be read
     */
    public Fragment createFragemnt(final Path template) throws IOException {
        return createFragemnt(template, DEFAULT_ENCODING);
    }

    /**
     * Creates a new {@link Fragment}.
     *
     * @param template must not be {@code null}
     * @param encoding or empty
     * @return never {@code null}, always new instance
     * @throws IOException if file can't be read
     */
    public Fragment createFragemnt(final Path template, final String encoding) throws IOException {
        return createFragemnt(read(template, encoding), encoding);
    }

    /**
     * Creates a new {@link Layout} with {@link #DEFAULT_ENCODING default encoding}.
     *
     * @param template must not be {@code null}
     * @return never {@code null}, always new instance
     */
    public Layout createLayout(final String template) {
        return createLayout(template, DEFAULT_ENCODING);
    }

    /**
     * Creates a new {@link Layout}.
     *
     * @param template must not be {@code null}
     * @param encoding or empty
     * @return never {@code null}, always new instance
     */
    public Layout createLayout(final String template, final String encoding) {
        return new LayoutImpl(template, encoding, freeMarkerConfig);
    }

    /**
     * Creates a new {@link Layout} with {@link #DEFAULT_ENCODING default encoding}.
     *
     * @param template must not be {@code null}
     * @return never {@code null}, always new instance
     * @throws IOException if file can't be read
     */
    public Layout createLayout(final Path template) throws IOException {
        return createLayout(template, DEFAULT_ENCODING);
    }

    /**
     * Creates a new {@link Layout}.
     *
     * @param template must not be {@code null}
     * @param encoding or empty
     * @return never {@code null}, always new instance
     * @throws IOException if file can't be read
     */
    public Layout createLayout(final Path template, final String encoding) throws IOException {
        return createLayout(read(template, encoding), encoding);
    }

    /**
     * Read file into string with encoding.
     *
     * @param template must not be {@code null}
     * @param encoding must not be {@code null} or empty
     * @return never {@code null}
     * @throws IOException if file can't be read
     */
    private String read(final Path template, final String encoding) throws IOException {
        Validate.notNull(template, "template");
        Validate.notEmpty(encoding, "encoding");
        return new String(Files.readAllBytes(template), encoding);
    }

    /**
     * Factory method.
     *
     * @return never {@code null}, always new instance
     */
    public static FreeMarkerDown create() {
        return create(new FreeMarker().createConfiguration());
    }

    /**
     * Factory method.
     *
     * @param freeMarkerConfig must not be {@code null}
     * @return never {@code null}, always new instance
     */
    public static FreeMarkerDown create(final Configuration freeMarkerConfig) {
        return new FreeMarkerDown(freeMarkerConfig);
    }

    /**
     * Factory method to create FreeMarker template configuration.
     *
     * @return never {@code null}, may return same instance
     */
    public static Configuration createConfiguration() {
        return new FreeMarker().createConfiguration();
    }
}
