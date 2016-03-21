package de.weltraumschaf.freemarkerdown;

/**
 * A template model is the base to render templates.
 * <p>
 * A model can {@link #render() render} itself and apply {@link #apply(de.weltraumschaf.freemarkerdown.PreProcessor) pre
 * processors}. Also you can {@link #assignVariable(java.lang.String, java.lang.Object) assign variables}.
 * </p>
 * <p>
 * Maybe you ask: "Why didn't call this thing simply Template?" Yes I considered that, but I didn't want a name
 * conflicting with the name of the FreeMarker template class. This was the best idea I came up with.
 * </p>
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public interface TemplateModel {

    /**
     * Renders the template in a string.
     * <p>
     * Throws {@link TemplateError} if template can't be parsed.
     * </p>
     *
     * @return never {@code null}
     */
    String render();

    /**
     * Apply a pre processor on a renderable.
     *
     * @param processor must not be {@code null}
     */
    void apply(PreProcessor processor);

    /**
     * Assign any object as template variable.
     *
     * @param name must not be {@code null} or empty
     * @param value must not be {@code null}
     */
    void assignVariable(final String name, final Object value);

    /**
     * Get the name of template.
     * <p>
     * This is only useful to determine which template you get in a {@link PreProcessor}
     * or {@link Interceptor}.
     *
     * @return never {@code null} or empty
     */
    String getName();
}
