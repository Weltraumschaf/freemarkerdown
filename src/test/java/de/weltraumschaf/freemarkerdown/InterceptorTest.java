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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
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
public class InterceptorTest {

    private final FreeMarkerDown fmd = FreeMarkerDown.create();

    @Mock
    private Interceptor interceptor;

    @Test
    public void interceptFragment_beforePreprocessing_withoutPreprocessor() {
        final Fragment fragment = fmd.createFragemnt("foo bar baz");
        fmd.register(interceptor, BEFORE_PREPROCESSING);

        fmd.render(fragment);

        verify(interceptor, never()).intercept(BEFORE_PREPROCESSING, fragment, "");
    }

    @Test
    public void interceptFragment_beforePreprocessing_withPreprocessor() {
        final Fragment fragment = fmd.createFragemnt("foo bar baz");
        fmd.register(mock(PreProcessor.class));
        fmd.register(interceptor, BEFORE_PREPROCESSING);

        fmd.render(fragment);

        verify(interceptor, times(1)).intercept(BEFORE_PREPROCESSING, fragment, "");
    }

    @Test
    public void interceptFragment_afterPreprocessing_withoutPreprocessor() {
        final Fragment fragment = fmd.createFragemnt("foo bar baz");
        fmd.register(interceptor, AFTER_PREPROCESSING);

        fmd.render(fragment);

        verify(interceptor, never()).intercept(AFTER_PREPROCESSING, fragment, "");
    }

    @Test
    public void interceptFragment_afterPreprocessing_withtPreprocessor() {
        final Fragment fragment = fmd.createFragemnt("foo bar baz");
        fmd.register(mock(PreProcessor.class));
        fmd.register(interceptor, AFTER_PREPROCESSING);

        fmd.render(fragment);

        verify(interceptor, times(1)).intercept(AFTER_PREPROCESSING, fragment, "");
    }

    @Test
    public void interceptFragment_beforeRendering() {
        final Fragment fragment = fmd.createFragemnt("foo bar baz");
        fmd.register(interceptor, BEFORE_RENDERING);

        fmd.render(fragment);

        verify(interceptor, never()).intercept(BEFORE_RENDERING, fragment, "foo bar baz");
    }

    @Test
    public void interceptFragment_afterRendering() {
        final Fragment fragment = fmd.createFragemnt("foo bar baz");
        fmd.register(interceptor, AFTER_RENDERING);

        fmd.render(fragment);

        verify(interceptor, times(1)).intercept(AFTER_RENDERING, fragment, "foo bar baz");
    }

    @Test
    public void interceptLayout_beforePreprocessing_withoutPreprocessor() {
        final Fragment fragment = fmd.createFragemnt("foo bar baz");
        final Layout layout = fmd.createLayout("Layout ${content}");
        layout.assignTemplateModel("content", fragment);
        fmd.register(interceptor, BEFORE_PREPROCESSING);

        fmd.render(layout);

        verify(interceptor, never()).intercept(BEFORE_PREPROCESSING, fragment, "");
        verify(interceptor, never()).intercept(BEFORE_PREPROCESSING, layout, "");
    }

    @Test
    @Ignore
    public void interceptLayout_beforePreprocessing_withoPreprocessor() {
        fmd.register(mock(PreProcessor.class));
        final Fragment fragment = fmd.createFragemnt("foo bar baz");
        final Layout layout = fmd.createLayout("Layout ${content}");
        layout.assignTemplateModel("content", fragment);
        fmd.register(interceptor, BEFORE_PREPROCESSING);

        fmd.render(layout);

        verify(interceptor, times(1)).intercept(BEFORE_PREPROCESSING, fragment, "");
        verify(interceptor, times(1)).intercept(BEFORE_PREPROCESSING, layout, "");
    }

    @Test
    @Ignore
    public void interceptLayout_afterPreprocessing() {

    }

    @Test
    @Ignore
    public void interceptLayout_beforeRendering() {

    }

    @Test
    @Ignore
    public void interceptLayout_afterRendering() {

    }
}
