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
import java.util.Map;
import net.jcip.annotations.NotThreadSafe;

/**
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
@NotThreadSafe
final class LayoutImpl extends BaseTemplate implements Layout {

    /**
     * Holds the layout fragments.
     */
    private final Map<String, Renderable> fragments = Maps.newHashMap();

    /**
     * Dedicated constructor.
     *
     * @param template must not be {@code null}
     * @param encoding must not be {@code null} or empty
     */
    LayoutImpl(final String template, final String encoding) {
        super(template, encoding);
    }

    @Override
    public void assignFragment(final String name, final Renderable fragment) {
        fragments.put(Validate.notEmpty(name, "name"), Validate.notNull(fragment, "fragment"));
    }

    @Override
    public String render() {
        for (final Map.Entry<String, Renderable> fragment : fragments.entrySet()) {
            assignVariable(fragment.getKey(), fragment.getValue().render());
        }

        return super.render();
    }
}
