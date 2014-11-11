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
 * Tests for {@link Layout}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Layout_PropagateVariablesTest {

    private final Layout sut = new LayoutImpl("", Defaults.ENCODING.getValue(), new FreeMarker().createConfiguration());

    @Test
    public void propagateAssigendVariableToFragment() {
        final FragmentImpl fragment = new FragmentImpl(
                "",
                Defaults.ENCODING.getValue(),
                new FreeMarker().createConfiguration());
        sut.assignTemplateModel("fragment", fragment);

        sut.assignVariable("foo", "bar");

        assertThat(fragment.getTemplateVariables().size(), is(1));
        assertThat(fragment.getTemplateVariables(), hasEntry("foo", (Object) "bar"));
    }

    @Test
    public void propagateAssigendVariableToLateAssignedFragment() {
        sut.assignVariable("foo", "bar");
        final FragmentImpl fragment = new FragmentImpl(
                "",
                Defaults.ENCODING.getValue(),
                new FreeMarker().createConfiguration());

        sut.assignTemplateModel("fragment", fragment);
        assertThat(fragment.getTemplateVariables().size(), is(1));
        assertThat(fragment.getTemplateVariables(), hasEntry("foo", (Object) "bar"));
    }

    @Test
    public void propagateAssigendVariableToLayoutwithFragments() {
        final FragmentImpl fragmentOne = new FragmentImpl(
                "",
                Defaults.ENCODING.getValue(),
                new FreeMarker().createConfiguration());
        sut.assignTemplateModel("fragmentOne", fragmentOne);
        final LayoutImpl inner = new LayoutImpl(
                "",
                Defaults.ENCODING.getValue(),
                new FreeMarker().createConfiguration());
        sut.assignTemplateModel("inner", inner);
        final FragmentImpl fragmentTwo = new FragmentImpl(
                "",
                Defaults.ENCODING.getValue(),
                new FreeMarker().createConfiguration());
        inner.assignTemplateModel("fragmentTwo", fragmentTwo);
        final FragmentImpl fragmentThree = new FragmentImpl(
                "",
                Defaults.ENCODING.getValue(),
                new FreeMarker().createConfiguration());
        inner.assignTemplateModel("fragmentThree", fragmentThree);

        sut.assignVariable("foo", "bar");

        assertThat(fragmentOne.getTemplateVariables().size(), is(1));
        assertThat(fragmentOne.getTemplateVariables(), hasEntry("foo", (Object) "bar"));
        assertThat(fragmentTwo.getTemplateVariables().size(), is(1));
        assertThat(fragmentTwo.getTemplateVariables(), hasEntry("foo", (Object) "bar"));
        assertThat(fragmentThree.getTemplateVariables().size(), is(1));
        assertThat(fragmentThree.getTemplateVariables(), hasEntry("foo", (Object) "bar"));
        assertThat(inner.getTemplateVariables().size(), is(1));
        assertThat(inner.getTemplateVariables(), hasEntry("foo", (Object) "bar"));
    }

    @Test
    public void propagateAssigendVariableToLateAssignedLayoutWithFragments() {
        sut.assignVariable("foo", "bar");

        final FragmentImpl fragmentOne = new FragmentImpl(
                "",
                Defaults.ENCODING.getValue(),
                new FreeMarker().createConfiguration());
        sut.assignTemplateModel("fragmentOne", fragmentOne);
        final LayoutImpl inner = new LayoutImpl(
                "",
                Defaults.ENCODING.getValue(),
                new FreeMarker().createConfiguration());
        sut.assignTemplateModel("inner", inner);
        final FragmentImpl fragmentTwo = new FragmentImpl(
                "",
                Defaults.ENCODING.getValue(),
                new FreeMarker().createConfiguration());
        inner.assignTemplateModel("fragmentTwo", fragmentTwo);
        final FragmentImpl fragmentThree = new FragmentImpl(
                "",
                Defaults.ENCODING.getValue(),
                new FreeMarker().createConfiguration());
        inner.assignTemplateModel("fragmentThree", fragmentThree);

        assertThat(fragmentOne.getTemplateVariables().size(), is(1));
        assertThat(fragmentOne.getTemplateVariables(), hasEntry("foo", (Object) "bar"));
        assertThat(fragmentTwo.getTemplateVariables().size(), is(1));
        assertThat(fragmentTwo.getTemplateVariables(), hasEntry("foo", (Object) "bar"));
        assertThat(fragmentThree.getTemplateVariables().size(), is(1));
        assertThat(fragmentThree.getTemplateVariables(), hasEntry("foo", (Object) "bar"));
        assertThat(inner.getTemplateVariables().size(), is(1));
        assertThat(inner.getTemplateVariables(), hasEntry("foo", (Object) "bar"));
    }
}
