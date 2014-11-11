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

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import java.io.IOException;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * Tests for {@link FreeMarker}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class FreeMarkerTest {

    private final FreeMarker sut = new FreeMarker();

    @Test
    public void createTemplate_alwaysNewInstance() throws IOException {
        final FreeMarker fm = new FreeMarker();
        final Template one = sut.createTemplate("", fm.createConfiguration());
        final Template two = sut.createTemplate("", fm.createConfiguration());
        final Template three = sut.createTemplate("", fm.createConfiguration());

        assertThat(one, is(not(sameInstance(two))));
        assertThat(one, is(not(sameInstance(three))));
        assertThat(two, is(not(sameInstance(three))));
    }

    @Test
    public void createConfiguration_alwaysNewInstance() {
        final Configuration one = sut.createConfiguration();
        final Configuration two = sut.createConfiguration();
        final Configuration three = sut.createConfiguration();

        assertThat(one, is(not(sameInstance(two))));
        assertThat(one, is(not(sameInstance(three))));
        assertThat(two, is(not(sameInstance(three))));
    }

    @Test
    public void createDefaultObjectWrapper_alwaysSameInstance() {
        final DefaultObjectWrapper one = sut.createDefaultObjectWrapper();
        final DefaultObjectWrapper two = sut.createDefaultObjectWrapper();
        final DefaultObjectWrapper three = sut.createDefaultObjectWrapper();

        assertThat(one, is(sameInstance(two)));
        assertThat(one, is(sameInstance(three)));
        assertThat(two, is(sameInstance(three)));
    }

}
