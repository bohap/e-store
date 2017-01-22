package com.finki.emt.bookstore.config.database;

import com.finki.emt.bookstore.domain.Category;
import com.finki.emt.bookstore.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.stream.Stream;

@Component
public class CategorySeeder {

    private final Logger log = LoggerFactory.getLogger(CategorySeeder.class);

    @Inject
    private CategoryService service;

    private String[] categories = {"architecture", "art", "business", "fiction",
            "music", "biography", "humor", "sport", "health", "science",
            "programming", "development", "history", "comics"};

    void seed() {
        log.debug("Request to seed Category entity");
        Stream.of(categories)
                .filter(c -> !service.exists(c))
                .map(c -> new Category(0, c))
                .forEach(c -> service.save(c));
    }
}
