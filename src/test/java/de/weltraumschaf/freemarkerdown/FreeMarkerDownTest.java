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
import static org.hamcrest.Matchers.*;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.mockito.Mockito.*;
import org.pegdown.PegDownProcessor;
import org.pegdown.plugins.PegDownPlugins;

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
    public void createFragemnt_throwsExceptionIfTemplateIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'template'");

        sut.createFragemnt((String) null);
    }

    @Test
    public void createFragemnt_withEncoding_throwsExceptionIfTemplateIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'template'");

        sut.createFragemnt((String) null, "utf-8");
    }

    @Test
    public void createFragemnt_withEncoding_throwsExceptionIfEncodingIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'encoding'");

        sut.createFragemnt("", null);
    }

    @Test
    public void createFragemnt_withEncoding_throwsExceptionIfEncodingIsEmpty() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'encoding'");

        sut.createFragemnt("", "");
    }

    @Test
    public void createFragemnt_alwaysNewInstance() {
        final Fragment one = sut.createFragemnt("");
        final Fragment two = sut.createFragemnt("");
        final Fragment three = sut.createFragemnt("");

        assertThat(one, is(not(sameInstance(two))));
        assertThat(one, is(not(sameInstance(three))));
        assertThat(two, is(not(sameInstance(three))));
    }

    @Test
    public void createFragemnt_withEncoding_alwaysNewInstance() {
        final Fragment one = sut.createFragemnt("", "utf-8");
        final Fragment two = sut.createFragemnt("", "utf-8");
        final Fragment three = sut.createFragemnt("", "utf-8");

        assertThat(one, is(not(sameInstance(two))));
        assertThat(one, is(not(sameInstance(three))));
        assertThat(two, is(not(sameInstance(three))));
    }

    @Test
    public void createLayout_throwsExceptionIfTemplateIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'template'");

        sut.createLayout((String) null);
    }

    @Test
    public void createLayout_withEncoding_throwsExceptionIfTemplateIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'template'");

        sut.createLayout((String) null, "utf-8");
    }

    @Test
    public void createLayout_withEncoding_throwsExceptionIfEncodingIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'encoding'");

        sut.createLayout("", null);
    }

    @Test
    public void createLayout_withEncoding_throwsExceptionIfEncodingIsEmpty() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'encoding'");

        sut.createLayout("", "");
    }

    @Test
    public void createLayout_alwaysNewInstance() {
        final Layout one = sut.createLayout("");
        final Layout two = sut.createLayout("");
        final Layout three = sut.createLayout("");

        assertThat(one, is(not(sameInstance(two))));
        assertThat(one, is(not(sameInstance(three))));
        assertThat(two, is(not(sameInstance(three))));
    }

    @Test
    public void createLayout_withEncoding_alwaysNewInstance() {
        final Layout one = sut.createLayout("", "utf-8");
        final Layout two = sut.createLayout("", "utf-8");
        final Layout three = sut.createLayout("", "utf-8");

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
                .withPrefabValues(PegDownProcessor.class, new PegDownProcessor(), new PegDownProcessor())
                .verify();
    }

    @Test
    public void toStringContainsMembers() {
        assertThat(sut.toString(), is("FreeMarkerDown{preProcessors=[]}"));
    }

}
