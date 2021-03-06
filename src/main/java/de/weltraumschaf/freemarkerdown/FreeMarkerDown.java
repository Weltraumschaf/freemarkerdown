package de.weltraumschaf.freemarkerdown;

import de.weltraumschaf.commons.guava.Lists;
import de.weltraumschaf.commons.guava.Sets;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.freemarkerdown.Interceptor.ExecutionPoint;
import freemarker.template.Configuration;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import net.jcip.annotations.NotThreadSafe;

/**
 * This is the main API entry point to render stuff.
 * <p>
 * Use the {@link #create(java.lang.String)} or {@link #create(freemarker.template.Configuration)} to create an
 * instance.
 * </p>
 * <p>
 * For examples see the <a href="http://weltraumschaf.github.io/freemarkerdown/examples.html">this site</a>
 * </p>
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
@NotThreadSafe
public final class FreeMarkerDown {

    /**
     * Holds the pre processors keyed by name.
     */
    private final List<PreProcessor> preProcessors = Lists.newArrayList();

    /**
     * Configures FreeMarker.
     */
    private final Configuration freeMarkerConfig;

    /**
     * Dispatches events to registered interceptors.
     */
    private final EventDispatcher events;

    /**
     * Use {@link #create(java.lang.String)} or {@link #create(freemarker.template.Configuration)} to create new
     * instances.
     *
     * @param freeMarkerConfig must not be {@code null}
     * @param events must not be {@code null}
     */
    FreeMarkerDown(final Configuration freeMarkerConfig, final EventDispatcher events) {
        super();
        this.freeMarkerConfig = Validate.notNull(freeMarkerConfig, "freeMarkerConfig");
        this.events = Validate.notNull(events, "events");
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
     * Registers an interceptor for an execution point.
     *
     * @param interceptor must not be {@code null}
     * @param point must not be {@code null}
     */
    public void register(final Interceptor interceptor, final ExecutionPoint point) {
        events.register(interceptor, point);
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

        registerForEvents(template);
        preprocessTemplate(template);
        final String content = renderTemplate(template);
        unregisterForEvents(template);

        return content;
    }

    /**
     * Register the event dispatcher to receive events from the template model.
     *
     * @param template must not be {@code null}
     */
    private void registerForEvents(final TemplateModel template) {
        if (template instanceof EventProducer) {
            ((EventProducer) template).register(events);
        }
    }

    /**
     * Unregister the event dispatcher to receive events from the template model.
     *
     * @param template must not be {@code null}
     */
    private void unregisterForEvents(final TemplateModel template) {
        if (template instanceof EventProducer) {
            ((EventProducer) template).unregister(events);
        }
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
     * Creates a new {@link Fragment}.
     *
     * @param template must not be {@code null}
     * @param options optional options
     * @param name must not be {@code null} or empty
     * @return never {@code null}, always new instance
     */
    public Fragment createFragemnt(final String template, final String name, final RenderOptions... options) {
        return new FragmentImpl(
            template,
            freeMarkerConfig.getDefaultEncoding(),
            freeMarkerConfig,
            null == options
                ? Collections.<RenderOptions>emptySet()
                : Sets.newHashSet(options),
            name
        );
    }

    /**
     * Creates a new {@link Fragment} with the template file name as template name.
     *
     * @param template must not be {@code null}
     * @param options optional options
     * @return never {@code null}, always new instance
     * @throws IOException if file can't be read
     */
    public Fragment createFragemnt(final Path template, final RenderOptions... options) throws IOException {
        return createFragemnt(template, template.toString(), options);
    }

    /**
     * Creates a new {@link Fragment}.
     *
     * @param template must not be {@code null}
     * @param name must not be {@code null} or empty
     * @param options optional options
     * @return never {@code null}, always new instance
     * @throws IOException if file can't be read
     */
    public Fragment createFragemnt(final Path template, final String name, final RenderOptions... options) throws IOException {
        return createFragemnt(read(template), name, options);
    }

    /**
     * Creates a new {@link Layout}.
     *
     * @param template must not be {@code null}
     * @param options optional options
     * @param name must not be {@code null} or empty
     * @return never {@code null}, always new instance
     */
    public Layout createLayout(final String template, final String name, final RenderOptions... options) {
        return new LayoutImpl(
            template,
            freeMarkerConfig.getDefaultEncoding(),
            freeMarkerConfig,
            null == options
                ? Collections.<RenderOptions>emptySet()
                : Sets.newHashSet(options),
            name
        );
    }

    /**
     * Creates a new {@link Layout} with the template file name as template name.
     *
     * @param template must not be {@code null}
     * @param options optional options
     * @return never {@code null}, always new instance
     * @throws IOException if file can't be read
     */
    public Layout createLayout(final Path template, final RenderOptions... options) throws IOException {
        return createLayout(template, template.toString(), options);
    }

    /**
     * Creates a new {@link Layout}.
     *
     * @param template must not be {@code null}
     * @param name must not be {@code null} or empty
     * @param options optional options
     * @return never {@code null}, always new instance
     * @throws IOException if file can't be read
     */
    public Layout createLayout(final Path template, final String name, final RenderOptions... options) throws IOException {
        return createLayout(read(template), name, options);
    }

    /**
     * Read file into string with configured encoding.
     *
     * @param template must not be {@code null}
     * @return never {@code null}
     * @throws IOException if file can't be read
     */
    private String read(final Path template) throws IOException {
        Validate.notNull(template, "template");
        return new String(Files.readAllBytes(template), freeMarkerConfig.getDefaultEncoding());
    }

    /**
     * Factory method.
     *
     * @param encoding must not be {@code null} or empty
     * @return never {@code null}, always new instance
     */
    public static FreeMarkerDown create(final String encoding) {
        return create(createConfiguration(encoding));
    }

    /**
     * Factory method.
     *
     * @param freeMarkerConfig must not be {@code null}
     * @return never {@code null}, always new instance
     */
    public static FreeMarkerDown create(final Configuration freeMarkerConfig) {
        return new FreeMarkerDown(freeMarkerConfig, new EventDispatcher());
    }

    /**
     * Factory method to create FreeMarker template configuration.
     *
     * @param encoding must not be {@code null} or empty
     * @return never {@code null}, may return same instance
     */
    public static Configuration createConfiguration(final String encoding) {
        return new FreeMarker().createConfiguration(encoding);
    }

}
