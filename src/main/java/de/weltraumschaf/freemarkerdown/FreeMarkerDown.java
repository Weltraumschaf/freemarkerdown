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
import java.util.HashMap;
import java.util.Map;

/**
 * This is the main API entry point to render stuff.
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class FreeMarkerDown {

    /**
     * Holds the pre processors keyed by name.
     */
    private final Map<String, PreProcessor> preprocessors = new HashMap<>();
    /**
     * Holds the pre processors keyed by name.
     */
    private final Map<String, PostProcessor> postrocessors = new HashMap<>();

    /**
     * Registers a pre processor.
     * <p>
     * Preprocessors are called before any rendering.
     * </p>
     *
     * @param processor must not be {@code null}
     */
    public void register(final PreProcessor processor) {
        Validate.notNull(processor, "processor");
        preprocessors.put(processor.getTarget(), processor);
    }

    /**
     * Registers a post processor.
     * <p>
     * Preprocessors are called after any rendering.
     * </p>
     *
     * @param processor must not be {@code null}
     */
    public void register(final PostProcessor processor) {
        Validate.notNull(processor, "processor");
        postrocessors.put(processor.getTarget(), processor);
    }

    public String render(final Renderable template) {
        return "";
    }
}
