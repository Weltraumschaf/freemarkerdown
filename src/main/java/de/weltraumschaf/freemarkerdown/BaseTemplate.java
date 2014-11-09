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

import de.weltraumschaf.commons.guava.Objects;
import de.weltraumschaf.commons.validate.Validate;
import freemarker.template.TemplateException;
import java.io.ByteArrayOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import net.jcip.annotations.NotThreadSafe;

/**
 * Common template functionality.
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
@NotThreadSafe
abstract class BaseTemplate implements TemplateModel {

    /**
     * Holds the assigned variables.
     */
    private final Variables templateVariables = new Variables();

    /**
     * Rendered template as original.
     */
    private final String template;

    /**
     * Encoding of rendered template string.
     */
    private final String encoding;

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
     * Injectable dependency.
     */
    private PreProcessorApplier preProcessorApplier = new PreProcessorApplierImpl();

    /**
     * Provides FreeMarker objects.
     */
    private FreeMarker factory = new FreeMarker();

    /**
     * Dedicated constructor.
     *
     * @param template must not be {@code null}
     * @param encoding must not be {@code null} or empty
     */
    BaseTemplate(final String template, final String encoding) {
        super();
        this.template = Validate.notNull(template, "template");
        this.preProcessedTemplate = this.template;
        this.encoding = Validate.notEmpty(encoding, "encoding");
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

    /**
     * Get all assigned variables.
     *
     * @return never {@code null}, unmodifiable
     */
    final Map<String, Object> getTemplateVariables() {
        return templateVariables.getVariables();
    }

    @Override
    public void assignVariable(final String name, final Object value) {
        templateVariables.assignVariable(name, value);
    }

    @Override
    public String render() {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            factory.createTemplate(preProcessedTemplate).process(
                    templateVariables.getVariables(),
                    new OutputStreamWriter(out, encoding));

            return out.toString(encoding);
        } catch (final IOException ex) {
            // Should never happen because we only operate on strings, not on files.
            throw new IOError(ex);
        } catch (final TemplateException ex) {
            throw new TemplateError(ex.getMessage(), ex);
        }
    }

    @Override
    public final void apply(final PreProcessor processor) {
        preProcessedTemplate = preProcessorApplier.apply(preProcessedTemplate, processor);
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(templateVariables, encoding, template);
    }

    @Override
    public final boolean equals(final Object obj) {
        if (!(obj instanceof BaseTemplate)) {
            return false;
        }

        final BaseTemplate other = (BaseTemplate) obj;
        return Objects.equal(templateVariables, other.templateVariables)
                && Objects.equal(encoding, other.encoding)
                && Objects.equal(template, other.template);
    }

    @Override
    public final String toString() {
        return "BaseTemplate{"
                + "templateVariables=" + templateVariables + ", "
                + "template=" + template + ", "
                + "encoding=" + encoding + ", "
                + "preProcessedTemplate=" + preProcessedTemplate
                + '}';
    }

}
