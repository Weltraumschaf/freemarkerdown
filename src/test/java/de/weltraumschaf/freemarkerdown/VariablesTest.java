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

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * Tests for {2link Variables}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class VariablesTest {

    private final Variables sut = new Variables();

    @Test
    public void getVariables_emptyByDefault() {
        assertThat(sut.getVariables().isEmpty(), is(true));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getVariables_returnUnmodifiable() {
        sut.getVariables().put("foo", new Object());
    }

    @Test
    public void getVariables_oneVariableAssigned() {
        final Object foo = new Object();
        sut.assignVariable("foo", foo);

        assertThat(sut.getVariables().size(), is(1));
        assertThat(sut.getVariables(), hasEntry("foo", foo));
    }

    @Test
    public void getVariables_threeVariableAssigned() {
        final Object foo = new Object();
        sut.assignVariable("foo", foo);
        final Object bar = new Object();
        sut.assignVariable("bar", bar);
        final Object baz = new Object();
        sut.assignVariable("baz", baz);

        assertThat(sut.getVariables().size(), is(3));
        assertThat(sut.getVariables(), hasEntry("foo", foo));
        assertThat(sut.getVariables(), hasEntry("bar", bar));
        assertThat(sut.getVariables(), hasEntry("baz", baz));
    }

}
