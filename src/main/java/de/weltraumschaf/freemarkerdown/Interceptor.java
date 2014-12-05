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

/**
 * An interceptor can hook into the FreeMarkerDown execution process.
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public interface Interceptor {

    /**
     * Called from {@link FreeMarkerDown} on particular execution points.
     * <p>
     * You are not provided with the execution point. You should register own implementations for
     * each execution point, rather than using the same for multiple points in execution. This leads
     * smaller and simpler objects.
     * </p>
     *
     * @param point never {@code null}, execution point of interception
     * @param template never {@code null}, rendered template
     * @param content never {@code null}, maybe empty, any rendered/converted content
     */
    void intercept(final ExecutionPoint point, final TemplateModel template, final String content);

    /**
     * Execution point a interceptor con hook into.
     *
     * @since 1.0.0
     */
    public static enum ExecutionPoint {
        /**
         * This is executed before preprocessors will be executed.
         */
        BEFORE_PREPROCESSING,
        /**
         * This is executed after preprocessors were executed.
         */
        AFTER_PREPROCESSING,
        /**
         * This is executed before FreeMarker templates will be rendered.
         */
        BEFORE_RENDERING,
        /**
         * This is executed after FreeMarker templates were be rendered.
         */
        AFTER_RENDERING,
        /**
         * This is executed before PegDown converts the content from Makrdown to HTML.
         */
        BEFORE_MARKDOWN,
        /**
         * This is executed after PegDown converts the content from Makrdown to HTML.
         */
        AFTER_MARKDOWN;
    }
}
