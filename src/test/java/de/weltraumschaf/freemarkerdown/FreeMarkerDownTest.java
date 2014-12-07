/*
 *  LICENSE
 *
 * "THE BEER-WARE LICENSE" (Revision 43):
 * "Sven Strittmatter" <weltraumschaf@googlemail.com> wrote this file.
 * As long as you retain this notice you can do whatever you want with
 * this stuff. If we meet some day, and you think this stuff is worth it,
 * you can buy me a non alcohol-free beer in return.
 *
 * Copyright (C) 2012 "Sven Strittmatter" <weltraumschaf@googlemail.com>
 */
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
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class FreeMarkerDownTest extends TestCaseBase{

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
    public void createFragemnt_fromString_withEncoding_throwsExceptionIfTemplateIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'template'");

        sut.createFragemnt((String) null, "utf-8", "name");
    }

    @Test
    public void createFragemnt_fromString_alwaysNewInstance() {
        final Fragment one = sut.createFragemnt("", ENCODING, "one");
        final Fragment two = sut.createFragemnt("", ENCODING, "two");
        final Fragment three = sut.createFragemnt("", ENCODING, "three");

        assertThat(one, is(not(sameInstance(two))));
        assertThat(one, is(not(sameInstance(three))));
        assertThat(two, is(not(sameInstance(three))));
    }


    @Test
    public void createLayout_fromString_withEncoding_throwsExceptionIfTemplateIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'template'");

        sut.createLayout((String) null, "utf-8", "name");
    }

    @Test
    public void createLayout_fromString_alwaysNewInstance() {
        final Layout one = sut.createLayout("", ENCODING, "one");
        final Layout two = sut.createLayout("", ENCODING, "two");
        final Layout three = sut.createLayout("", ENCODING, "three");

        assertThat(one, is(not(sameInstance(two))));
        assertThat(one, is(not(sameInstance(three))));
        assertThat(two, is(not(sameInstance(three))));
    }

    @Test
    public void createFragemnt_fromPath_withEncoding_throwsExceptionIfTemplateIsNull() throws IOException {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'template'");

        sut.createFragemnt((Path) null, "utf-8", "name");
    }

    @Test
    public void createFragemnt_fromPath_withEncoding_throwsExceptionIfEncodingIsNull() throws IOException {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'encoding'");

        sut.createFragemnt(mock(Path.class), (String) null, "name");
    }

    @Test
    public void createFragemnt_fromPath_withEncoding_throwsExceptionIfEncodingIsEmpty() throws IOException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'encoding'");

        sut.createFragemnt(mock(Path.class), "", "name");
    }

    @Test
    public void createFragemnt_fromPath_alwaysNewInstance() throws IOException {
        final Fragment one = sut.createFragemnt(tmp.newFile().toPath(), ENCODING, "one");
        final Fragment two = sut.createFragemnt(tmp.newFile().toPath(), ENCODING, "two");
        final Fragment three = sut.createFragemnt(tmp.newFile().toPath(), ENCODING, "three");

        assertThat(one, is(not(sameInstance(two))));
        assertThat(one, is(not(sameInstance(three))));
        assertThat(two, is(not(sameInstance(three))));
    }

    @Test
    public void createLayout_fromPath_throwsExceptionIfTemplateIsNull() throws IOException {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'template'");

        sut.createLayout((Path) null, ENCODING, "name");
    }

    @Test
    public void createLayout_fromPath_withEncoding_throwsExceptionIfTemplateIsNull() throws IOException {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'template'");

        sut.createLayout((Path) null, "utf-8", "name");
    }

    @Test
    public void createLayout_fromPath_withEncoding_throwsExceptionIfEncodingIsNull() throws IOException {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'encoding'");

        sut.createLayout(mock(Path.class), (String) null, "name");
    }

    @Test
    public void createLayout_fromPath_withEncoding_throwsExceptionIfEncodingIsEmpty() throws IOException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'encoding'");

        sut.createLayout(mock(Path.class), "", "name");
    }

    @Test
    public void createLayout_fromPath_alwaysNewInstance() throws IOException {
        final Layout one = sut.createLayout(tmp.newFile().toPath(), ENCODING, "one");
        final Layout two = sut.createLayout(tmp.newFile().toPath(), ENCODING, "two");
        final Layout three = sut.createLayout(tmp.newFile().toPath(), ENCODING, "three");

        assertThat(one, is(not(sameInstance(two))));
        assertThat(one, is(not(sameInstance(three))));
        assertThat(two, is(not(sameInstance(three))));
    }

    @Test
    public void createLayout_fromPath_withEncoding_alwaysNewInstance() throws IOException {
        final Layout one = sut.createLayout(tmp.newFile().toPath(), "utf-8", "name");
        final Layout two = sut.createLayout(tmp.newFile().toPath(), "utf-8", "name");
        final Layout three = sut.createLayout(tmp.newFile().toPath(), "utf-8", "name");

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
