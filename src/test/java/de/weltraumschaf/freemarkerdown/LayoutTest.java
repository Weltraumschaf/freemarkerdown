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
 * @author Sven Strittmatter
 */
public class LayoutTest extends TestCaseBase {

    @Test(expected = NullPointerException.class)
    public void render_nullTemplate() {
        new LayoutImpl(
                null,
                ENCODING,
                FREE_MARKER.createConfiguration(ENCODING),
                Collections.<RenderOptions>emptySet(),
                "name");
    }

    @Test
    public void render_emptyTemplate() throws IOException, TemplateException {
        assertThat(
                new LayoutImpl(
                        "",
                        ENCODING,
                        FREE_MARKER.createConfiguration(ENCODING),
                        Collections.<RenderOptions>emptySet(),
                        "name").render(),
                is(""));
    }

    @Test
    public void render_notEmptyTemplate() throws IOException, TemplateException {
        assertThat(
                new LayoutImpl(
                        "foo bar baz",
                        ENCODING,
                        FREE_MARKER.createConfiguration(ENCODING),
                        Collections.<RenderOptions>emptySet(),
                        "name").render(),
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
                ENCODING,
                FREE_MARKER.createConfiguration(ENCODING),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN),
                "name");
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
                ENCODING,
                FREE_MARKER.createConfiguration(ENCODING),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN),
                "name");
        sut.assignTemplateModel("fragmentOne",
                new FragmentImpl("foo",
                        ENCODING,
                        FREE_MARKER.createConfiguration(ENCODING),
                        Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN),
                        "name")
        );

        assertThat(sut.render(), is("<p>foo</p>\n"));
    }

    @Test
    public void render_withThreeFragement() throws IOException, TemplateException {
        final Layout sut = new LayoutImpl(
                "<p>${fragmentOne}</p>\n"
                + "<p>${fragmentTwo}</p>\n"
                + "<p>${fragmentThree}</p>\n",
                ENCODING,
                FREE_MARKER.createConfiguration(ENCODING),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN),
                "name"
        );
        sut.assignTemplateModel("fragmentOne", new FragmentImpl(
                "foo",
                ENCODING,
                FREE_MARKER.createConfiguration(ENCODING),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN),
                "name"));
        sut.assignTemplateModel("fragmentTwo", new FragmentImpl(
                "bar",
                ENCODING,
                FREE_MARKER.createConfiguration(ENCODING),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN),
                "name"));
        sut.assignTemplateModel("fragmentThree", new FragmentImpl(
                "baz",
                ENCODING,
                FREE_MARKER.createConfiguration(ENCODING),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN),
                "name"));

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
                ENCODING,
                FREE_MARKER.createConfiguration(ENCODING),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN),
                "name"
        );
        final Layout sut = new LayoutImpl(
                "<h1>snafu</h1>\n"
                + "${inside}",
                ENCODING,
                FREE_MARKER.createConfiguration(ENCODING),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN),
                "name");
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
                ENCODING,
                FREE_MARKER.createConfiguration(ENCODING),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN),
                "name"
        );
        inside.assignTemplateModel("fragmentOne", new FragmentImpl(
                "foo",
                ENCODING,
                FREE_MARKER.createConfiguration(ENCODING),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN),
                "name"));
        inside.assignTemplateModel("fragmentTwo", new FragmentImpl(
                "bar",
                ENCODING,
                FREE_MARKER.createConfiguration(ENCODING),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN),
                "name"));
        inside.assignTemplateModel("fragmentThree", new FragmentImpl(
                "baz",
                ENCODING,
                FREE_MARKER.createConfiguration(ENCODING),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN),
                "name"));
        final Layout sut = new LayoutImpl(
                "<h1>snafu</h1>\n"
                + "${inside}",
                ENCODING,
                FREE_MARKER.createConfiguration(ENCODING),
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN),
                "name");
        sut.assignTemplateModel("inside", inside);

        assertThat(sut.render(), is(
                "<h1>snafu</h1>\n"
                + "<p>foo</p>\n"
                + "<p>bar</p>\n"
                + "<p>baz</p>\n"
        ));
    }
}
