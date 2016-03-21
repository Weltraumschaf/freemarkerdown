package de.weltraumschaf.freemarkerdown;

import java.util.Collections;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link LayoutImpl}.
 *
 * @author Sven Strittmatter
 */
public class LayoutImplTest extends TestCaseBase {

    private final LayoutImpl sut = new LayoutImpl(
            "template",
            "utf-8",
            FreeMarkerDown.createConfiguration(ENCODING),
            Collections.<RenderOptions>emptySet(),
            "name");

    @Test
    public void toStringContainsAllProperties() {
        assertThat(sut.toString(), is(
                "Layout{"
                + "templateVariables=VariableScope{"
                    + "parent=null, "
                    + "data=Variables{vars={}}"
                + "}, "
                + "template=template, "
                + "encoding=utf-8, "
                + "templateName=name, "
                + "preProcessedTemplate=template, "
                + "fragments={}"
                + "}"));
    }

}
