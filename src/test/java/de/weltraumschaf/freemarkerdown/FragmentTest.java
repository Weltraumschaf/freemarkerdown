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

import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * Tests for {@link Fragment}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class FragmentTest {

    @Test(expected = NullPointerException.class)
    public void render_nullTemplate() {
        new FragmentImpl(null, Defaults.ENCODING.getValue());

    }

    @Test
    public void render_emptyTemplate() throws IOException, TemplateException {
        assertThat(new FragmentImpl("", Defaults.ENCODING.getValue()).render(), is(""));
    }

    @Test
    public void render_notEmptyTemplate() throws IOException, TemplateException {
        assertThat(new FragmentImpl("foo bar baz", Defaults.ENCODING.getValue()).render(), is("foo bar baz"));
    }

    @Test
    public void render_notEmptyTemplateWithVariables() throws IOException, TemplateException {
        final List<String> fruits = new ArrayList<>();
        fruits.add("bananas");
        fruits.add("apples");
        fruits.add("pears");
        final Fragment sut = new FragmentImpl(
                "<p>And BTW we have these fruits:\n"
                        + "<ul>\n"
                        + "<#list fruits as fruit>\n"
                        + " <li>${fruit}</li>\n"
                        + "</#list>\n"
                        + "<ul>", Defaults.ENCODING.getValue());
        sut.assignVariable("fruits", fruits);

        assertThat(sut.render(),
            is("<p>And BTW we have these fruits:\n"
                + "<ul>\n"
                + " <li>bananas</li>\n"
                + " <li>apples</li>\n"
                + " <li>pears</li>\n"
                + "<ul>"));
    }

}