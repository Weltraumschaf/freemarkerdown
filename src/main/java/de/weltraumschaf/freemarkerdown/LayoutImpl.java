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
import java.util.Map;
import net.jcip.annotations.NotThreadSafe;

/**
 * Simple implementation of a two step layout.
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
@NotThreadSafe
final class LayoutImpl extends BaseTemplate implements Layout {

    /**
     * Holds the layout fragments.
     */
    private final Map<String, TemplateModel> fragments = Maps.newHashMap();

    /**
     * Dedicated constructor.
     *
     * @param template must not be {@code null}
     * @param encoding must not be {@code null} or empty
     * @param freeMarkerConfig must not be {@code null}
     * @param options optional options
     */
    LayoutImpl(final String template, final String encoding, final Configuration freeMarkerConfig, final Options... options) {
        super(template, encoding, freeMarkerConfig, options);
    }

    @Override
    public void assignVariable(final String name, final Object value) {
        super.assignVariable(name, value);

        for (final TemplateModel subTemplate : fragments.values()) {
            subTemplate.assignVariable(name, value);
        }
    }

    @Override
    public void assignTemplateModel(final String name, final TemplateModel template) {
        fragments.put(Validate.notEmpty(name, "name"), Validate.notNull(template, "template"));

        for (final Map.Entry<String, Object> variable : getTemplateVariables().entrySet()) {
            template.assignVariable(variable.getKey(), variable.getValue());
        }
    }

    @Override
    public String render() {
        for (final Map.Entry<String, TemplateModel> fragment : fragments.entrySet()) {
            assignVariable(fragment.getKey(), fragment.getValue().render());
        }

        return super.render();
    }

    @Override
    public void apply(final PreProcessor processor) {
        super.apply(processor);

        for (final TemplateModel fragment : fragments.values()) {
            fragment.apply(processor);
        }
    }
}
