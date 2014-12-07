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
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link LayoutImpl}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class LayoutImplTest extends TestCaseBase {

    private final LayoutImpl sut = new LayoutImpl(
            "template",
            "utf-8",
            FreeMarkerDown.createConfiguration(ENCODING),
            Collections.<RenderOptions>emptySet(),
            "name");

    @Test
    public void toStringContainsAllProperties() {
        assertThat(sut.toString(), is(
                "Layout{"
                + "templateVariables=VariableScope{"
                    + "parent=null, "
                    + "data=Variables{vars={}}"
                + "}, "
                + "template=template, "
                + "encoding=utf-8, "
                + "templateName=name, "
                + "preProcessedTemplate=template, "
                + "fragments={}"
                + "}"));
    }

}
