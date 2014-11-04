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
import freemarker.template.TemplateException;
import java.io.IOException;

/**
 * Extends the {@link Template template} to provide a two step layout.
 * <p>
 * Provides base variables such as title, encoding etc to the layout and inner content template.
 * </p>
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Layout extends Template {

    /**
     * Inner template to render content string.
     */
    private Renderable content = new DefaultContent();

    /**
     * Initializes all provided template variables with empty strings as default.
     *
     * @param templateConfiguration must not be {@code null}
     * @param layoutTemplateFile must not be {@code null}
     * @throws IOException if template can't be opened
     */
    public Layout(final Configuration templateConfiguration, final String layoutTemplateFile, final String encoding) throws IOException {
        super(templateConfiguration, layoutTemplateFile, encoding);
    }

    /**
     * Set the inner content template.
     *
     * @param content must not be {@code null}
     */
    public void setContent(final Renderable content) {
        Validate.notNull(content, "Content template must not be null!");
        this.content = content;
    }

    /**
     * Set the content string.
     *
     * @param content must not be {@code null}
     */
    public void setContent(final String content) {
        Validate.notNull(content);
        setContent(new DefaultContent(content));
    }

    @Override
    public String render() throws IOException, TemplateException {
        assignVariable("content", content.render());

        return super.render();
    }

    /**
     * Used as a default template which does only contains a string.
     */
    private static final class DefaultContent implements Renderable {

        /**
         * String which will be used as rendered output.
         */
        private final String content;

        /**
         * Initializes the rendered output with an empty string.
         */
        public DefaultContent() {
            this("");
        }

        /**
         * Dedicated constructor.
         *
         * @param content must not be {@code null}
         */
        public DefaultContent(final String content) {
            super();
            Validate.notNull(content, "Content must not be null!");
            this.content = content;
        }

        @Override
        public String render() throws IOException, TemplateException {
            return content;
        }

    }
}
