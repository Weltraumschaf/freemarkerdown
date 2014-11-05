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
import java.io.IOException;
import java.util.Map;

/**
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Layout extends BaseTemplate {

    /**
     * Holds the layout fragments.
     */
    private final Map<String, Renderable> fragments = Maps.newHashMap();

    /**
     * Convenience constructor which sets the encoding to {@link Defaults#ENCODING}.
     *
     * @param template must not be {@code null}
     */
    public Layout(String template) {
        this(template, Defaults.ENCODING.getValue());
    }

    /**
     * Dedicated constructor.
     *
     * @param template must not be {@code null}
     * @param encoding must not be {@code null} or empty
     */
    public Layout(String template, String encoding) {
        super(template, encoding);
    }

    /**
     * Assigns a fragment to the layout.
     * <p>
     * The content of the rendered fragment will be assigned to the layout's template before
     * it will be rendered.
     * </p>
     *
     * @param name must not be {@code null} or empty
     * @param fragment must not be {@code null}
     */
    public void assignFragment(final String name, final Renderable fragment) {
        fragments.put(Validate.notEmpty(name, "name"), Validate.notNull(fragment, "fragment"));
    }

    @Override
    public String render() throws IOException, TemplateException {
        for (final Map.Entry<String, Renderable> fragment : fragments.entrySet()) {
            assignVariable(fragment.getKey(), fragment.getValue().render());
        }

        return super.render();
    }

}
