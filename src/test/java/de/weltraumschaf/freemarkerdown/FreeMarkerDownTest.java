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
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link FreeMarkerDown}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
@Ignore
public class FreeMarkerDownTest {

    private final FreeMarkerDown sut = new FreeMarkerDown();

    @Test(expected = NullPointerException.class)
    public void register_preProcesorMustNotBeNull() {
        sut.register((PreProcessor) null);
    }

    @Test(expected = NullPointerException.class)
    public void register_postProcesorMustNotBeNull() {
        sut.register((PostProcessor) null);
    }

    @Test
    public void render_fragment() {
        final Fragment renderable = new Fragment(
                "<?foo\n"
                + "foo bar baz\n"
                + "?>\n"
                + "Lorem ipsum dolor.\n"
                + "<?bar snafu ?>bla blub"
        );

        assertThat(sut.render(renderable), is("Lorem ipsum dolor.\nbla blub"));
    }

    @Test
    public void render_fragment_withPreProcessors() {
        final Fragment renderable = new Fragment(
                "<?foo\n"
                + "foo bar baz\n"
                + "?>\n"
                + "Lorem ipsum dolor.\n"
                + "<?bar snafu ?>bla blub"
        );
        final PreProcessor one = mock(PreProcessor.class);
        when(one.getTarget()).thenReturn("foo");
        sut.register(one);
        final PreProcessor two = mock(PreProcessor.class);
        when(two.getTarget()).thenReturn("bar");
        sut.register(two);

        assertThat(sut.render(renderable), is(
                "foo\n"
                + "Lorem ipsum dolor.\n"
                + "barbla blub"));
    }

    @Test
    public void render_fragment_withPostProcessors() {
        final Fragment renderable = new Fragment(
                "<?foo\n"
                + "foo bar baz\n"
                + "?>\n"
                + "Lorem ipsum dolor.\n"
                + "<?bar snafu ?>bla blub"
        );
        final PostProcessor one = mock(PostProcessor.class);
        when(one.getTarget()).thenReturn("foo");
        sut.register(one);
        final PostProcessor two = mock(PostProcessor.class);
        when(two.getTarget()).thenReturn("bar");
        sut.register(two);

        assertThat(sut.render(renderable), is(
                "foo\n"
                + "Lorem ipsum dolor.\n"
                + "barbla blub"
        ));
    }

    @Test
    @Ignore
    public void render_layoutWithFragments() {
        final Layout renderable = new Layout(
                "<?foo\n"
                + "foo bar baz\n"
                + "?>\n"
                + "Lorem ipsum dolor.\n"
                + "<?bar snafu ?>bla blub"
        );

        assertThat(sut.render(renderable), is(""));
    }

    @Test
    @Ignore
    public void render_layoutWithFragments_withPreProcessors() {
        final Layout renderable = new Layout(
                "<?foo\n"
                + "foo bar baz\n"
                + "?>\n"
                + "Lorem ipsum dolor.\n"
                + "<?bar snafu ?>bla blub"
        );
        final PreProcessor one = mock(PreProcessor.class);
        when(one.getTarget()).thenReturn("foo");
        sut.register(one);
        final PreProcessor two = mock(PreProcessor.class);
        when(two.getTarget()).thenReturn("bar");
        sut.register(two);

        assertThat(sut.render(renderable), is(""));
    }

    @Test
    @Ignore
    public void render_layoutWithFragments_withPostProcessors() {
        final Layout renderable = new Layout(
                "<?foo\n"
                + "foo bar baz\n"
                + "?>\n"
                + "Lorem ipsum dolor.\n"
                + "<?bar snafu ?>bla blub"
        );
        final PostProcessor one = mock(PostProcessor.class);
        when(one.getTarget()).thenReturn("foo");
        sut.register(one);
        final PostProcessor two = mock(PostProcessor.class);
        when(two.getTarget()).thenReturn("bar");
        sut.register(two);

        assertThat(sut.render(renderable), is(""));
    }

}
