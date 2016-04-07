package de.weltraumschaf.freemarkerdown;

import net.jcip.annotations.NotThreadSafe;

/**
 * A layout is a template which may have subtemplates.
 * <p>
 * Whit layouts you can cascade {@link TemplateModel}:
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
 *  final FreeMarkerDown fmd = FreeMarkerDown.create();

  final Layout mainLayout = fmd.newLayout(
        "...\n"
      + "${fragmentOne}\n"
      + "...\n"
      + "${subLayout}\n");

      final Fragment fragmentOne = fmd.newFragemnt("foo");
      mainLayout.assignTemplateModel("fragmentOne", fragmentOne);

      final Layout subLayout = fmd.newLayout(
          + "${fragmentTwo}\n"
          + "...\n"
          + "${fragmentThree}\n");
      final Fragment fragmentTwo = fmd.newFragemnt("bar");
      subLayout.assignTemplateModel("fragmentTwo", fragmentTwo);
      final Fragment fragmentThree = fmd.newFragemnt("baz");
      subLayout.assignTemplateModel("fragmentThree", fragmentThree);

      mainLayout.assignTemplateModel("subLayout", subLayout);

      System.out.println(fmd.create().render(mainLayout));
 }</pre>
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
 * @author Sven Strittmatter
 */
@NotThreadSafe
public interface Layout extends TemplateModel {

    /**
     * Assigns a fragment to the layout.
     * <p>
     * The content of the rendered fragment will be assigned to the layout's template before it will be rendered.
     * </p>
     *
     * @param name must not be {@code null} or empty
     * @param fragment must not be {@code null}
     */
    void assignTemplateModel(String name, TemplateModel fragment);
    /**
     * Assigns a fragment to the layout.
     *
     * @see #assignTemplateModel(java.lang.String, de.weltraumschaf.freemarkerdown.TemplateModel)
     * @param name must not be {@code null}
     * @param fragment must not be {@code null}
     */
    void assignTemplateModel(VariableName name, TemplateModel fragment);

}
