package de.weltraumschaf.freemarkerdown;

import java.util.Map;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link VariableScope}.
 *
 * @author Sven Strittmatter
 */
public class VariableScopeTest {

    private final VariableScope sut = new VariableScope();

    @Test
    public void hasParent() {
        assertThat(sut.hasParent(), is(false));

        sut.setParent(new VariableScope());

        assertThat(sut.hasParent(), is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void setParent_throwsErrorForSelf() {
        sut.setParent(sut);
    }

    @Test
    public void getData() {
        assertThat(sut.getData().size(), is(0));
    }

    @Test
    public void getData_withoutParent() {
        sut.assignVariable("foo", "bar");
        sut.assignVariable("baz", true);

        final Map<String, Object> data = sut.getData();

        assertThat(data.size(), is(2));
        assertThat(data, hasEntry("foo", (Object) "bar"));
        assertThat(data, hasEntry("baz", (Object) true));
    }

    @Test
    public void getData_withOneParent() {
        final VariableScope parent = new VariableScope();
        parent.assignVariable("foo", "snafu");
        parent.assignVariable("blub", "lala");
        sut.setParent(parent);
        sut.assignVariable("foo", "bar");
        sut.assignVariable("baz", true);

        final Map<String, Object> data = sut.getData();

        assertThat(data.size(), is(3));
        assertThat(data, hasEntry("foo", (Object) "bar"));
        assertThat(data, hasEntry("baz", (Object) true));
        assertThat(data, hasEntry("blub", (Object) "lala"));
    }

    @Test
    public void getData_withTwoParents() {
        final VariableScope parentTwo = new VariableScope();
        parentTwo.assignVariable("blub", "lulu");
        parentTwo.assignVariable("narf", "narf");
        final VariableScope parentOne = new VariableScope();
        parentOne.setParent(parentTwo);
        parentOne.assignVariable("foo", "snafu");
        parentOne.assignVariable("blub", "lala");
        sut.setParent(parentOne);
        sut.assignVariable("foo", "bar");
        sut.assignVariable("baz", true);

        final Map<String, Object> data = sut.getData();

        assertThat(data.size(), is(4));
        assertThat(data, hasEntry("foo", (Object) "bar"));
        assertThat(data, hasEntry("baz", (Object) true));
        assertThat(data, hasEntry("blub", (Object) "lala"));
        assertThat(data, hasEntry("narf", (Object) "narf"));
    }

    @Test
    public void equalsContract() {
        final VariableScope variableScope = new VariableScope();
        variableScope.assignVariable("foo", "bar");
        EqualsVerifier.forClass(VariableScope.class)
                .withPrefabValues(VariableScope.class, variableScope, new VariableScope())
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    public void toStringContainsMembers() {
        assertThat(sut.toString(), is("VariableScope{parent=null, data=Variables{vars={}}}"));
    }
}
