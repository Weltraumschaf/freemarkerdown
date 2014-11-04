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
import freemarker.template.TemplateException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
abstract class BaseTemplate implements Renderable, Assignable {

    /**
     * Rendered template.
     */
    private final freemarker.template.Template template;
    /**
     * Holds the assigned variables.
     */
    private final Variables templateVariables = new Variables();

    /**
     * Encoding of rendered template string.
     */
    private final String encoding;

    /**
     * Creates a template from an template string.
     *
     * @param template must not be {@code null}
     * @param encoding must not be {@code null}
     */
    BaseTemplate(final String template, final String encoding) {
        this(FreeMarker.createTemplate(template), encoding);
    }

    /**
     * Dedicated constructor.
     *
     * @param template must not be {@code null}
     * @param encoding must not be {@code null} or empty
     */
    private BaseTemplate(final freemarker.template.Template template, final String encoding) {
        super();
        this.template = Validate.notNull(template, "template");
        this.encoding = Validate.notEmpty(encoding, "encoding");
    }

    @Override
    public void assignVariable(final String name, final Object value) {
        templateVariables.assignVariable(name, value);
    }

    @Override
    public String render() throws IOException, TemplateException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        template.process(templateVariables.getVariables(), new OutputStreamWriter(out, encoding));

        return out.toString(encoding);
    }

}
