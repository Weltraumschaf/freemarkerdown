package de.weltraumschaf.freemarkerdown;

import net.jcip.annotations.NotThreadSafe;

/**
 * Implementations provide a assignable variable name.
 * <p>
 * With this interface you can enumerate your template variables:
 * </p>
 * <pre>{@code
 * enum Variables implements VariableName {
 *     FOO("foo"),
 *     BAR("bar"),
 *     BAZ("baz");
 *
 *     private final String variableName;
 *
 *     private Variables(final String variableName) {
 *         this.variableName = variableName;
 *     }
 *
 *     &#064;Override
 *     public String getVariableName() {
 *         return variableName;
 *     }
 * }
 * }</pre>
 * <p>
 * This you can use to assign values to tempates:
 * </p>
 * <pre>{@code
 * TemplateModel template = ...
 * template.assignVariable(Variables.FOO, "This is foo!");
 * }</pre>
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
@NotThreadSafe
public interface VariableName {

    /**
     * Returns the literal variable name.
     *
     * @return never {@code null} or empty
     */
    String getVariableName();
}
