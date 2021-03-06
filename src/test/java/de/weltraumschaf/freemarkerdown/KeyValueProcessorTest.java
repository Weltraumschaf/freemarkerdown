package de.weltraumschaf.freemarkerdown;

import de.weltraumschaf.commons.guava.Maps;
import java.util.Map;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * Tests for {@link KeyValueProcessor}.
 *
 * @author Sven Strittmatter
 */
public class KeyValueProcessorTest {

    private static final String NL = Defaults.DEFAULT_NEW_LINE.getValue();

    private final Map<String, String> result = Maps.newHashMap();
    private final KeyValueProcessor sut = new KeyValueProcessor(result);

    @Test
    public void hasWarnings_falseByDefault() {
        assertThat(sut.hasWarnings(), is(false));
    }

    @Test
    public void getWarnings_emptyByDefault() {
        assertThat(sut.getWarnings().size(), is(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getWarnings_isUnmodifiable() {
        sut.getWarnings().add("foo");
    }

    @Test
    public void getTarget() {
        assertThat(sut.getTarget(), is("fdm-keyvalue"));
    }

    @Test(expected = NullPointerException.class)
    public void process_nullInput() {
        sut.process(null);
    }

    @Test
    public void process_removeBlocksAndGetData() {
        assertThat(sut.process(
                "  key1: value1" + NL
                + "  key2: " + NL
                + "  // comment" + NL
                + "  key3: value3" + NL), is(""));
        assertThat(sut.process(
                "  key4: value4" + NL), is(""));

        assertThat(sut.hasWarnings(), is(false));
        assertThat(sut.getWarnings().size(), is(0));
        assertThat(result.size(), is(4));
        assertThat(result, hasEntry("key1", "value1"));
        assertThat(result, hasEntry("key2", ""));
        assertThat(result, hasEntry("key3", "value3"));
        assertThat(result, hasEntry("key4", "value4"));
    }

    @Test
    public void process_ignoreMissingKey() {
        assertThat(sut.process(
                "    Navi: Projects" + NL
                + ": My personal projects I'm working on" + NL
                + "    Keywords: Projects, Jenkins, Darcs" + NL), is(""));

        assertThat(sut.hasWarnings(), is(true));
        assertThat(sut.getWarnings().size(), is(1));
        assertThat(sut.getWarnings(),
                contains("Empty key given: ': My personal projects I'm working on'! Skipping line."));
        assertThat(result.size(), is(2));
        assertThat(result, hasEntry("Navi", "Projects"));
        assertThat(result, hasEntry("Keywords", "Projects, Jenkins, Darcs"));
    }

    @Test
    public void process_ignoreEmptyKey() {
        assertThat(sut.process(
                "    Navi: Projects" + NL
                + "        : My personal projects I'm working on" + NL
                + "    Keywords: Projects, Jenkins, Darcs" + NL), is(""));

        assertThat(sut.hasWarnings(), is(true));
        assertThat(sut.getWarnings().size(), is(1));
        assertThat(sut.getWarnings(),
                contains("Empty key given: '        : My personal projects I'm working on'! Skipping line."));
        assertThat(result.size(), is(2));
        assertThat(result, hasEntry("Navi", "Projects"));
        assertThat(result, hasEntry("Keywords", "Projects, Jenkins, Darcs"));
    }

    @Test
    public void process_ignoreMissingValue() {
        assertThat(sut.process(
                "    Navi: Projects" + NL
                + "    Description" + NL
                + "    Keywords: Projects, Jenkins, Darcs" + NL), is(""));

        assertThat(sut.hasWarnings(), is(true));
        assertThat(sut.getWarnings().size(), is(1));
        assertThat(sut.getWarnings(),
                contains("Malformed line '    Description'! Missing split token ':'. Use format 'key : value'."));
        assertThat(result.size(), is(2));
        assertThat(result, hasEntry("Navi", "Projects"));
        assertThat(result, hasEntry("Keywords", "Projects, Jenkins, Darcs"));
    }

    @Test
    public void process_emptyLine() {
        assertThat(sut.process(
                "    Navi: Projects" + NL
                + "    " + NL
                + "    Keywords: Projects, Jenkins, Darcs" + NL), is(""));

        assertThat(sut.hasWarnings(), is(false));
        assertThat(sut.getWarnings().size(), is(0));
        assertThat(result.size(), is(2));
        assertThat(result, hasEntry("Navi", "Projects"));
        assertThat(result, hasEntry("Keywords", "Projects, Jenkins, Darcs"));
    }

    @Test
    public void process_emptyLines() {
        assertThat(sut.process(
                "    Navi: Projects" + NL
                + "    " + NL
                + NL
                + "    " + NL
                + "    " + NL
                + "    Keywords: Projects, Jenkins, Darcs" + NL), is(""));

        assertThat(sut.hasWarnings(), is(false));
        assertThat(sut.getWarnings().size(), is(0));
        assertThat(result.size(), is(2));
        assertThat(result, hasEntry("Navi", "Projects"));
        assertThat(result, hasEntry("Keywords", "Projects, Jenkins, Darcs"));
    }

    @Test
    public void process_processSingleColon() {
        assertThat(sut.process(":" + NL), is(""));

        assertThat(sut.hasWarnings(), is(true));
        assertThat(sut.getWarnings().size(), is(1));
        assertThat(sut.getWarnings(), contains("No key given: ':'! Skipping line."));
        assertThat(result.size(), is(0));
    }

    @Test
    public void process_noValueForKey() {
        assertThat(sut.process("foo:" + NL), is(""));

        assertThat(sut.hasWarnings(), is(true));
        assertThat(sut.getWarnings().size(), is(1));
        assertThat(sut.getWarnings(), contains("No value given: 'foo'! Set vlaue empty."));
        assertThat(result.size(), is(1));
        assertThat(result, hasEntry("foo", ""));
    }
}
