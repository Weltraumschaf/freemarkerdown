package de.weltraumschaf.freemarkerdown;

import de.weltraumschaf.commons.guava.Sets;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link FreeMarkerDown}.
 *
 * @author Sven Strittmatter
 */
public class FreeMarkerDown_RenderTest extends TestCaseBase{

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
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN),
                "name"
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
                ENCODING,
                new FreeMarker().createConfiguration(ENCODING),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN),
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
                + "<?bar snafu ?> bla blub",
                ENCODING,
                new FreeMarker().createConfiguration(ENCODING),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN),
                "name"
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
                + "<?bar snafu ?> bla blub",
                ENCODING,
                new FreeMarker().createConfiguration(ENCODING),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN),
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
                "foo\n"
                + "Lorem ipsum dolor.\n"
                + "bar bla blub"));
    }
}
