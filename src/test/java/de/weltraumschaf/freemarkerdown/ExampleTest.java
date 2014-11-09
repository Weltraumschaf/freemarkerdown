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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Integration tests.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class ExampleTest {

    @Test
    @Ignore
    public void exampleOne() {
        // START SNIPPET: exampleOne
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
        // END SNIPPET: exampleOne
    }
}
