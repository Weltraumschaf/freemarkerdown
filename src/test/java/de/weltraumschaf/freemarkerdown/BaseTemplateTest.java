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

import de.weltraumschaf.commons.guava.Lists;
import de.weltraumschaf.commons.guava.Sets;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOError;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import org.pegdown.PegDownProcessor;

/**
 * Tests for {@link BaseTemplate}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class BaseTemplateTest extends TestCaseBase {

    @Test(expected = NullPointerException.class)
    public void render_nullTemplate() {
        new BaseTemplateStub(null, ENCODING);
    }

    @Test
    public void render_emptyTemplate() throws IOException, TemplateException {
        assertThat(new BaseTemplateStub("", ENCODING).render(),
                is(""));
    }

    @Test
    public void render_notEmptyTemplate_withMarkdown() throws IOException, TemplateException {
        assertThat(new BaseTemplateStub("foo bar baz", ENCODING).render(),
                is("<p>foo bar baz</p>"));
    }

    @Test
    public void render_notEmptyTemplate_withoutMarkdown() throws IOException, TemplateException {
        assertThat(new BaseTemplateStub("foo bar baz", ENCODING, Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN)).render(),
                is("foo bar baz"));
    }

    @Test
    public void render_notEmptyTemplateWithVariables() throws IOException, TemplateException {
        final List<String> fruits = Lists.newArrayList();
        fruits.add("bananas");
        fruits.add("apples");
        fruits.add("pears");
        final BaseTemplate sut = new BaseTemplateStub(
                "<p>And BTW we have these fruits:\n"
                + "<ul>\n"
                + "<#list fruits as fruit>\n"
                + " <li>${fruit}</li>\n"
                + "</#list>\n"
                + "<ul>",
                ENCODING,
                Sets.newHashSet(RenderOptions.WITHOUT_MARKDOWN)
        );
        sut.assignVariable("fruits", fruits);

        assertThat(sut.render(),
                is("<p>And BTW we have these fruits:\n"
                        + "<ul>\n"
                        + " <li>bananas</li>\n"
                        + " <li>apples</li>\n"
                        + " <li>pears</li>\n"
                        + "<ul>"));
    }

    @Test
    public void equalsContract() {
        final FreeMarker fmd = FREE_MARKER;
        final VariableScope variableScope = new VariableScope();
        variableScope.assignVariable("foo", "bar");
        EqualsVerifier.forClass(BaseTemplateStub.class)
                .withPrefabValues(Configuration.class, fmd.createConfiguration(ENCODING), fmd.createConfiguration(ENCODING))
                .withPrefabValues(VariableScope.class, variableScope, new VariableScope())
                .withPrefabValues(PegDownProcessor.class, new PegDownProcessor(), new PegDownProcessor())
                .suppress(Warning.NULL_FIELDS)
                .verify();
    }

    @Test
    public void apply() {
        final PreProcessor processor = mock(PreProcessor.class);
        final PreProcessorApplier applier = mock(PreProcessorApplier.class);
        when(applier.apply("foobar", processor)).thenReturn("snafu");

        final BaseTemplate sut = new BaseTemplateStub("foobar", "utf-8");
        sut.setPreProcessorApplier(applier);
        assertThat(sut.getPreProcessedTemplate(), is("foobar"));

        sut.apply(processor);

        verify(applier, times(1)).apply("foobar", processor);
        assertThat(sut.getPreProcessedTemplate(), is("snafu"));
    }

    @Test
    public void factory_wrappsIoExceptions() throws IOException {
        final FreeMarker factory = spy(FREE_MARKER);
        final Throwable ex = new IOException("foobar");
        doThrow(ex).when(factory).createTemplate(anyString(), (Configuration) anyObject());

        final BaseTemplate sut = new BaseTemplateStub("", "utf-8");
        sut.setFactory(factory);

        try {
            sut.render();
            fail("Expected error not thrown!");
        } catch (final IOError err) {
            assertThat(err.getCause(), is(sameInstance((Throwable) ex)));
            assertThat(err.getMessage(), is("java.io.IOException: foobar"));
        }
    }

    @Test
    public void factory_wrappsTemplateException() throws IOException, TemplateException {
        final Template template = spy(FREE_MARKER.createTemplate("", FREE_MARKER.createConfiguration(ENCODING)));
        final Throwable ex = new TemplateException("foobar", null);
        doThrow(ex).when(template).process(anyObject(), (Writer) anyObject());
        final FreeMarker factory = spy(FREE_MARKER);
        doReturn(template).when(factory).createTemplate(anyString(), (Configuration) anyObject());

        final BaseTemplate sut = new BaseTemplateStub("", "utf-8");
        sut.setFactory(factory);

        try {
            sut.render();
            fail("Expected error not thrown!");
        } catch (final TemplateError err) {
            assertThat(err.getCause(), is(sameInstance((Throwable) ex)));
            assertThat(err.getMessage(), is("foobar"));
        }
    }

    @Test
    public void toStringContainsMembers() {
        final BaseTemplate sut = new BaseTemplateStub("foobar", "utf-8");

        assertThat(sut.toString(), is(
                "BaseTemplate{"
                + "templateVariables=VariableScope{"
                    + "parent=null, "
                    + "data=Variables{vars={}}}, "
                + "template=foobar, "
                + "encoding=utf-8, "
                + "templateName=name, "
                + "preProcessedTemplate=foobar}"));
    }

    @Test
    public void getName() {
        final BaseTemplate sut = new BaseTemplateStub("foobar", "utf-8");

        assertThat(sut.getName(), is("name"));
    }

    private static final class BaseTemplateStub extends BaseTemplate {

        public BaseTemplateStub(final String template, final String encoding) {
            this(template, encoding, Collections.<RenderOptions>emptySet());
        }

        public BaseTemplateStub(final String template, final String encoding, final Set<RenderOptions> options) {
            super(
                    template,
                    encoding,
                    FREE_MARKER.createConfiguration(ENCODING),
                    options,
                    "name");
        }

    }
}
