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

import nl.jqno.equalsverifier.EqualsVerifier;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link FreeMarkerDown}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class FreeMarkerDownTest {

    @Rule
    //CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON

    private final FreeMarkerDown sut = FreeMarkerDown.create();

    @Test(expected = NullPointerException.class)
    public void register_preProcesorMustNotBeNull() {
        sut.register((PreProcessor) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void register_returnUnmodifiable() {
        sut.getPreProcessors().add(mock(PreProcessor.class));
    }

    @Test
    public void register() {
        final PreProcessor processor = mock(PreProcessor.class);

        sut.register(processor);

        assertThat(sut.getPreProcessors().size(), is(1));
        assertThat(sut.getPreProcessors(), contains(processor));
    }

    @Test
    public void newFragemnt_throwsExceptionIfTemplateIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'template'");

        FreeMarkerDown.newFragemnt(null);
    }

    @Test
    public void newFragemnt_withEncoding_throwsExceptionIfTemplateIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'template'");

        FreeMarkerDown.newFragemnt(null, "utf-8");
    }

    @Test
    public void newFragemnt_withEncoding_throwsExceptionIfEncodingIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'encoding'");

        FreeMarkerDown.newFragemnt("", null);
    }

    @Test
    public void newFragemnt_withEncoding_throwsExceptionIfEncodingIsEmpty() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'encoding'");

        FreeMarkerDown.newFragemnt("", "");
    }

    @Test
    public void newFragemnt_alwaysNewInstance() {
        final Fragment one = FreeMarkerDown.newFragemnt("");
        final Fragment two = FreeMarkerDown.newFragemnt("");
        final Fragment three = FreeMarkerDown.newFragemnt("");

        assertThat(one, is(not(sameInstance(two))));
        assertThat(one, is(not(sameInstance(three))));
        assertThat(two, is(not(sameInstance(three))));
    }

    @Test
    public void newFragemnt_withEncoding_alwaysNewInstance() {
        final Fragment one = FreeMarkerDown.newFragemnt("", "utf-8");
        final Fragment two = FreeMarkerDown.newFragemnt("", "utf-8");
        final Fragment three = FreeMarkerDown.newFragemnt("", "utf-8");

        assertThat(one, is(not(sameInstance(two))));
        assertThat(one, is(not(sameInstance(three))));
        assertThat(two, is(not(sameInstance(three))));
    }

    @Test
    public void newLayout_throwsExceptionIfTemplateIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'template'");

        FreeMarkerDown.newLayout(null);
    }

    @Test
    public void newLayout_withEncoding_throwsExceptionIfTemplateIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'template'");

        FreeMarkerDown.newLayout(null, "utf-8");
    }

    @Test
    public void newLayout_withEncoding_throwsExceptionIfEncodingIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'encoding'");

        FreeMarkerDown.newLayout("", null);
    }

    @Test
    public void newLayout_withEncoding_throwsExceptionIfEncodingIsEmpty() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'encoding'");

        FreeMarkerDown.newLayout("", "");
    }

    @Test
    public void newLayout_alwaysNewInstance() {
        final Layout one = FreeMarkerDown.newLayout("");
        final Layout two = FreeMarkerDown.newLayout("");
        final Layout three = FreeMarkerDown.newLayout("");

        assertThat(one, is(not(sameInstance(two))));
        assertThat(one, is(not(sameInstance(three))));
        assertThat(two, is(not(sameInstance(three))));
    }

    @Test
    public void newLayout_withEncoding_alwaysNewInstance() {
        final Layout one = FreeMarkerDown.newLayout("", "utf-8");
        final Layout two = FreeMarkerDown.newLayout("", "utf-8");
        final Layout three = FreeMarkerDown.newLayout("", "utf-8");

        assertThat(one, is(not(sameInstance(two))));
        assertThat(one, is(not(sameInstance(three))));
        assertThat(two, is(not(sameInstance(three))));
    }

    @Test
    public void create_alwaysNewInstance() {
        final FreeMarkerDown one = FreeMarkerDown.create();
        final FreeMarkerDown two = FreeMarkerDown.create();
        final FreeMarkerDown three = FreeMarkerDown.create();

        assertThat(one, is(not(sameInstance(two))));
        assertThat(one, is(not(sameInstance(three))));
        assertThat(two, is(not(sameInstance(three))));
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(FreeMarkerDown.class)
                .verify();
    }

    @Test
    public void toStringContainsMembers() {
        assertThat(sut.toString(), is("FreeMarkerDown{preProcessors=[]}"));
    }

}
