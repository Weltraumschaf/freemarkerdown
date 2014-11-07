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

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link TemplateError}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
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
