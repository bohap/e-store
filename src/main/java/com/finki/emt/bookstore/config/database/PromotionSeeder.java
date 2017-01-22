package com.finki.emt.bookstore.config.database;

import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.service.BookService;
import com.finki.emt.bookstore.service.PromotionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Component
public class PromotionSeeder {

    private final Logger log = LoggerFactory.getLogger(PromotionSeeder.class);

    @Inject
    private PromotionService service;

    @Inject
    private BookService bookService;

    private final Random rand = new Random();

    void seed() {
        log.debug("Request to seed Promotion entity");
        ZonedDateTime now = ZonedDateTime.now();
        List<Book> books = bookService.findAll();
        Collections.shuffle(books);
        int size = rand.nextInt(books.size() / 3);
        books.subList(0, size)
                .forEach(b -> {
                    int startDays = rand.nextInt(3);
                    int endDays = rand.nextInt(7) + startDays;
                    ZonedDateTime start = now.plusDays(startDays);
                    ZonedDateTime end = now.plusDays(endDays);
                    double price = b.getPrice() - (b.getPrice() * rand.nextDouble());
                    service.create(b, price, start, end);
                });
    }
}
