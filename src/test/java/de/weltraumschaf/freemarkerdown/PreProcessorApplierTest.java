package de.weltraumschaf.freemarkerdown;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link PreProcessorApplier}.
 *
 * @author Sven Strittmatter
 */
public class PreProcessorApplierTest {

    final PreProcessorApplier sut = new PreProcessorApplierImpl();

    @Test(expected = NullPointerException.class)
    public void apply_throwsExceptionIfNullPassedInAsSubject() {
        sut.apply(null, mock(PreProcessor.class));
    }

    @Test(expected = NullPointerException.class)
    public void apply_throwsExceptionIfNullPassedInAsProcessor() {
        sut.apply("", null);
    }

    @Test
    public void apply_twoProcessorInstructionsWithNewlines() {
        final PreProcessor processor = mock(PreProcessor.class);
        when(processor.getTarget()).thenReturn("foo");
        when(processor.process(" foo foo foo ")).thenReturn("FOO");
        when(processor.process(" bar bar bar ")).thenReturn("BAR");

        assertThat(sut.apply(
            "Lorem ipsum dolor sit amet,\n"
            + "<?foo foo foo foo ?>\n"
            + "consetetur sadipscing elitr,\n"
            + "<?foo bar bar bar ?>\n"
            + "sed diam nonumy eirmod tempor\n"
            + "invidunt ut labore et dolore\n"
            + "magna aliquyam erat, sed diam.", processor),
            is("Lorem ipsum dolor sit amet,\n"
            + "FOO\n"
            + "consetetur sadipscing elitr,\n"
            + "BAR\n"
            + "sed diam nonumy eirmod tempor\n"
            + "invidunt ut labore et dolore\n"
            + "magna aliquyam erat, sed diam."));
    }

    @Test
    public void apply_twoProcessorInstructionsInline() {
        final PreProcessor processor = mock(PreProcessor.class);
        when(processor.getTarget()).thenReturn("foo");
        when(processor.process(" foo foo foo ")).thenReturn("FOO");
        when(processor.process(" bar bar bar ")).thenReturn("BAR");

        assertThat(sut.apply(
            "Lorem ipsum dolor sit amet "
            + "<?foo foo foo foo ?> "
            + "consetetur sadipscing elitr, "
            + "<?foo bar bar bar ?> "
            + "sed diam nonumy eirmod tempor"
            + "invidunt ut labore et dolore"
            + "magna aliquyam erat, sed diam.", processor),
            is("Lorem ipsum dolor sit amet "
            + "FOO "
            + "consetetur sadipscing elitr, "
            + "BAR "
            + "sed diam nonumy eirmod tempor"
            + "invidunt ut labore et dolore"
            + "magna aliquyam erat, sed diam."));
    }

    @Test
    public void apply_twoProcessorInstructionsInline_oneNotApplicable() {
        final PreProcessor processor = mock(PreProcessor.class);
        when(processor.getTarget()).thenReturn("foo");
        when(processor.process(" foo foo foo ")).thenReturn("FOO");

        assertThat(sut.apply(
            "Lorem ipsum dolor sit amet "
            + "<?foo foo foo foo ?> "
            + "consetetur sadipscing elitr, "
            + "<?bar bar bar bar ?> "
            + "sed diam nonumy eirmod tempor"
            + "invidunt ut labore et dolore"
            + "magna aliquyam erat, sed diam.", processor),
            is("Lorem ipsum dolor sit amet "
            + "FOO "
            + "consetetur sadipscing elitr, "
            + "<?bar bar bar bar ?> "
            + "sed diam nonumy eirmod tempor"
            + "invidunt ut labore et dolore"
            + "magna aliquyam erat, sed diam."));
    }

}
