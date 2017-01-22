package com.finki.emt.bookstore.config.database;

import com.finki.emt.bookstore.domain.Basket;
import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.service.BasketService;
import com.finki.emt.bookstore.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Component
public class BasketSeeder {

    private final Logger log = LoggerFactory.getLogger(BasketSeeder.class);

    @Inject
    private BasketService service;

    @Inject
    private BookService bookService;

    private final Random rand = new Random();

    void seed() {
        log.debug("Request for seeding basket books");
        List<Basket> baskets = service.findAll();
        List<Book> books = bookService.findAll();

        Collections.shuffle(baskets);
        int size = rand.nextInt(baskets.size());
        IntStream.range(0, size)
                .forEach(i -> {
                    Basket b = baskets.get(i);
                    Collections.shuffle(books);
                    Collection<Book> basketBooks = books.subList(0, rand.nextInt(books.size() / 2));
                    service.addBooks(b.getUserId(), basketBooks);
                });
    }
}
