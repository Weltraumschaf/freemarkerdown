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
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public interface Layout extends Renderable, Assignable {

    /**
     * Assigns a fragment to the layout.
     * <p>
     * The content of the rendered fragment will be assigned to the layout's template before
     * it will be rendered.
     * </p>
     *
     * @param name must not be {@code null} or empty
     * @param fragment must not be {@code null}
     */
    public void assignFragment(String name, Renderable fragment);

}
