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

import de.weltraumschaf.commons.guava.Lists;
import de.weltraumschaf.commons.guava.Sets;
import de.weltraumschaf.commons.validate.Validate;
import freemarker.template.Configuration;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
    private final List<PreProcessor> preProcessors = Lists.newArrayList();

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
        super();
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
     * @return never {@code null}
     */
    public String render(final TemplateModel template) {
        Validate.notNull(template, "template");
        preprocessTemplate(template);
        return renderTemplate(template);
    }

    /**
     * Execute all registered preprocessors.
     *
     * @param template must not be {@code null}
     */
    private void preprocessTemplate(final TemplateModel template) {
        Validate.notNull(template, "template");

        for (final PreProcessor preProcessor : preProcessors) {
            template.apply(preProcessor);
        }
    }

    /**
     * Render FreeMarker templates.
     *
     * @param template must not be {@code null}
     * @return never {@code null}
     */
    private String renderTemplate(final TemplateModel template) {
        Validate.notNull(template, "template");

        final String rendered = template.render();

        return rendered == null ? "" : rendered;
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
     * @param options optional options
     * @return never {@code null}, always new instance
     */
    public Fragment createFragemnt(final String template, final Options... options) {
        return createFragemnt(template, DEFAULT_ENCODING, options);
    }

    /**
     * Creates a new {@link Fragment}.
     *
     * @param template must not be {@code null}
     * @param encoding or empty
     * @param options optional options
     * @return never {@code null}, always new instance
     */
    public Fragment createFragemnt(final String template, final String encoding, final Options... options) {
        return new FragmentImpl(
                template,
                encoding,
                freeMarkerConfig,
                null == options
                        ? Collections.<Options>emptySet()
                        : Sets.newHashSet(options));
    }

    /**
     * Creates a new {@link Fragment} with {@link #DEFAULT_ENCODING default encoding}.
     *
     * @param template must not be {@code null}
     * @param options optional options
     * @return never {@code null}, always new instance
     * @throws IOException if file can't be read
     */
    public Fragment createFragemnt(final Path template, final Options... options) throws IOException {
        return createFragemnt(template, DEFAULT_ENCODING, options);
    }

    /**
     * Creates a new {@link Fragment}.
     *
     * @param template must not be {@code null}
     * @param encoding or empty
     * @param options optional options
     * @return never {@code null}, always new instance
     * @throws IOException if file can't be read
     */
    public Fragment createFragemnt(final Path template, final String encoding, final Options... options) throws IOException {
        return createFragemnt(read(template, encoding), encoding, options);
    }

    /**
     * Creates a new {@link Layout} with {@link #DEFAULT_ENCODING default encoding}.
     *
     * @param template must not be {@code null}
     * @param options optional options
     * @return never {@code null}, always new instance
     */
    public Layout createLayout(final String template, final Options... options) {
        return createLayout(template, DEFAULT_ENCODING, options);
    }

    /**
     * Creates a new {@link Layout}.
     *
     * @param template must not be {@code null}
     * @param encoding or empty
     * @param options optional options
     * @return never {@code null}, always new instance
     */
    public Layout createLayout(final String template, final String encoding, final Options... options) {
        return new LayoutImpl(
                template,
                encoding,
                freeMarkerConfig,
                null == options
                        ? Collections.<Options>emptySet()
                        : Sets.newHashSet(options));
    }

    /**
     * Creates a new {@link Layout} with {@link #DEFAULT_ENCODING default encoding}.
     *
     * @param template must not be {@code null}
     * @param options optional options
     * @return never {@code null}, always new instance
     * @throws IOException if file can't be read
     */
    public Layout createLayout(final Path template, final Options... options) throws IOException {
        return createLayout(template, DEFAULT_ENCODING, options);
    }

    /**
     * Creates a new {@link Layout}.
     *
     * @param template must not be {@code null}
     * @param encoding or empty
     * @param options optional options
     * @return never {@code null}, always new instance
     * @throws IOException if file can't be read
     */
    public Layout createLayout(final Path template, final String encoding, final Options... options) throws IOException {
        return createLayout(read(template, encoding), encoding, options);
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
        return create(createConfiguration());
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
