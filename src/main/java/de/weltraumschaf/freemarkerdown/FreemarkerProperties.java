/*
 * LICENSE
 *
 * "THE BEER-WARE LICENSE" (Revision 43):
 * "Sven Strittmatter" <weltraumschaf(at)googlemail(dot)com> wrote this file.
 * As long as you retain this notice you can do whatever you want with
 * this stuff. If we meet some day, and you think this stuff is worth it,
 * you can buy me a non alcohol-free beer in return.
 *
 * Copyright (C) 2012 "Sven Strittmatter" <weltraumschaf(at)googlemail(dot)com>
 */
package de.weltraumschaf.freemarkerdown;

import de.weltraumschaf.commons.validate.Validate;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import net.jcip.annotations.NotThreadSafe;

/**
 * Provides some FreeMarker related properties from file.
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
@NotThreadSafe
final class FreemarkerProperties {

    /**
     * Location of properties files in library ({@value}).
     */
    private static final String FILE = "/de/weltraumschaf/freemarkerdown/freemarker.properties";

    /**
     * Location of property file.
     */
    private final String propertyFileName;

    /**
     * Properties.
     */
    private final Properties properties = new Properties();

    /**
     * Convenience constructor which loads file from {@link #FILE fixed location}.
     */
    FreemarkerProperties() {
        this(FILE);
    }

    /**
     * Dedicated constructor.
     *
     * @param propertyFileName must not be {@code null} or empty
     */
    FreemarkerProperties(final String propertyFileName) {
        super();
        this.propertyFileName = Validate.notEmpty(propertyFileName, "propertyFileName");
        load();
    }

    /**
     * Opens the properties file and loads it.
     *
     * Only loads th property file once.
     */
    private void load() {
        try (final InputStream in = getClass().getResourceAsStream(propertyFileName)) {
            properties.load(in);
        } catch (final IOException | NullPointerException ex) {
            throw new IOError(ex);
        }
    }

    /**
     * Get the version string.
     *
     * @return The version string.
     */
    public String getVersion() {
        return properties.getProperty(PropertyNames.VERSION.toString(),
                PropertyNames.VERSION.getDefaultValue());
    }

    /**
     * Returns the version string.
     *
     * @return Same as {@link #getVersion()}.
     */
    @Override
    public String toString() {
        return getVersion();
    }

    /**
     * Available properties in the file.
     */
    private enum PropertyNames {

        /**
         * Version property.
         */
        VERSION("version", "n/a");

        /**
         * Property name.
         */
        private final String name;

        /**
         * Default value.
         */
        private final String defaultValue;

        /**
         * Initializes the enum with the property name.
         *
         * @param name The property name.
         * @param defaultValue Fall back value.
         */
        PropertyNames(final String name, final String defaultValue) {
            this.name = name;
            this.defaultValue = defaultValue;
        }

        /**
         * Returns the property name.
         *
         * @return String containing the property name.
         */
        @Override
        public String toString() {
            return name;
        }

        /**
         * Get the fallback value.
         *
         * @return Return default value string.
         */
        public String getDefaultValue() {
            return defaultValue;
        }

    }
}
