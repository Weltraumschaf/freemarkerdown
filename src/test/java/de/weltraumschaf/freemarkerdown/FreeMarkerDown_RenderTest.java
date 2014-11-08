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
public class FreeMarkerDown_RenderTest {

    private final FreeMarkerDown sut = FreeMarkerDown.create();

    @Test(expected = NullPointerException.class)
    public void render_nulPassedIn() {
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
                Defaults.ENCODING.getValue()
        );

        assertThat(sut.render(renderable), is(
                "<?foo\n"
                + "foo bar baz\n"
                + "?>\n"
                + "Lorem ipsum dolor.\n"
                + "<?bar snafu ?> bla blub"));
    }

    @Test
    public void render_fragment_withPreProcessors() {
        final Fragment renderable = new FragmentImpl(
                "<?foo\n"
                + "foo bar baz\n"
                + "?>\n"
                + "Lorem ipsum dolor.\n"
                + "<?bar snafu ?> bla blub",
                Defaults.ENCODING.getValue()
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
                "foo\n"
                + "Lorem ipsum dolor.\n"
                + "bar bla blub"));
    }

    @Test
    public void render_layoutWithFragments() {
        final Layout renderable = new LayoutImpl(
                "<?foo\n"
                + "foo bar baz\n"
                + "?>\n"
                + "Lorem ipsum dolor.\n"
                + "<?bar snafu ?> bla blub"
        );

        assertThat(sut.render(renderable), is(
                "<?foo\n"
                + "foo bar baz\n"
                + "?>\n"
                + "Lorem ipsum dolor.\n"
                + "<?bar snafu ?> bla blub"));
    }

    @Test
    public void render_layoutWithFragments_withPreProcessors() {
        final Layout renderable = new LayoutImpl(
                "<?foo\n"
                + "foo bar baz\n"
                + "?>\n"
                + "Lorem ipsum dolor.\n"
                + "<?bar snafu ?> bla blub"
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
                "foo\n"
                + "Lorem ipsum dolor.\n"
                + "bar bla blub"));
    }
}