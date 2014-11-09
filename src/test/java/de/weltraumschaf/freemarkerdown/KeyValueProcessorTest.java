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

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * Tests for {@link KeyValueProcessor}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class KeyValueProcessorTest {

    private final Map<String, String> result = new HashMap<>();
    private final KeyValueProcessor sut = new KeyValueProcessor(result);

    @Test
    public void testSomeMethod() {
    }

}
