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

import freemarker.template.Configuration;
import java.util.Set;
import net.jcip.annotations.NotThreadSafe;

/**
 * Basic implementation of a fragment.
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
@NotThreadSafe
final class FragmentImpl extends BaseTemplate implements Fragment {

    /**
     * Dedicated constructor.
     *
     * @param template must not be {@code null}
     * @param encoding must not be {@code null} or empty
     * @param freeMarkerConfig must not be {@code null}
     * @param options must not be {@code null}
     * @param templateName must not be {@code null} or empty
     */
    public FragmentImpl(
            final String template,
            final String encoding,
            final Configuration freeMarkerConfig,
            final Set<RenderOptions> options,
            final String templateName) {
        super(template, encoding, freeMarkerConfig, options, templateName);
    }

    @Override
    public String toString() {
        return "Fragment{" + toStringProperties() + '}';
    }
}
