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
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.Version;
import java.io.IOError;
import java.io.IOException;

/**
 * Helper to create FreeMarker objects.
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class FreeMarker {

    /**
     * Provide some properties from fixed file.
     */
    private static final FreemarkerProperties properties = new FreemarkerProperties();
    private static final Version version = new Version(properties.getVersion());

    /**
     * Hidden for pure static class.
     */
    private FreeMarker() {
        super();
        throw new UnsupportedOperationException("Do not call via reflection!");
    }

    /**
     * Creates an original FreeMarker template with empty name and null configuration.
     *
     * @param template must not be {@code null}
     * @return always new instance
     */
    static Template createTemplate(final String template) {
        try {
            return new Template("", Validate.notNull(template, "template"), createConfiguration());
        } catch (final IOException ex) {
            throw new IOError(ex);
        }
    }

    static DefaultObjectWrapper createDefaultObjectWrapper() {
        return new DefaultObjectWrapperBuilder(version).build();
    }

    static Configuration createConfiguration() {
        final Configuration cfg = new Configuration(version);

        cfg.setObjectWrapper(FreeMarker.createDefaultObjectWrapper());
        cfg.setDefaultEncoding(Defaults.ENCODING.getValue());

        return cfg;
    }

}
