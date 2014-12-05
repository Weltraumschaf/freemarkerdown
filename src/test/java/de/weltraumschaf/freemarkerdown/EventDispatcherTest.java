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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests for {@link EventDispatcher}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
@RunWith(MockitoJUnitRunner.class)
public class EventDispatcherTest {

    @Mock
    private Interceptor interceptorOne;
    @Mock
    private Interceptor interceptorTwo;
    @Mock
    private TemplateModel template;

    private final EventDispatcher sut = new EventDispatcher();

    @Test
    public void trigger_BEFORE_PREPROCESSING() {
        sut.register(interceptorOne, BEFORE_PREPROCESSING);
        sut.register(interceptorTwo, BEFORE_PREPROCESSING);
        final Event event = new Event(BEFORE_PREPROCESSING, template, "");

        sut.trigger(event);

        verify(interceptorOne, times(1)).intercept(BEFORE_PREPROCESSING, template, "");
        verify(interceptorTwo, times(1)).intercept(BEFORE_PREPROCESSING, template, "");
    }

    @Test
    public void trigger_AFTER_PREPROCESSING() {
        sut.register(interceptorOne, AFTER_PREPROCESSING);
        sut.register(interceptorTwo, AFTER_PREPROCESSING);
        final Event event = new Event(AFTER_PREPROCESSING, template, "");

        sut.trigger(event);

        verify(interceptorOne, times(1)).intercept(AFTER_PREPROCESSING, template, "");
        verify(interceptorTwo, times(1)).intercept(AFTER_PREPROCESSING, template, "");
    }

    @Test
    public void trigger_BEFORE_RENDERING() {
        sut.register(interceptorOne, BEFORE_RENDERING);
        sut.register(interceptorTwo, BEFORE_RENDERING);
        final Event event = new Event(BEFORE_RENDERING, template, "");

        sut.trigger(event);

        verify(interceptorOne, times(1)).intercept(BEFORE_RENDERING, template, "");
        verify(interceptorTwo, times(1)).intercept(BEFORE_RENDERING, template, "");
    }

    @Test
    public void trigger_AFTER_RENDERING() {
        sut.register(interceptorOne, AFTER_RENDERING);
        sut.register(interceptorTwo, AFTER_RENDERING);
        final Event event = new Event(AFTER_RENDERING, template, "");

        sut.trigger(event);

        verify(interceptorOne, times(1)).intercept(AFTER_RENDERING, template, "");
        verify(interceptorTwo, times(1)).intercept(AFTER_RENDERING, template, "");
    }

    @Test
    public void trigger_BEFORE_MARKDOWN() {
        sut.register(interceptorOne, BEFORE_MARKDOWN);
        sut.register(interceptorTwo, BEFORE_MARKDOWN);
        final Event event = new Event(BEFORE_MARKDOWN, template, "");

        sut.trigger(event);

        verify(interceptorOne, times(1)).intercept(BEFORE_MARKDOWN, template, "");
        verify(interceptorTwo, times(1)).intercept(BEFORE_MARKDOWN, template, "");
    }

    @Test
    public void trigger_AFTER_MARKDOWN() {
        sut.register(interceptorOne, AFTER_MARKDOWN);
        sut.register(interceptorTwo, AFTER_MARKDOWN);
        final Event event = new Event(AFTER_MARKDOWN, template, "");

        sut.trigger(event);

        verify(interceptorOne, times(1)).intercept(AFTER_MARKDOWN, template, "");
        verify(interceptorTwo, times(1)).intercept(AFTER_MARKDOWN, template, "");
    }
}
