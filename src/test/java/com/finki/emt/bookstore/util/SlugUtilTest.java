package com.finki.emt.bookstore.util;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SlugUtilTest {

    @Test
    public void testGenerate() {
        String text = "simple test";
        assertThat(SlugUtil.generate(text), equalTo("simple-test"));
    }

    @Test
    public void testGenerateOnUnAlphaCharacters() {
        String text = "`@s$%Imple^&*$24Tes$#@t";
        assertThat(SlugUtil.generate(text), equalTo("sImpleTest"));
    }

    @Test
    public void testGenerateOnNonUTF8Characters() {
        String text = "simple texтестt";
        assertThat(SlugUtil.generate(text), equalTo("simple-text"));
    }

    @Test
    public void testInvalidCharacters() {
        final String invalid = "te�st";
        final String valid = invalid.replaceAll("[^\\x20-\\x7e]", "");
        assertThat("test", is(equalTo(valid)));
    }
}
