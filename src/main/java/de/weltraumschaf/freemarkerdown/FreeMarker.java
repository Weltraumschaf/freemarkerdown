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
import java.io.IOException;

/**
 * Helper to create FreeMarker objects.
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
class FreeMarker {

    /**
     * Provide some properties from fixed file.
     */
    private final FreemarkerProperties properties = new FreemarkerProperties();
    /**
     * Used version.
     */
    private final Version version = new Version(properties.getVersion());

    /**
     * Creates an original FreeMarker template with empty name and null configuration.
     *
     * @param template must not be {@code null}
     * @return always new instance
     * @throws IOException Should never happen, because we do not read templates from file.
     */
    Template createTemplate(final String template) throws IOException {
        return createTemplate(template, createConfiguration());
    }

    /**
     * Creates an original FreeMarker template with empty name and null configuration.
     *
     * @param template must not be {@code null}
     * @param config must not be {@code null}
     * @return always new instance
     * @throws IOException Should never happen, because we do not read templates from file.
     */
    Template createTemplate(final String template, final Configuration config) throws IOException {
        return new Template("", Validate.notNull(template, "template"), Validate.notNull(config, "config"));
    }

    /**
     * Create configuration.
     *
     * @return never {@code null}, always new instance.
     */
    Configuration createConfiguration() {
        final Configuration cfg = new Configuration(version);

        cfg.setObjectWrapper(createDefaultObjectWrapper());
        cfg.setDefaultEncoding(Defaults.ENCODING.getValue());

        return cfg;
    }

    /**
     * Create default object wrapper.
     *
     * @return never {@code null}, always same instance.
     */
    DefaultObjectWrapper createDefaultObjectWrapper() {
        return new DefaultObjectWrapperBuilder(version).build();
    }

}
