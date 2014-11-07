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
 * Tests for {@link FreemarkerProperties}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class FreemarkerPropertiesTest {

    private final FreemarkerProperties sut = new FreemarkerProperties();

    @Test
    public void getVersion() {
        assertThat(sut.getVersion(), is("2.3.21"));
    }

    @Test
    public void toStringContainsVersion() {
        assertThat(sut.toString(), is("2.3.21"));
    }

}
