package com.finki.emt.bookstore.config.database;

import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.domain.User;
import com.finki.emt.bookstore.service.BookService;
import com.finki.emt.bookstore.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class FavoriteBooksSeeder {

    private final Logger log = LoggerFactory.getLogger(FavoriteBooksSeeder.class);

    @Inject
    private UserService userService;

    @Inject
    private BookService bookService;

    private final Random rand = new Random();

    void seed() {
        log.debug("Request for seeding favorite books");
        List<User> users = userService.findAll();
        List<Book> books = bookService.findAll();

        Collections.shuffle(books);
        int size = rand.nextInt(books.size());
        IntStream.range(0, size)
                .forEach(i -> {
                    Book book = books.get(i);
                    Collections.shuffle(users);
                    Set<User> favorites = users.subList(0, rand.nextInt(users.size() / 2)).stream()
                            .collect(Collectors.toSet());
                    book.setFavorites(favorites);
                    bookService.save(book);
                });
    }
}
