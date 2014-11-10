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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * Integration tests.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class ExamplesTest {

    @Test
    public void exampleWithAllFeatures() {
        // START SNIPPET: exampleWithAllFeatures
        final FreeMarkerDown fmd = FreeMarkerDown.create();

        final Fragment template = fmd.createFragemnt(
                "# A Title\n"
                + "\n"
                + "<?fdm-keyvalue\n"
                + "    key1: value one\n"
                + "    key2: 42\n"
                + "?>\n"
                + "\n"
                + "Lorem ipsum dolor sit amet.\n"
                + "\n"
                + "## A List\n"
                + "\n"
                + "<#list sequence as item>\n"
                + "- ${item}\n"
                + "</#list>");
        template.assignVariable("sequence", Arrays.asList("foo", "bar", "baz"));

        final Map<String, String> keyValues = new HashMap<>();
        final PreProcessor processor = PreProcessors.createKeyValueProcessor(keyValues);
        fmd.register(processor);

        assertThat(fmd.render(template), is(
                "<h1>A Title</h1>"
                + "<p>Lorem ipsum dolor sit amet.</p>"
                + "<h2>A List</h2>\n"
                + "<ul>\n"
                + "  <li>foo</li>\n"
                + "  <li>bar</li>\n"
                + "  <li>baz</li>\n"
                + "</ul>"));
        assertThat(keyValues.size(), is(2));
        assertThat(keyValues, allOf(hasEntry("key1", "value one"), hasEntry("key2", "42")));
        // END SNIPPET: exampleWithAllFeatures
    }

    @Test
    public void renderSingleFragmentwithMarkdownFromStringTemplate() {
        // START SNIPPET: renderSingleFragmentwithMarkdownFromStringTemplate
        final FreeMarkerDown fmd = FreeMarkerDown.create();

        final Fragment template = fmd.createFragemnt(
                "# A Title\n"
                + "\n"
                + "Lorem ipsum dolor sit amet.\n"
                + "\n"
                + "## A List\n"
                + "\n"
                + "<#list sequence as item>\n"
                + "- ${item}\n"
                + "</#list>");
        template.assignVariable("sequence", Arrays.asList("foo", "bar", "baz"));

        assertThat(fmd.render(template), is(
                "<h1>A Title</h1>"
                + "<p>Lorem ipsum dolor sit amet.</p>"
                + "<h2>A List</h2>\n"
                + "<ul>\n"
                + "  <li>foo</li>\n"
                + "  <li>bar</li>\n"
                + "  <li>baz</li>\n"
                + "</ul>"));
        // END SNIPPET: renderSingleFragmentwithMarkdownFromStringTemplate
    }

    @Test
    public void renderSingleFragmentwithMarkdownFromFileTemplate() throws IOException, URISyntaxException {
        // START SNIPPET: renderSingleFragmentwithMarkdownFromFileTemplate
        final FreeMarkerDown fmd = FreeMarkerDown.create();

        final URI file = getClass()
                .getResource("renderSingleFragmentwithMarkdownFromFileTemplate.md.ftl")
                .toURI();
        final Fragment template = fmd.createFragemnt(Paths.get(file));
        template.assignVariable("sequence", Arrays.asList("foo", "bar", "baz"));

        assertThat(fmd.render(template), is(
                "<h1>A Title</h1>"
                + "<p>Lorem ipsum dolor sit amet.</p>"
                + "<h2>A List</h2>\n"
                + "<ul>\n"
                + "  <li>foo</li>\n"
                + "  <li>bar</li>\n"
                + "  <li>baz</li>\n"
                + "</ul>"));
        // END SNIPPET: renderSingleFragmentwithMarkdownFromFileTemplate
    }

    @Test
    public void renderLayoutwithFragmentsandMarkdownFromStringTemplate() {
        // START SNIPPET: renderLayoutwithFragmentsandMarkdownFromStringTemplate
        final FreeMarkerDown fmd = FreeMarkerDown.create();

        final Layout layout = fmd.createLayout(
                "# A Title\n"
                + "\n"
                + "Lorem ipsum dolor sit amet.\n"
                + "\n"
                + "## A List\n"
                + "\n"
                + "<#list sequence as item>\n"
                + "- ${item}\n"
                + "</#list>\n"
                + "\n"
                + "${fragment}");
        layout.assignVariable("sequence", Arrays.asList("foo", "bar", "baz"));

        final Fragment fragment = fmd.createFragemnt("This is ${foo}.");
        fragment.assignVariable("foo", "bar");
        layout.assignTemplateModel("fragment", fragment);

        assertThat(fmd.render(layout), is(
                "<h1>A Title</h1>"
                + "<p>Lorem ipsum dolor sit amet.</p>"
                + "<h2>A List</h2>\n"
                + "<ul>\n"
                + "  <li>foo</li>\n"
                + "  <li>bar</li>\n"
                + "  <li>baz</li>\n"
                + "</ul>"
                + "<p>This is bar.</p>"));
        // END SNIPPET: renderLayoutwithFragmentsandMarkdownFromStringTemplate
    }

    @Test
    public void renderLayoutwithFragmentsandMarkdownFromFileTemplate() throws URISyntaxException, IOException {
        // START SNIPPET: renderLayoutwithFragmentsandMarkdownFromFileTemplate
        final FreeMarkerDown fmd = FreeMarkerDown.create();

        final URI fileLayout = getClass()
                .getResource("renderLayoutwithFragmentsandMarkdownFromStringTemplate_layout.md.ftl")
                .toURI();
        final Layout layout = fmd.createLayout(Paths.get(fileLayout));
        layout.assignVariable("sequence", Arrays.asList("foo", "bar", "baz"));

        final URI fileFragment = getClass()
                .getResource("renderLayoutwithFragmentsandMarkdownFromStringTemplate_fragment.md.ftl")
                .toURI();
        final Fragment fragment = fmd.createFragemnt(Paths.get(fileFragment));
        fragment.assignVariable("foo", "bar");
        layout.assignTemplateModel("fragment", fragment);

        assertThat(fmd.render(layout), is(
                "<h1>A Title</h1>"
                + "<p>Lorem ipsum dolor sit amet.</p>"
                + "<h2>A List</h2>\n"
                + "<ul>\n"
                + "  <li>foo</li>\n"
                + "  <li>bar</li>\n"
                + "  <li>baz</li>\n"
                + "</ul>"
                + "<p>This is bar.</p>"));
        // END SNIPPET: renderLayoutwithFragmentsandMarkdownFromFileTemplate
    }

    @Test
    public void renderWithoutMarkdown() {
        // START SNIPPET: renderWithoutMarkdown
        final FreeMarkerDown fmd = FreeMarkerDown.create();

        final Fragment template = fmd.createFragemnt(
                "# A Title\n"
                + "\n"
                + "Lorem ipsum dolor sit amet.\n"
                + "\n"
                + "## A List\n"
                + "\n"
                + "<#list sequence as item>\n"
                + "- ${item}\n"
                + "</#list>");
        template.assignVariable("sequence", Arrays.asList("foo", "bar", "baz"));

        assertThat(fmd.render(template, Options.WITHOUT_MARKDOWN), is(
                "# A Title\n"
                + "\n"
                + "Lorem ipsum dolor sit amet.\n"
                + "\n"
                + "## A List\n"
                + "\n"
                + "- foo\n"
                + "- bar\n"
                + "- baz\n"));
        // END SNIPPET: renderWithoutMarkdown
    }

    @Test
    public void renderWithPreprocessor() {
        // START SNIPPET: renderWithPreprocessor
        final FreeMarkerDown fmd = FreeMarkerDown.create();

        final Fragment template = fmd.createFragemnt(
                "# A Title\n"
                + "\n"
                + "<?fdm-keyvalue\n"
                + "    key1: value one\n"
                + "    key2: 42\n"
                + "?>\n"
                + "\n"
                + "Lorem ipsum dolor sit amet.\n");

        final Map<String, String> keyValues = new HashMap<>();
        final PreProcessor processor = PreProcessors.createKeyValueProcessor(keyValues);
        fmd.register(processor);

        assertThat(fmd.render(template), is(
                "<h1>A Title</h1>"
                + "<p>Lorem ipsum dolor sit amet.</p>"));

        assertThat(keyValues.size(), is(2));
        assertThat(keyValues, allOf(hasEntry("key1", "value one"), hasEntry("key2", "42")));

        if (processor.hasWarnings()) {
            for (final String warning : processor.getWarnings()) {
                // Handle warning ...
            }
        }
        // END SNIPPET: renderWithPreprocessor
    }

    @Test
    public void layoutPropagatesVariables() {
        // START SNIPPET: layoutPropagatesVariables
        final FreeMarkerDown fmd = FreeMarkerDown.create();

        final Layout layout = fmd.createLayout(
                "# A Title\n"
                + "\n"
                + "Lorem ipsum dolor sit amet.\n"
                + "\n"
                + "${fragment}");
        layout.assignVariable("sequence", Arrays.asList("foo", "bar", "baz"));

        final Fragment fragment = fmd.createFragemnt(
                "## A List\n"
                + "\n"
                + "<#list sequence as item>\n"
                + "- ${item}\n"
                + "</#list>\n"
                + "\n");
        layout.assignTemplateModel("fragment", fragment);

        assertThat(fmd.render(layout), is(
                "<h1>A Title</h1>"
                + "<p>Lorem ipsum dolor sit amet.</p>"
                + "<h2>A List</h2>\n"
                + "<ul>\n"
                + "  <li>foo</li>\n"
                + "  <li>bar</li>\n"
                + "  <li>baz</li>\n"
                + "</ul>"));
        // END SNIPPET: layoutPropagatesVariables
    }

    @Test
    public void handlingTemplateErrors() {
        // START SNIPPET: handlingTemplateErrors
        final FreeMarkerDown fmd = FreeMarkerDown.create();

        final Fragment fragment = fmd.createFragemnt("Lorem ipsum dolor: ${foo}");

        try {
            fmd.render(fragment);
        } catch (final TemplateError err) {
            assertThat(err.getMessage(), is(
                    "The following has evaluated to null or missing:\n"
                    + "==> foo  [in template \"\" at line 1, column 22]\n"
                    + "\n"
                    + "----\n"
                    + "Tip: If the failing expression is known to be legally refer to something that's null or missing, "
                    + "either specify a default value like myOptionalVar!myDefault, or use <#if myOptionalVar??>"
                    + "when-present<#else>when-missing</#if>. (These only cover the last step of the expression; to "
                    + "cover the whole expression, use parenthesis: (myOptionalVar.foo)!myDefault, (myOptionalVar.foo)??\n"
                    + "----\n"
                    + "\n"
                    + "----\n"
                    + "FTL stack trace (\"~\" means nesting-related):\n\t- Failed at: ${foo}  [in template \"\" at "
                    + "line 1, column 20]\n"
                    + "----"));
        }
        // END SNIPPET: handlingTemplateErrors
    }
}
