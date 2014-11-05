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
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link Layout}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class LayoutTest {

    @Test(expected = NullPointerException.class)
    public void render_nullTemplate() {
        new Layout(null);

    }

    @Test
    public void render_emptyTemplate() throws IOException, TemplateException {
        assertThat(new Layout("").render(), is(""));
    }

    @Test
    public void render_notEmptyTemplate() throws IOException, TemplateException {
        assertThat(new Layout("foo bar baz").render(), is("foo bar baz"));
    }

    @Test
    public void render_notEmptyTemplateWithVariables() throws IOException, TemplateException {
        final List<String> fruits = new ArrayList<>();
        fruits.add("bananas");
        fruits.add("apples");
        fruits.add("pears");
        final Layout sut = new Layout(
                "<p>And BTW we have these fruits:\n"
                + "<ul>\n"
                + "<#list fruits as fruit>\n"
                + " <li>${fruit}</li>\n"
                + "</#list>\n"
                + "<ul>");
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
    public void render_withOneFragement() throws IOException, TemplateException {
        final Layout sut = new Layout("<p>${fragmentOne}</p>\n");
        sut.assignFragment("fragmentOne", new Fragment("foo"));

        assertThat(sut.render(), is("<p>foo</p>\n"));
    }

    @Test
    public void render_withThreeFragement() throws IOException, TemplateException {
        final Layout sut = new Layout(
            "<p>${fragmentOne}</p>\n"
            + "<p>${fragmentTwo}</p>\n"
            + "<p>${fragmentThree}</p>\n"
        );
        sut.assignFragment("fragmentOne", new Fragment("foo"));
        sut.assignFragment("fragmentTwo", new Fragment("bar"));
        sut.assignFragment("fragmentThree", new Fragment("baz"));

        assertThat(sut.render(), is(
            "<p>foo</p>\n"
            + "<p>bar</p>\n"
            + "<p>baz</p>\n"
        ));
    }

}
