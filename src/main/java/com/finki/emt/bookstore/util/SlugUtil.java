package com.finki.emt.bookstore.util;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class SlugUtil {

    public static String generate(String text) {
        String result = text.replaceAll("[^a-zA-Z ]", "").toLowerCase(Locale.ENGLISH);
        return Arrays.stream(result.split("\\s+"))
                .collect(Collectors.joining("-"));
    }
}
