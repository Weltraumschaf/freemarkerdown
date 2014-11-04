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
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Extends the {@link Fragment template} to provide a two step layout.
 * <p>
 * Provides base variables such as title, encoding etc to the layout and inner content template.
 * </p>
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Layout extends BaseTemplate {

    private final Map<String, Fragment> fragments = Maps.newHashMap();

    public Layout(String template) {
        this(template, Defaults.ENCODING.getValue());
    }

    public Layout(String template, String encoding) {
        super(template, encoding);
    }

    public void assignFragment(final String name, final Fragment fragment) {
        fragments.put(name, fragment);
    }

    @Override
    public String render() throws IOException, TemplateException {
        for (final Map.Entry<String, Fragment> fragment : fragments.entrySet()) {
            assignVariable(fragment.getKey(), fragment.getValue().render());
        }

        return super.render();
    }

}
