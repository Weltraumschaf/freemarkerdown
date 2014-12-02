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

import de.weltraumschaf.commons.guava.Lists;
import de.weltraumschaf.commons.guava.Sets;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link Layout}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class LayoutTest {

    @Test(expected = NullPointerException.class)
    public void render_nullTemplate() {
        new LayoutImpl(
                null,
                Defaults.ENCODING.getValue(),
                FREE_MARKER.createConfiguration(),
                Collections.<RenderOptions>emptySet());
    }
    private static final FreeMarker FREE_MARKER = new FreeMarker();

    @Test
    public void render_emptyTemplate() throws IOException, TemplateException {
        assertThat(
                new LayoutImpl(
                        "",
                        Defaults.ENCODING.getValue(),
                        FREE_MARKER.createConfiguration(),
                        Collections.<RenderOptions>emptySet()).render(),
                is(""));
    }

    @Test
    public void render_notEmptyTemplate() throws IOException, TemplateException {
        assertThat(
                new LayoutImpl(
                        "foo bar baz",
                        Defaults.ENCODING.getValue(),
                        FREE_MARKER.createConfiguration(),
                        Collections.<RenderOptions>emptySet()).render(),
                is("<p>foo bar baz</p>"));
    }

    @Test
    public void render_notEmptyTemplateWithVariables() throws IOException, TemplateException {
        final List<String> fruits = Lists.newArrayList();
        fruits.add("bananas");
        fruits.add("apples");
        fruits.add("pears");
        final Layout sut = new LayoutImpl(
                "<p>And BTW we have these fruits:\n"
                + "<ul>\n"
                + "<#list fruits as fruit>\n"
                + " <li>${fruit}</li>\n"
                + "</#list>\n"
                + "<ul>",
                Defaults.ENCODING.getValue(),
                FREE_MARKER.createConfiguration(),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN));
        sut.assignVariable("fruits", fruits);

        assertThat(sut.render(),
                is("<p>And BTW we have these fruits:\n"
                        + "<ul>\n"
                        + " <li>bananas</li>\n"
                        + " <li>apples</li>\n"
                        + " <li>pears</li>\n"
                        + "<ul>"));
    }

    @Test
    public void render_withOneFragement() throws IOException, TemplateException {
        final Layout sut = new LayoutImpl("<p>${fragmentOne}</p>\n",
                Defaults.ENCODING.getValue(),
                FREE_MARKER.createConfiguration(),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN));
        sut.assignTemplateModel("fragmentOne",
                new FragmentImpl("foo",
                        Defaults.ENCODING.getValue(),
                        FREE_MARKER.createConfiguration(),
                        Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN)));

        assertThat(sut.render(), is("<p>foo</p>\n"));
    }

    @Test
    public void render_withThreeFragement() throws IOException, TemplateException {
        final Layout sut = new LayoutImpl(
                "<p>${fragmentOne}</p>\n"
                + "<p>${fragmentTwo}</p>\n"
                + "<p>${fragmentThree}</p>\n",
                Defaults.ENCODING.getValue(),
                FREE_MARKER.createConfiguration(),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN)
        );
        sut.assignTemplateModel("fragmentOne", new FragmentImpl(
                "foo",
                Defaults.ENCODING.getValue(),
                FREE_MARKER.createConfiguration(),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN)));
        sut.assignTemplateModel("fragmentTwo", new FragmentImpl(
                "bar",
                Defaults.ENCODING.getValue(),
                FREE_MARKER.createConfiguration(),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN)));
        sut.assignTemplateModel("fragmentThree", new FragmentImpl(
                "baz",
                Defaults.ENCODING.getValue(),
                FREE_MARKER.createConfiguration(),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN)));

        assertThat(sut.render(), is(
                "<p>foo</p>\n"
                + "<p>bar</p>\n"
                + "<p>baz</p>\n"
        ));
    }

    @Test
    public void render_withLayoutInside() {
        final Layout inside = new LayoutImpl(
                "<p>foobar</p>\n",
                Defaults.ENCODING.getValue(),
                FREE_MARKER.createConfiguration(),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN)
        );
        final Layout sut = new LayoutImpl(
                "<h1>snafu</h1>\n"
                + "${inside}",
                Defaults.ENCODING.getValue(),
                FREE_MARKER.createConfiguration(),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN));
        sut.assignTemplateModel("inside", inside);

        assertThat(sut.render(), is(
                "<h1>snafu</h1>\n"
                + "<p>foobar</p>\n"
        ));
    }

    @Test
    public void render_withLayoutInsideWhichHasFragments() {
        final Layout inside = new LayoutImpl(
                "<p>${fragmentOne}</p>\n"
                + "<p>${fragmentTwo}</p>\n"
                + "<p>${fragmentThree}</p>\n",
                Defaults.ENCODING.getValue(),
                FREE_MARKER.createConfiguration(),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN)
        );
        inside.assignTemplateModel("fragmentOne", new FragmentImpl(
                "foo",
                Defaults.ENCODING.getValue(),
                FREE_MARKER.createConfiguration(),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN)));
        inside.assignTemplateModel("fragmentTwo", new FragmentImpl(
                "bar",
                Defaults.ENCODING.getValue(),
                FREE_MARKER.createConfiguration(),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN)));
        inside.assignTemplateModel("fragmentThree", new FragmentImpl(
                "baz",
                Defaults.ENCODING.getValue(),
                FREE_MARKER.createConfiguration(),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN)));
        final Layout sut = new LayoutImpl(
                "<h1>snafu</h1>\n"
                + "${inside}",
                Defaults.ENCODING.getValue(),
                FREE_MARKER.createConfiguration(),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN));
        sut.assignTemplateModel("inside", inside);

        assertThat(sut.render(), is(
                "<h1>snafu</h1>\n"
                + "<p>foo</p>\n"
                + "<p>bar</p>\n"
                + "<p>baz</p>\n"
        ));
    }
}
