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

import de.weltraumschaf.commons.guava.Maps;
import de.weltraumschaf.commons.validate.Validate;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

/**
 * Template class which encapsulates FreeMarker.
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Template implements Renderable, Assignable {

    /**
     * Rendered template.
     */
    private final freemarker.template.Template template;
    /**
     * Holds the assigned variables.
     */
    private final Map<String, Object> templateVariables = Maps.newHashMap();
    /**
     * Encoding of rendered template string.
     */
    private final String encoding;

    /**
     * Creates a template from an template string.
     *
     * @param template must not be {@code null}
     * @param encoding must not be {@code null}
     * @throws IOException if template can't be opened
     */
    public Template(final String template, final String encoding) throws IOException {
        this(Freemarker.createTemplate(template), encoding);
    }

    /**
     * Dedicated constructor.
     *
     * @param templateConfiguration must not be {@code null}
     * @param templateFile must not be {@code null}
     * @param encoding must not be {@code null} or empty
     * @throws IOException if template can't be opened
     */
    public Template(final Configuration templateConfiguration, final String templateFile, final String encoding)
        throws IOException {
        this(templateConfiguration.getTemplate(templateFile), encoding);
    }

    /**
     * Dedicated constructor.
     *
     * @param template must not be {@code null}
     * @param encoding must not be {@code null} or empty
     * @throws IOException if template can't be opened
     */
    public Template(final freemarker.template.Template template, final String encoding)
        throws IOException {
        super();
        this.template = Validate.notNull(template, "template");
        this.encoding = Validate.notEmpty(encoding, "encoding");
    }

    /**
     * Assign any object as template variable.
     *
     * @param name must not be {@code null}
     * @param value must not be {@code null}
     */
    @Override
    public void assignVariable(final String name, final Object value) {
        templateVariables.put(Validate.notEmpty(name, "name"), Validate.notNull(value, "value"));
    }

    /**
     * Get a variable.
     *
     * @param name must not be {@code null} or empty
     * @return never {@code null}, maybe empty string
     */
    protected Object getVariable(final String name) {
        Validate.notEmpty(name, "name");

        if (templateVariables.containsKey(name)) {
            return templateVariables.get(name);
        }

        return "";
    }

    @Override
    public String render() throws IOException, TemplateException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        template.process(templateVariables, new OutputStreamWriter(out, encoding));

        return out.toString(encoding);
    }

}
