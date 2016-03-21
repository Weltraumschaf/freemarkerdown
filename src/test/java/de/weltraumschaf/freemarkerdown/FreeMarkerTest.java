package de.weltraumschaf.freemarkerdown;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import java.io.IOException;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * Tests for {@link FreeMarker}.
 *
 * @author Sven Strittmatter
 */
public class FreeMarkerTest extends TestCaseBase {

    private final FreeMarker sut = new FreeMarker();

    @Test
    public void createTemplate_alwaysNewInstance() throws IOException {
        final FreeMarker fm = new FreeMarker();
        final Template one = sut.createTemplate("", fm.createConfiguration(ENCODING));
        final Template two = sut.createTemplate("", fm.createConfiguration(ENCODING));
        final Template three = sut.createTemplate("", fm.createConfiguration(ENCODING));

        assertThat(one, is(not(sameInstance(two))));
        assertThat(one, is(not(sameInstance(three))));
        assertThat(two, is(not(sameInstance(three))));
    }

    @Test
    public void createConfiguration_alwaysNewInstance() {
        final Configuration one = sut.createConfiguration(ENCODING);
        final Configuration two = sut.createConfiguration(ENCODING);
        final Configuration three = sut.createConfiguration(ENCODING);

        assertThat(one, is(not(sameInstance(two))));
        assertThat(one, is(not(sameInstance(three))));
        assertThat(two, is(not(sameInstance(three))));
    }

    @Test
    public void createDefaultObjectWrapper_alwaysSameInstance() {
        final DefaultObjectWrapper one = sut.createDefaultObjectWrapper();
        final DefaultObjectWrapper two = sut.createDefaultObjectWrapper();
        final DefaultObjectWrapper three = sut.createDefaultObjectWrapper();

        assertThat(one, is(sameInstance(two)));
        assertThat(one, is(sameInstance(three)));
        assertThat(two, is(sameInstance(three)));
    }

}
