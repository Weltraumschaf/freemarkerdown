package de.weltraumschaf.freemarkerdown;

import de.weltraumschaf.commons.guava.Maps;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link PreProcessors}.
 *
 * @author Sven Strittmatter
 */
public class PreProcessorsTest {

    @Rule
    //CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON

    @Test
    public void invokeConstructorByReflectionThrowsException() throws Exception {
        assertThat(PreProcessors.class.getDeclaredConstructors().length, is(1));

        final Constructor<PreProcessors> ctor = PreProcessors.class.getDeclaredConstructor();
        ctor.setAccessible(true);

        thrown.expect(either(instanceOf(UnsupportedOperationException.class))
                .or(instanceOf(InvocationTargetException.class)));
        ctor.newInstance();
    }

    @Test
    public void createKeyValueProcessor_alwaysNewInstance() {
        final PreProcessor one = PreProcessors.createKeyValueProcessor(Maps.<String, String>newHashMap());
        final PreProcessor two = PreProcessors.createKeyValueProcessor(Maps.<String, String>newHashMap());
        final PreProcessor three = PreProcessors.createKeyValueProcessor(Maps.<String, String>newHashMap());

        assertThat(one, is(not(sameInstance(two))));
        assertThat(one, is(not(sameInstance(three))));
        assertThat(two, is(not(sameInstance(three))));
    }
}
