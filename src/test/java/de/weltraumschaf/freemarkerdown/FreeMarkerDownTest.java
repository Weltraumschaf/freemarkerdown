package de.weltraumschaf.freemarkerdown;

import static de.weltraumschaf.freemarkerdown.Interceptor.ExecutionPoint.AFTER_MARKDOWN;
import static de.weltraumschaf.freemarkerdown.Interceptor.ExecutionPoint.BEFORE_MARKDOWN;
import freemarker.template.Configuration;
import java.io.IOException;
import java.nio.file.Path;
import nl.jqno.equalsverifier.EqualsVerifier;
import static org.hamcrest.Matchers.*;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import static org.mockito.Mockito.*;
import org.pegdown.PegDownProcessor;

/**
 * Tests for {@link FreeMarkerDown}.
 *
 * @author Sven Strittmatter
 */
public class FreeMarkerDownTest extends TestCaseBase {

    @Rule
    //CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON
    @Rule
    //CHECKSTYLE:OFF
    public final TemporaryFolder tmp = new TemporaryFolder();
    //CHECKSTYLE:ON

    private final EventDispatcher events = new EventDispatcher();
    private final FreeMarkerDown sut = new FreeMarkerDown(FreeMarkerDown.createConfiguration(ENCODING), events);

    @Test(expected = NullPointerException.class)
    public void register_preProcesorMustNotBeNull() {
        sut.register((PreProcessor) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void register_returnUnmodifiable() {
        sut.getPreProcessors().add(mock(PreProcessor.class));
    }

    @Test
    public void registerPreProcessor() {
        final PreProcessor processor = mock(PreProcessor.class);

        sut.register(processor);

        assertThat(sut.getPreProcessors().size(), is(1));
        assertThat(sut.getPreProcessors(), contains(processor));
    }

    @Test
    public void createFragemnt_fromString_throwsExceptionIfTemplateIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'template'");

        sut.createFragemnt((String) null, "name");
    }

    @Test
    public void createFragemnt_fromString_alwaysNewInstance() {
        final Fragment one = sut.createFragemnt("", "one");
        final Fragment two = sut.createFragemnt("", "two");
        final Fragment three = sut.createFragemnt("", "three");

        assertThat(one, is(not(sameInstance(two))));
        assertThat(one, is(not(sameInstance(three))));
        assertThat(two, is(not(sameInstance(three))));
    }

    @Test
    public void createLayout_fromString_throwsExceptionIfTemplateIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'template'");

        sut.createLayout((String) null, "name");
    }

    @Test
    public void createLayout_fromString_alwaysNewInstance() {
        final Layout one = sut.createLayout("", "one");
        final Layout two = sut.createLayout("", "two");
        final Layout three = sut.createLayout("", "three");

        assertThat(one, is(not(sameInstance(two))));
        assertThat(one, is(not(sameInstance(three))));
        assertThat(two, is(not(sameInstance(three))));
    }

    @Test
    public void createFragemnt_fromPath_throwsExceptionIfTemplateIsNull() throws IOException {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'template'");

        sut.createFragemnt((Path) null, "name");
    }

    @Test
    public void createFragemnt_fromPath_alwaysNewInstance() throws IOException {
        final Fragment one = sut.createFragemnt(tmp.newFile().toPath(), "one");
        final Fragment two = sut.createFragemnt(tmp.newFile().toPath(), "two");
        final Fragment three = sut.createFragemnt(tmp.newFile().toPath(), "three");

        assertThat(one, is(not(sameInstance(two))));
        assertThat(one, is(not(sameInstance(three))));
        assertThat(two, is(not(sameInstance(three))));
    }

    @Test
    public void createLayout_fromPath_throwsExceptionIfTemplateIsNull() throws IOException {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'template'");

        sut.createLayout((Path) null, "name");
    }

    @Test
    public void createLayout_fromPath_alwaysNewInstance() throws IOException {
        final Layout one = sut.createLayout(tmp.newFile().toPath(), "one");
        final Layout two = sut.createLayout(tmp.newFile().toPath(), "two");
        final Layout three = sut.createLayout(tmp.newFile().toPath(), "three");

        assertThat(one, is(not(sameInstance(two))));
        assertThat(one, is(not(sameInstance(three))));
        assertThat(two, is(not(sameInstance(three))));
    }

    @Test
    public void create_alwaysNewInstance() {
        final FreeMarkerDown one = FreeMarkerDown.create(ENCODING);
        final FreeMarkerDown two = FreeMarkerDown.create(ENCODING);
        final FreeMarkerDown three = FreeMarkerDown.create(ENCODING);

        assertThat(one, is(not(sameInstance(two))));
        assertThat(one, is(not(sameInstance(three))));
        assertThat(two, is(not(sameInstance(three))));
    }

    @Test
    public void equalsContract() {
        final FreeMarker fm = new FreeMarker();
        EqualsVerifier.forClass(FreeMarkerDown.class)
            .withPrefabValues(Configuration.class, fm.createConfiguration(ENCODING), fm.createConfiguration(ENCODING))
            .withPrefabValues(PegDownProcessor.class, new PegDownProcessor(), new PegDownProcessor())
            .verify();
    }

    @Test
    public void toStringContainsMembers() {
        assertThat(sut.toString(), is("FreeMarkerDown{preProcessors=[]}"));
    }

    @Test
    public void registerInterceptor() {
        final Interceptor interceptorOne = mock(Interceptor.class);
        final Interceptor interceptorTwo = mock(Interceptor.class);
        final Interceptor interceptorThree = mock(Interceptor.class);

        sut.register(interceptorOne, AFTER_MARKDOWN);
        sut.register(interceptorTwo, AFTER_MARKDOWN);
        sut.register(interceptorThree, BEFORE_MARKDOWN);

        assertThat(events.getInterceptors(), hasKey(AFTER_MARKDOWN));
        assertThat(events.getInterceptors(), hasKey(BEFORE_MARKDOWN));
        assertThat(events.getInterceptors().size(), is(2));

        assertThat(events.getInterceptors().get(AFTER_MARKDOWN), contains(interceptorOne, interceptorTwo));
        assertThat(events.getInterceptors().get(BEFORE_MARKDOWN), contains(interceptorThree));
    }

}
