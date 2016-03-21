package de.weltraumschaf.freemarkerdown;

import java.io.IOError;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link FreemarkerProperties}.
 *
 * @author Sven Strittmatter
 */
public class FreemarkerPropertiesTest {

    private final FreemarkerProperties sut = new FreemarkerProperties();

    @Test
    public void getVersion() {
        assertThat(sut.getVersion(), is("2.3.21"));
    }

    @Test
    public void toStringContainsVersion() {
        assertThat(sut.toString(), is("2.3.21"));
    }

    @Test(expected = IOError.class)
    public void badFile() {
        new FreemarkerProperties("foobar");
    }

}
