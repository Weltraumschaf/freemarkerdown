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

import java.util.Collections;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link FreeMarkerDown}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class FreeMarkerDown_RenderMarkdownTest extends TestCaseBase {

    private final FreeMarkerDown sut = FreeMarkerDown.create(ENCODING);

    @Test(expected = NullPointerException.class)
    public void render_nullPassedIn() {
        sut.render(null);
    }

    @Test
    public void render_fragment() {
        final Fragment renderable = new FragmentImpl(
                "<?foo\n"
                + "foo bar baz\n"
                + "?>\n"
                + "Lorem ipsum dolor.\n"
                + "<?bar snafu ?> bla blub",
                ENCODING,
                new FreeMarker().createConfiguration(ENCODING),
                Collections.<RenderOptions>emptySet(),
                "name"
        );

        assertThat(sut.render(renderable), is(
                "<p>&lt;?foo foo bar baz ?&gt; Lorem ipsum dolor. &lt;?bar snafu ?&gt; bla blub</p>"));
    }

    @Test
    public void render_fragment_withPreProcessors() {
        final Fragment renderable = new FragmentImpl(
                "<?foo\n"
                + "foo bar baz\n"
                + "?>\n"
                + "Lorem ipsum dolor.\n"
                + "<?bar snafu ?> bla blub",
                ENCODING,
                new FreeMarker().createConfiguration(ENCODING),
                Collections.<RenderOptions>emptySet(),
                "name"
        );
        final PreProcessor one = mock(PreProcessor.class);
        when(one.getTarget()).thenReturn("foo");
        when(one.process("\nfoo bar baz\n")).thenReturn("foo");
        sut.register(one);
        final PreProcessor two = mock(PreProcessor.class);
        when(two.getTarget()).thenReturn("bar");
        when(two.process(" snafu ")).thenReturn("bar");
        sut.register(two);

        assertThat(sut.render(renderable), is(
                "<p>foo Lorem ipsum dolor. bar bla blub</p>"));
    }

    @Test
    public void render_layoutWithFragments() {
        final Layout renderable = new LayoutImpl(
                "<?foo\n"
                + "foo bar baz\n"
                + "?>\n"
                + "Lorem ipsum dolor.\n"
                + "<?bar snafu ?> bla blub",
                ENCODING,
                new FreeMarker().createConfiguration(ENCODING),
                Collections.<RenderOptions>emptySet(),
                "name"
        );

        assertThat(sut.render(renderable), is(
                "<p>&lt;?foo foo bar baz ?&gt; Lorem ipsum dolor. &lt;?bar snafu ?&gt; bla blub</p>"));
    }

    @Test
    public void render_layoutWithFragments_withPreProcessors() {
        final Layout renderable = new LayoutImpl(
                "<?foo\n"
                + "foo bar baz\n"
                + "?>\n"
                + "Lorem ipsum dolor.\n"
                + "<?bar snafu ?> bla blub",
                ENCODING,
                new FreeMarker().createConfiguration(ENCODING),
                Collections.<RenderOptions>emptySet(),
                "name"
        );
        final PreProcessor one = mock(PreProcessor.class);
        when(one.getTarget()).thenReturn("foo");
        when(one.process("\nfoo bar baz\n")).thenReturn("foo");
        sut.register(one);
        final PreProcessor two = mock(PreProcessor.class);
        when(two.getTarget()).thenReturn("bar");
        when(two.process(" snafu ")).thenReturn("bar");
        sut.register(two);

        assertThat(sut.render(renderable), is(
                "<p>foo Lorem ipsum dolor. bar bla blub</p>"));
    }
}
