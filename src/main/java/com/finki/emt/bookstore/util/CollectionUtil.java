package com.finki.emt.bookstore.util;

import java.util.Collection;

public class CollectionUtil {

    public static <T> boolean equals(Collection<T> c1, Collection<T> c2) {
        if (c1 == c2) {
            return true;
        }
        if (c1 != null && c2 == null && c1.size() == 0 || c1 == null && c2.size() == 0) {
            return true;
        }
        if (c1 == null || c2 == null || c1.size() != c2.size()) {
            return false;
        }
        int count = 0;
        for (T el1 : c1) {
            for (T el2 : c2) {
                if (el1.equals(el2)) {
                    count++;
                    break;
                }
            }
        }
        return count == c1.size();
    }
}
