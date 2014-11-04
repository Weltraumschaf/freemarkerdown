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
import freemarker.template.TemplateException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

/**
 * Fragment class which encapsulates FreeMarker.
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Fragment implements Renderable, Assignable {

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

    public Fragment(final String template) {
        this(template, Defaults.ENCODING.getValue());
    }

    /**
     * Creates a template from an template string.
     *
     * @param template must not be {@code null}
     * @param encoding must not be {@code null}
     */
    public Fragment(final String template, final String encoding) {
        this(FreeMarker.createTemplate(template), encoding);
    }

    /**
     * Dedicated constructor.
     *
     * @param template must not be {@code null}
     * @param encoding must not be {@code null} or empty
     */
    Fragment(final freemarker.template.Template template, final String encoding) {
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

    @Override
    public String render() throws IOException, TemplateException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        template.process(templateVariables, new OutputStreamWriter(out, encoding));

        return out.toString(encoding);
    }

}
