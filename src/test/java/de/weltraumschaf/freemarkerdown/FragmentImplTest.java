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

import java.util.Collections;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * Tests for {@link FragmentImpl}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class FragmentImplTest {

    private final FragmentImpl sut = new FragmentImpl(
            "template",
            "utf-8",
            FreeMarkerDown.createConfiguration(),
            Collections.<RenderOptions>emptySet(),
            "name");

    @Test
    public void toStringContainsAllProperties() {
        assertThat(sut.toString(), is(
                "Fragment{"
                + "templateVariables=VariableScope{"
                    + "parent=null, "
                    + "data=Variables{vars={}}"
                + "}, "
                + "template=template, "
                + "encoding=utf-8, "
                + "preProcessedTemplate=template"
                + "}"));
    }

}
