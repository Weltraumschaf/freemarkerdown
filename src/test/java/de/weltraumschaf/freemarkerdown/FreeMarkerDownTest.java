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
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link FreeMarkerDown}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class FreeMarkerDownTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private final FreeMarkerDown sut = new FreeMarkerDown();

    @Test(expected = NullPointerException.class)
    public void register_preProcesorMustNotBeNull() {
        sut.register((PreProcessor) null);
    }

    @Test
    @Ignore
    public void render_fragment() {
        final Fragment renderable = new FragmentImpl(
                "<?foo\n"
                + "foo bar baz\n"
                + "?>\n"
                + "Lorem ipsum dolor.\n"
                + "<?bar snafu ?>bla blub",
                Defaults.ENCODING.getValue()
        );

        assertThat(sut.render(renderable), is("Lorem ipsum dolor.\nbla blub"));
    }

    @Test
    @Ignore
    public void render_fragment_withPreProcessors() {
        final Fragment renderable = new FragmentImpl(
                "<?foo\n"
                + "foo bar baz\n"
                + "?>\n"
                + "Lorem ipsum dolor.\n"
                + "<?bar snafu ?>bla blub",
                Defaults.ENCODING.getValue()
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
    @Ignore
    public void render_layoutWithFragments() {
        final Layout renderable = new LayoutImpl(
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
        final Layout renderable = new LayoutImpl(
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
    public void newFragemnt_throwsExceptionIfTemplateIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'template'");

        FreeMarkerDown.newFragemnt(null);
    }

    @Test
    public void newFragemnt_withEncoding_throwsExceptionIfTemplateIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'template'");

        FreeMarkerDown.newFragemnt(null, "utf-8");
    }

    @Test
    public void newFragemnt_withEncoding_throwsExceptionIfEncodingIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'encoding'");

        FreeMarkerDown.newFragemnt("", null);
    }

    @Test
    public void newFragemnt_withEncoding_throwsExceptionIfEncodingIsEmpty() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'encoding'");

        FreeMarkerDown.newFragemnt("", "");
    }

    @Test
    public void newFragemnt_alwaysNewInstance() {
        final Fragment one = FreeMarkerDown.newFragemnt("");
        final Fragment two = FreeMarkerDown.newFragemnt("");
        final Fragment three = FreeMarkerDown.newFragemnt("");

        assertThat(one, is(not(sameInstance(two))));
        assertThat(one, is(not(sameInstance(three))));
        assertThat(two, is(not(sameInstance(three))));
    }

    @Test
    public void newFragemnt_withEncoding_alwaysNewInstance() {
        final Fragment one = FreeMarkerDown.newFragemnt("", "utf-8");
        final Fragment two = FreeMarkerDown.newFragemnt("", "utf-8");
        final Fragment three = FreeMarkerDown.newFragemnt("", "utf-8");

        assertThat(one, is(not(sameInstance(two))));
        assertThat(one, is(not(sameInstance(three))));
        assertThat(two, is(not(sameInstance(three))));
    }

    @Test
    public void newLayout_throwsExceptionIfTemplateIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'template'");

        FreeMarkerDown.newLayout(null);
    }

    @Test
    public void newLayout_withEncoding_throwsExceptionIfTemplateIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'template'");

        FreeMarkerDown.newLayout(null, "utf-8");
    }

    @Test
    public void newLayout_withEncoding_throwsExceptionIfEncodingIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'encoding'");

        FreeMarkerDown.newLayout("", null);
    }

    @Test
    public void newLayout_withEncoding_throwsExceptionIfEncodingIsEmpty() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'encoding'");

        FreeMarkerDown.newLayout("", "");
    }

    @Test
    public void newLayout_alwaysNewInstance() {
        final Layout one = FreeMarkerDown.newLayout("");
        final Layout two = FreeMarkerDown.newLayout("");
        final Layout three = FreeMarkerDown.newLayout("");

        assertThat(one, is(not(sameInstance(two))));
        assertThat(one, is(not(sameInstance(three))));
        assertThat(two, is(not(sameInstance(three))));
    }

    @Test
    public void newLayout_withEncoding_alwaysNewInstance() {
        final Layout one = FreeMarkerDown.newLayout("", "utf-8");
        final Layout two = FreeMarkerDown.newLayout("", "utf-8");
        final Layout three = FreeMarkerDown.newLayout("", "utf-8");

        assertThat(one, is(not(sameInstance(two))));
        assertThat(one, is(not(sameInstance(three))));
        assertThat(two, is(not(sameInstance(three))));
    }
}
