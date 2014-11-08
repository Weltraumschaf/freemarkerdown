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

import net.jcip.annotations.NotThreadSafe;

/**
 * A layout is a template which may have subtemplates.
 * <p>
 * Whit layouts you can cascade {@link Layouts layouts} and {@link Fragment fragments}:
 * </p>
 * <pre>
 * +-----------------------+
 * |      mainLayout       |
 * |    +-------------+    |
 * |    | fragmentOne |    |
 * |    +-------------+    |
 * | +-------------------+ |
 * | |     subLayout     | |
 * | | +---------------+ | |
 * | | |  fragmentTwo  | | |
 * | | +---------------+ | |
 * | | +---------------+ | |
 * | | | fragmentThree | | |
 * | | +---------------+ | |
 * | +-------------------+ |
 * +-----------------------+
 * </pre>
 * <p>
 * This results in a tree structure of templates:
 * </p>
 * <pre>
 *             [ mainLayout ]
 *                    |
 *        +-----------+-----------+
 *        |                       |
 * [ fragmentOne ]          [ subLayout ]
 *                                |
 *                    +-----------+-----------+
 *                    |                       |
 *              [ fragmentTwo  ]       [ fragmentThree ]
 * </pre>
 * <p>
 * The code to create a structure like this looks like:
 * </p>
 * <pre>
 * {@code
 *  final Layout mainLayout = FreeMarkerDown.newLayout(
 *        "...\n"
 *      + "${fragmentOne}\n"
 *      + "...\n"
 *      + "${subLayout}\n");
 *
 *      final Fragment fragmentOne = FreeMarkerDown.newFragemnt("foo");
 *      mainLayout.assignFragment("fragmentOne", fragmentOne);
 *
 *      final Layout subLayout = FreeMarkerDown.newLayout(
 *          + "${fragmentTwo}\n"
 *          + "...\n"
 *          + "${fragmentThree}\n");
 *      final Fragment fragmentTwo = FreeMarkerDown.newFragemnt("bar");
 *      subLayout.assignFragment("fragmentTwo", fragmentTwo);
 *      final Fragment fragmentThree = FreeMarkerDown.newFragemnt("baz");
 *      subLayout.assignFragment("fragmentThree", fragmentThree);
 *
 *      mainLayout.assignFragment("subLayout", subLayout);
 *
 *      System.out.println(FreeMarkerDown.create().render(mainLayout));
 * }</pre>
 * <p>
 * This will print something like:
 * </p>
 * <pre>
 * ...
 * foo
 * ...
 * bar
 * ...
 * baz
 * </pre>
 *
 * @since 1.0.0
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
@NotThreadSafe
public interface Layout extends Renderable, Assignable {


    /**
     * Assigns a fragment to the layout.
     * <p>
     * The content of the rendered fragment will be assigned to the layout's template before it will be rendered.
     * </p>
     *
     * @param name must not be {@code null} or empty
     * @param fragment must not be {@code null}
     */
    void assignFragment(String name, Renderable fragment);

}
