package com.finki.emt.bookstore.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public class PageRequestUtil {

    public static PageRequest create(Optional<Integer> limit, Optional<Integer> offset,
                                     Optional<Boolean> latest, String... orderByColumns) {
        final int pageLimit = limit.orElse(Integer.MAX_VALUE);
        final int pageNumber = offset.map(o -> o / pageLimit).orElse(0);
        final boolean orderByLatest = latest.orElse(false);

        if (orderByLatest) {
            return new PageRequest(pageNumber, pageLimit, Sort.Direction.DESC, orderByColumns);
        }
        return new PageRequest(pageNumber, pageLimit);
    }
}
