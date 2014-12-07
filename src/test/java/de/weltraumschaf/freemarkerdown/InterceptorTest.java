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

import static de.weltraumschaf.freemarkerdown.Interceptor.ExecutionPoint.*;
import java.util.Collection;
import java.util.Collections;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests for intercepting the rendering.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
@RunWith(MockitoJUnitRunner.class)
public class InterceptorTest extends TestCaseBase {

    private final FreeMarkerDown fmd = FreeMarkerDown.create(ENCODING);

    @Mock
    private Interceptor interceptor;

    @Test
    public void interceptFragment_beforePreprocessing_withoutPreprocessor() {
        final Fragment fragment = fmd.createFragemnt("foo bar baz", ENCODING, "fragment");
        fmd.register(interceptor, BEFORE_PREPROCESSING);

        fmd.render(fragment);

        verify(interceptor, never()).intercept(BEFORE_PREPROCESSING, fragment, "");
    }

    @Test
    public void interceptFragment_beforePreprocessing_withPreprocessor() {
        final Fragment fragment = fmd.createFragemnt("foo bar baz", ENCODING, "fragment");
        fmd.register(new PreProcessorStub());
        fmd.register(interceptor, BEFORE_PREPROCESSING);

        fmd.render(fragment);

        verify(interceptor, times(1)).intercept(BEFORE_PREPROCESSING, fragment, "foo bar baz");
    }

    @Test
    public void interceptFragment_afterPreprocessing_withoutPreprocessor() {
        final Fragment fragment = fmd.createFragemnt("foo bar baz", ENCODING, "fragment");
        fmd.register(interceptor, AFTER_PREPROCESSING);

        fmd.render(fragment);

        verify(interceptor, never()).intercept(AFTER_PREPROCESSING, fragment, "");
    }

    @Test
    public void interceptFragment_afterPreprocessing_withtPreprocessor() {
        final Fragment fragment = fmd.createFragemnt("foo bar baz", ENCODING, "fragment");
        fmd.register(new PreProcessorStub());
        fmd.register(interceptor, AFTER_PREPROCESSING);

        fmd.render(fragment);

        verify(interceptor, times(1)).intercept(AFTER_PREPROCESSING, fragment, "foo bar baz");
    }

    @Test
    public void interceptFragment_beforeRendering() {
        final Fragment fragment = fmd.createFragemnt("foo bar baz", ENCODING, "fragment");
        fmd.register(interceptor, BEFORE_RENDERING);

        fmd.render(fragment);

        verify(interceptor, times(1)).intercept(BEFORE_RENDERING, fragment, "foo bar baz");
    }

    @Test
    public void interceptFragment_afterRendering() {
        final Fragment fragment = fmd.createFragemnt("foo bar baz", ENCODING, "fragment");
        fmd.register(interceptor, AFTER_RENDERING);

        fmd.render(fragment);

        verify(interceptor, times(1)).intercept(AFTER_RENDERING, fragment, "foo bar baz");
    }

    @Test
    public void interceptFragment_beforeMarkdown() {
        final Fragment fragment = fmd.createFragemnt("foo bar baz", ENCODING, "fragment");
        fmd.register(interceptor, BEFORE_MARKDOWN);

        fmd.render(fragment);

        verify(interceptor, times(1)).intercept(BEFORE_MARKDOWN, fragment, "foo bar baz");
    }

    @Test
    public void interceptFragment_afterMarkdown() {
        final Fragment fragment = fmd.createFragemnt("foo bar baz", ENCODING, "fragment");
        fmd.register(interceptor, AFTER_MARKDOWN);

        fmd.render(fragment);

        verify(interceptor, times(1)).intercept(AFTER_MARKDOWN, fragment, "<p>foo bar baz</p>");
    }

    @Test
    public void interceptFragment_beforeMarkdown_withoutMarkdownOption() {
        final Fragment fragment = fmd.createFragemnt("foo bar baz", ENCODING, "fragment", RenderOptions.WITHOUT_MARKDOWN);
        fmd.register(interceptor, BEFORE_MARKDOWN);

        fmd.render(fragment);

        verify(interceptor, never()).intercept(BEFORE_MARKDOWN, fragment, "foo bar baz");
    }

    @Test
    public void interceptFragment_afterMarkdown_withoutMarkdownOption() {
        final Fragment fragment = fmd.createFragemnt("foo bar baz", ENCODING, "fragment", RenderOptions.WITHOUT_MARKDOWN);
        fmd.register(interceptor, AFTER_MARKDOWN);

        fmd.render(fragment);

        verify(interceptor, never()).intercept(AFTER_MARKDOWN, fragment, "<p>foo bar baz</p>");
    }

    @Test
    public void interceptLayout_beforePreprocessing_withoutPreprocessor() {
        final Fragment fragment = fmd.createFragemnt("foo bar baz", ENCODING, "fragment");
        final Layout layout = fmd.createLayout("Layout ${content}", ENCODING, "layout");
        layout.assignTemplateModel("content", fragment);
        fmd.register(interceptor, BEFORE_PREPROCESSING);

        fmd.render(layout);

        verify(interceptor, never()).intercept(BEFORE_PREPROCESSING, fragment, "");
        verify(interceptor, never()).intercept(BEFORE_PREPROCESSING, layout, "");
    }

    @Test
    public void interceptLayout_beforePreprocessing_withPreprocessor() {
        fmd.register(new PreProcessorStub());
        final Fragment fragment = fmd.createFragemnt("foo bar baz", "utf-8", "inner-template");
        final Layout layout = fmd.createLayout("layout: ${content}", "utf-8", "outer-template");
        layout.assignTemplateModel("content", fragment);
        fmd.register(interceptor, BEFORE_PREPROCESSING);

        fmd.render(layout);

        verify(interceptor, times(1)).intercept(BEFORE_PREPROCESSING, layout, "layout: ${content}");
        verify(interceptor, times(1)).intercept(BEFORE_PREPROCESSING, fragment, "foo bar baz");
    }

    @Test
    public void interceptLayout_afterPreprocessing_withoutPreprocessor() {
        final Fragment fragment = fmd.createFragemnt("foo bar baz", "utf-8", "inner-template");
        final Layout layout = fmd.createLayout("layout: ${content}", "utf-8", "outer-template");
        layout.assignTemplateModel("content", fragment);
        fmd.register(interceptor, AFTER_MARKDOWN);

        fmd.render(layout);

        verify(interceptor, never()).intercept(AFTER_MARKDOWN, layout, "");
        verify(interceptor, never()).intercept(AFTER_MARKDOWN, fragment, "");
    }

    @Test
    public void interceptLayout_afterPreprocessing_withPreprocessor() {
        fmd.register(new PreProcessorStub());
        final Fragment fragment = fmd.createFragemnt("foo bar baz", "utf-8", "inner-template");
        final Layout layout = fmd.createLayout("layout: ${content}", "utf-8", "outer-template");
        layout.assignTemplateModel("content", fragment);
        fmd.register(interceptor, AFTER_MARKDOWN);

        fmd.render(layout);

        verify(interceptor, times(1)).intercept(AFTER_MARKDOWN, fragment, "<p>foo bar baz</p>");
        verify(interceptor, times(1)).intercept(AFTER_MARKDOWN, layout, "<p>layout: <p>foo bar baz</p></p>");
    }

    @Test
    public void interceptLayout_beforeRendering() {
        final Fragment fragment = fmd.createFragemnt("foo bar baz", "utf-8", "inner-template");
        final Layout layout = fmd.createLayout("layout: ${content}", "utf-8", "outer-template");
        layout.assignTemplateModel("content", fragment);
        fmd.register(interceptor, BEFORE_RENDERING);

        fmd.render(layout);

        verify(interceptor, times(1)).intercept(BEFORE_RENDERING, fragment, "foo bar baz");
        verify(interceptor, times(1)).intercept(BEFORE_RENDERING, layout, "layout: ${content}");
    }

    @Test
    public void interceptLayout_afterRendering() {
        final Fragment fragment = fmd.createFragemnt("foo bar baz", "utf-8", "inner-template");
        final Layout layout = fmd.createLayout("layout: ${content}", "utf-8", "outer-template");
        layout.assignTemplateModel("content", fragment);
        fmd.register(interceptor, AFTER_RENDERING);

        fmd.render(layout);

        verify(interceptor, times(1)).intercept(AFTER_RENDERING, fragment, "foo bar baz");
        verify(interceptor, times(1)).intercept(AFTER_RENDERING, layout, "layout: <p>foo bar baz</p>");
    }

    @Test
    public void interceptLayout_beforeMarkdown() {
        final Fragment fragment = fmd.createFragemnt("foo bar baz", "utf-8", "inner-template");
        final Layout layout = fmd.createLayout("layout: ${content}", "utf-8", "outer-template");
        layout.assignTemplateModel("content", fragment);
        fmd.register(interceptor, BEFORE_MARKDOWN);

        fmd.render(layout);

        verify(interceptor, times(1)).intercept(BEFORE_MARKDOWN, fragment, "foo bar baz");
        verify(interceptor, times(1)).intercept(BEFORE_MARKDOWN, layout, "layout: <p>foo bar baz</p>");
    }

    @Test
    public void interceptLayout_afterMarkdown() {
        final Fragment fragment = fmd.createFragemnt("foo bar baz", "utf-8", "inner-template");
        final Layout layout = fmd.createLayout("layout: ${content}", "utf-8", "outer-template");
        layout.assignTemplateModel("content", fragment);
        fmd.register(interceptor, AFTER_MARKDOWN);

        fmd.render(layout);

        verify(interceptor, times(1)).intercept(AFTER_MARKDOWN, fragment, "<p>foo bar baz</p>");
        verify(interceptor, times(1)).intercept(AFTER_MARKDOWN, layout, "<p>layout: <p>foo bar baz</p></p>");
    }

    @Test
    public void interceptLayout_beforeMarkdown_withoutMarkdownOption() {
        final Fragment fragment = fmd.createFragemnt("foo bar baz", "utf-8", "inner-template");
        final Layout layout = fmd.createLayout(
                "layout: ${content}", "utf-8", "outer-template",
                RenderOptions.WITHOUT_MARKDOWN);
        layout.assignTemplateModel("content", fragment);
        fmd.register(interceptor, BEFORE_MARKDOWN);

        fmd.render(layout);

        verify(interceptor, times(1)).intercept(BEFORE_MARKDOWN, fragment, "foo bar baz");
        verify(interceptor, never()).intercept(BEFORE_MARKDOWN, layout, "layout: <p>foo bar baz</p>");
    }

    @Test
    public void interceptLayout_afterMarkdown_withoutMarkdownOption() {
        final Fragment fragment = fmd.createFragemnt("foo bar baz", "utf-8", "inner-template");
        final Layout layout = fmd.createLayout(
                "layout: ${content}", "utf-8", "outer-template",
                RenderOptions.WITHOUT_MARKDOWN);
        layout.assignTemplateModel("content", fragment);
        fmd.register(interceptor, AFTER_MARKDOWN);

        fmd.render(layout);

        verify(interceptor, times(1)).intercept(AFTER_MARKDOWN, fragment, "<p>foo bar baz</p>");
        verify(interceptor, never()).intercept(AFTER_MARKDOWN, layout, "<p>layout: <p>foo bar baz</p></p>");
    }

    private static class PreProcessorStub implements PreProcessor {

        @Override
        public String process(final String input) {
            return input;
        }

        @Override
        public String getTarget() {
            return "target";
        }

        @Override
        public boolean hasWarnings() {
            return false;
        }

        @Override
        public Collection<String> getWarnings() {
            return Collections.emptyList();
        }

    }
}
