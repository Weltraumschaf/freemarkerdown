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

/**
 * Common template functionality.
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
abstract class BaseTemplate implements Renderable, Assignable {

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
     */
    private String preProcessedTemplate;

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

    @Override
    public void assignVariable(final String name, final Object value) {
        templateVariables.assignVariable(name, value);
    }

    @Override
    public String render() {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            FreeMarker.createTemplate(preProcessedTemplate).process(
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
    public void apply(final PreProcessor processor) {
        preProcessedTemplate = new PreProcessorApplier(preProcessedTemplate).apply(processor);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(templateVariables, encoding, template, preProcessedTemplate);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof BaseTemplate)) {
            return false;
        }

        final BaseTemplate other = (BaseTemplate) obj;
        return Objects.equal(templateVariables, other.templateVariables)
            && Objects.equal(encoding, other.encoding)
            && Objects.equal(template, other.template)
            && Objects.equal(preProcessedTemplate, other.preProcessedTemplate);
    }

}
