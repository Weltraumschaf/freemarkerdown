package de.weltraumschaf.freemarkerdown;

import de.weltraumschaf.commons.guava.Maps;
import de.weltraumschaf.commons.validate.Validate;
import freemarker.template.Configuration;
import java.util.Map;
import java.util.Set;
import net.jcip.annotations.NotThreadSafe;

/**
 * Simple implementation of a two step layout.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
@NotThreadSafe
final class LayoutImpl extends BaseTemplate implements Layout {

    /**
     * Holds the layout fragments.
     */
    private final Map<String, TemplateModel> fragments = Maps.newHashMap();

    /**
     * Dedicated constructor.
     *
     * @param template must not be {@code null}
     * @param encoding must not be {@code null} or empty
     * @param freeMarkerConfig must not be {@code null}
     * @param options must not be {@code null}
     * @param templateName must not be {@code null} or empty
     */
    LayoutImpl(
            final String template,
            final String encoding,
            final Configuration freeMarkerConfig,
            final Set<RenderOptions> options, final String templateName) {
        super(template, encoding, freeMarkerConfig, options, templateName);
    }

    @Override
    void register(final EventConsumer consumer) {
        super.register(consumer);

        for (final TemplateModel fragment : fragments.values()) {
            if (fragment instanceof BaseTemplate) {
                ((BaseTemplate) fragment).register(consumer);
            }
        }
    }

    @Override
    void unregister(final EventConsumer consumer) {
        super.unregister(consumer);

        for (final TemplateModel fragment : fragments.values()) {
            if (fragment instanceof BaseTemplate) {
                ((BaseTemplate) fragment).unregister(consumer);
            }
        }
    }

    @Override
    public void assignTemplateModel(final VariableName name, final TemplateModel template) {
        assignTemplateModel(name.getVariableName(), template);
    }

    @Override
    public void assignTemplateModel(final String name, final TemplateModel template) {
        if (template instanceof BaseTemplate) {
            ((BaseTemplate) template).setParent(this);
        }

        fragments.put(Validate.notEmpty(name, "name"), Validate.notNull(template, "template"));
    }

    @Override
    public String render() {
        for (final Map.Entry<String, TemplateModel> fragment : fragments.entrySet()) {
            assignVariable(fragment.getKey(), fragment.getValue().render());
        }

        return super.render();
    }

    @Override
    public void apply(final PreProcessor processor) {
        super.apply(processor);

        for (final TemplateModel fragment : fragments.values()) {
            fragment.apply(processor);
        }
    }

    @Override
    public String toString() {
        return "Layout{"
                + toStringProperties() + ", "
                + "fragments=" + fragments
                + '}';
    }
}
