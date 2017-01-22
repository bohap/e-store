package com.finki.emt.bookstore.config.database;

import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.domain.User;
import com.finki.emt.bookstore.security.AuthoritiesConstants;
import com.finki.emt.bookstore.service.BookService;
import com.finki.emt.bookstore.service.OrderService;
import com.finki.emt.bookstore.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Component
public class OrderSeeder {

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Inject
    private OrderService service;

    @Inject
    private BookService bookService;

    @Inject
    private UserService userService;

    private final Random random = new Random();

    void seed() {
        log.debug("Request to seed Order entity");
        List<User> users = userService.findByAuthority(AuthoritiesConstants.USER);
        List<Book> books = bookService.findAll();

        IntStream.range(0, 300)
                .forEach(i -> {
                    User user = users.get(random.nextInt(users.size()));
                    Collections.shuffle(books);
                    List<Book> orderBooks = books.subList(0, random.nextInt(14) + 1);
                    service.create(user, orderBooks);
                });
    }
}
