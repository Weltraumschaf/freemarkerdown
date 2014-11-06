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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class FreeMarkerDown {

    private final Map<String, PreProcessor> preprocessors = new HashMap<>();
    private final Map<String, PostProcessor> postrocessors = new HashMap<>();

    public void register(final PreProcessor processor) {
        preprocessors.put(processor.getName(), processor);
    }

    public void register(final PostProcessor processor) {
        postrocessors.put(processor.getName(), processor);
    }

    public String render(final Renderable template) {
        return "";
    }
}
