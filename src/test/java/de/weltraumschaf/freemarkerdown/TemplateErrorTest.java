package de.weltraumschaf.freemarkerdown;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link TemplateError}.
 *
 * @author Sven Strittmatter
 */
public class TemplateErrorTest {

    @Test(expected = NullPointerException.class)
    public void constructorThrowErrorIfMessageIsNull() {
        new TemplateError(null, new Throwable());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowErrorIfMessageIsEmpty() {
        new TemplateError("", new Throwable());
    }

    @Test
    public void getMessage() {
        final Throwable sut = new TemplateError("foobar", new Throwable());

        assertThat(sut.getMessage(), is("foobar"));
    }

}
