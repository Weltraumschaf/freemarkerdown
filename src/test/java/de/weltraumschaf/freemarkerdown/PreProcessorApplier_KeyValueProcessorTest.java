package de.weltraumschaf.freemarkerdown;

import de.weltraumschaf.commons.guava.Maps;
import java.util.Map;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * Integration test with {@link PreProcessorApplier} and {@link KeyValueProcessor}.
 *
 * @author Sven Strittmatter
 */
public class PreProcessorApplier_KeyValueProcessorTest {

    private final PreProcessorApplier sut = new PreProcessorApplierImpl();
    private final Map<String, String> result = Maps.newHashMap();
    private final PreProcessor processor = new KeyValueProcessor(result);

    @Test
    public void apply_emptyInput() {
        assertThat(sut.apply("", processor), is(""));

        assertThat(result.size(), is(0));
    }

    @Test
    public void apply_noInstruction() {
        assertThat(sut.apply("Lorem ipsum dolor.", processor), is("Lorem ipsum dolor."));

        assertThat(result.size(), is(0));
    }

    @Test
    public void apply_instructionWithWrongTarget() {
        assertThat(sut.apply("Lorem <?foo foo ?> ipsum <?bar bar ?> dolor.", processor),
                is("Lorem <?foo foo ?> ipsum <?bar bar ?> dolor."));

        assertThat(result.size(), is(0));
    }

    @Test
    public void apply_instructionWithTwoValues() {
        assertThat(sut.apply(
            "Lorem <?fdm-keyvalue\n"
            + " key1: foo\n"
            + " key2: bar\n"
            + "?> ipsum <?fdm-keyvalue\n"
            + "key3: baz\n"
            + "?> dolor.", processor),
            is("Lorem  ipsum  dolor."));

        assertThat(result.size(), is(3));
        assertThat(result, allOf(hasEntry("key1", "foo"), hasEntry("key2", "bar"), hasEntry("key3", "baz")));
    }
}
