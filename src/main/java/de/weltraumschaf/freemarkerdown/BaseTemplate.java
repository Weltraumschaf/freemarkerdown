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
import de.weltraumschaf.commons.guava.Objects;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.freemarkerdown.Interceptor.ExecutionPoint;
import static de.weltraumschaf.freemarkerdown.Interceptor.ExecutionPoint.*;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.ByteArrayOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Set;
import net.jcip.annotations.NotThreadSafe;
import org.pegdown.PegDownProcessor;

/**
 * Common template functionality.
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
@NotThreadSafe
abstract class BaseTemplate extends EventProducer implements TemplateModel {

    /**
     * Holds the assigned variables.
     */
    private final VariableScope templateVariables = new VariableScope();

    /**
     * Used to convert Markdown to HTML.
     * <p>
     * Not included into {@link #hashCode()} and {@link #equals(java.lang.Object)} because not a value, but service
     * object.
     * </p>
     */
    private final PegDownProcessor markdown = new PegDownProcessor();

    /**
     * Rendered template as original.
     */
    private final String template;

    /**
     * Encoding of rendered template string.
     */
    private final String encoding;

    /**
     * Configures FreeMarker.
     */
    private final Configuration freeMarkerConfig;

    /**
     * Rendering options.
     */
    private final Set<RenderOptions> options;

    /**
     * Unique name to identify the template.
     */
    private final String name;

    private final Collection<EventConsumer> listeners = Lists.newArrayList();

    /**
     * Injectable dependency.
     */
    private PreProcessorApplier preProcessorApplier = new PreProcessorApplierImpl();

    /**
     * Pre processed template.
     * <p>
     * Not considered as "state" of the template and thus not included in {@link #hashCode()} and
     * {@link #equals(java.lang.Object)}. This field is modified by each
     * {@link #apply(de.weltraumschaf.freemarkerdown.PreProcessor) applied} pre processor.
     * </p>
     */
    private String preProcessedTemplate;

    /**
     * Provides FreeMarker objects.
     */
    private FreeMarker factory = new FreeMarker();

    /**
     * Dedicated constructor.
     *
     * @param template must not be {@code null}
     * @param encoding must not be {@code null} or empty
     * @param freeMarkerConfig must not be {@code null} or empty
     * @param options must not be {@code null}
     */
    BaseTemplate(final String template, final String encoding, final Configuration freeMarkerConfig, final Set<RenderOptions> options, final String name) {
        super();
        this.template = Validate.notNull(template, "template");
        this.preProcessedTemplate = this.template;
        this.encoding = Validate.notEmpty(encoding, "encoding");
        this.freeMarkerConfig = Validate.notNull(freeMarkerConfig, "freeMarkerConfig");
        this.options = Validate.notNull(options, "options");
        this.name = Validate.notEmpty(name, "name");
    }

    /**
     * Injection point for factory.
     *
     * @param factory must not be {@code null}
     */
    final void setFactory(final FreeMarker factory) {
        this.factory = Validate.notNull(factory, "factory");
    }

    /**
     * Injection point for applier.
     *
     * @param preProcessorApplier must not be {@code null}
     */
    final void setPreProcessorApplier(final PreProcessorApplier preProcessorApplier) {
        this.preProcessorApplier = Validate.notNull(preProcessorApplier, "preProcessorApplier");
    }

    /**
     * The preprocessed template contains the current template after
     * {@link #apply(de.weltraumschaf.freemarkerdown.PreProcessor) pre processing} it.
     *
     * @return never {@code null}
     */
    final String getPreProcessedTemplate() {
        return preProcessedTemplate;
    }

    final void setParent(final BaseTemplate parent) {
        templateVariables.setParent(parent.templateVariables);
    }

    @Override
    public final void assignVariable(final String name, final Object value) {
        templateVariables.assignVariable(name, value);
    }

    @Override
    public String render() {
        triggerEvent(BEFORE_RENDERING);
        String content = processTemplate();
        triggerEvent(AFTER_RENDERING, content);

        if (options.contains(RenderOptions.WITHOUT_MARKDOWN)) {
            return content;
        }

        triggerEvent(BEFORE_MARKDOWN, content);
        content = convertMarkdown(content);
        triggerEvent(AFTER_MARKDOWN, content);

        return content;
    }

    /**
     * Processes the FreeMarker template.
     * <p>
     * Throws {@link TemplateError} if template can't be rendered.
     * </p>
     *
     * @return never {@code null}
     */
    private String processTemplate() throws TemplateError, IOError {
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            factory.createTemplate(preProcessedTemplate, freeMarkerConfig)
                    .process(
                            templateVariables.getData(),
                            new OutputStreamWriter(out, encoding));
            return out.toString(encoding);
        } catch (final IOException ex) {
            // Should never happen because we only operate on strings, not on files.
            throw new IOError(ex);
        } catch (final TemplateException ex) {
            throw new TemplateError(ex.getMessage(), ex);
        }
    }

    /**
     * Markdown conversion.
     *
     * @param template must not be {@code null}
     * @param rendered must not be {@code null}
     * @return
     */
    private String convertMarkdown(final String rendered) {
        return markdown.markdownToHtml(Validate.notNull(rendered, "rendered"));
    }

    @Override
    public void apply(final PreProcessor processor) {
        triggerEvent(BEFORE_PREPROCESSING);
        preProcessedTemplate = preProcessorApplier.apply(preProcessedTemplate, processor);
        triggerEvent(AFTER_PREPROCESSING, preProcessedTemplate);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    void register(final EventConsumer consumer) {
        listeners.add(Validate.notNull(consumer, "consumer"));
    }

    @Override
    void unregister(final EventConsumer consumer) {
        listeners.remove(Validate.notNull(consumer, "consumer"));
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(templateVariables, encoding, template, name);
    }

    @Override
    public final boolean equals(final Object obj) {
        if (!(obj instanceof BaseTemplate)) {
            return false;
        }

        final BaseTemplate other = (BaseTemplate) obj;
        return Objects.equal(templateVariables, other.templateVariables)
                && Objects.equal(encoding, other.encoding)
                && Objects.equal(template, other.template)
                && Objects.equal(name, other.name);
    }

    String toStringProperties() {
        return "templateVariables=" + templateVariables + ", "
                + "template=" + template + ", "
                + "encoding=" + encoding + ", "
                + "name=" + name + ", "
                + "preProcessedTemplate=" + preProcessedTemplate;
    }

    @Override
    public String toString() {
        return "BaseTemplate{" + toStringProperties() + '}';
    }

    private void triggerEvent(final ExecutionPoint executionPoint) {
        triggerEvent(executionPoint, "");
    }

    private void triggerEvent(final ExecutionPoint executionPoint, final String content) {
        Validate.notNull(executionPoint, "executionPoint");

        for (final EventConsumer listener : listeners) {
            listener.trigger(new Event(executionPoint, this, content));
        }
    }

}
