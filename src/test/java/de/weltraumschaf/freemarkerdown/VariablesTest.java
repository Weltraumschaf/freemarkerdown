package de.weltraumschaf.freemarkerdown;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * Tests for {2link Variables}.
 *
 * @author Sven Strittmatter
 */
public class VariablesTest {

    private final Variables sut = new Variables();

    @Test
    public void getVariables_emptyByDefault() {
        assertThat(sut.getVariables().isEmpty(), is(true));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getVariables_returnUnmodifiable() {
        sut.getVariables().put("foo", new Object());
    }

    @Test
    public void getVariables_oneVariableAssigned() {
        final Object foo = new Object();
        sut.assignVariable("foo", foo);

        assertThat(sut.getVariables().size(), is(1));
        assertThat(sut.getVariables(), hasEntry("foo", foo));
    }

    @Test
    public void getVariables_threeVariableAssigned() {
        final Object foo = new Object();
        sut.assignVariable("foo", foo);
        final Object bar = new Object();
        sut.assignVariable("bar", bar);
        final Object baz = new Object();
        sut.assignVariable("baz", baz);

        assertThat(sut.getVariables().size(), is(3));
        assertThat(sut.getVariables(), hasEntry("foo", foo));
        assertThat(sut.getVariables(), hasEntry("bar", bar));
        assertThat(sut.getVariables(), hasEntry("baz", baz));
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Variables.class)
                .suppress(Warning.NULL_FIELDS)
                .verify();
    }

    @Test
    public void toStringContainsVariables() {
        assertThat(sut.toString(), is("Variables{vars={}}"));
        sut.assignVariable("foo", "bar");
        assertThat(sut.toString(), is("Variables{vars={foo=bar}}"));
    }
}
