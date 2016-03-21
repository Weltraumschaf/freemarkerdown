package de.weltraumschaf.freemarkerdown;

import java.util.Collections;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * Tests for {@link FragmentImpl}.
 *
 * @author Sven Strittmatter
 */
public class FragmentImplTest extends TestCaseBase {

    private final FragmentImpl sut = new FragmentImpl(
            "template",
            "utf-8",
            FreeMarkerDown.createConfiguration(ENCODING),
            Collections.<RenderOptions>emptySet(),
            "name");

    @Test
    public void toStringContainsAllProperties() {
        assertThat(sut.toString(), is(
                "Fragment{"
                + "templateVariables=VariableScope{"
                    + "parent=null, "
                    + "data=Variables{vars={}}"
                + "}, "
                + "template=template, "
                + "encoding=utf-8, "
                + "templateName=name, "
                + "preProcessedTemplate=template}"));
    }

}
