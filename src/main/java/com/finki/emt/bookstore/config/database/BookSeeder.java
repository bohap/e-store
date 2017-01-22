package com.finki.emt.bookstore.config.database;

import com.finki.emt.bookstore.domain.Category;
import com.finki.emt.bookstore.domain.User;
import com.finki.emt.bookstore.security.AuthoritiesConstants;
import com.finki.emt.bookstore.service.BookService;
import com.finki.emt.bookstore.service.CategoryService;
import com.finki.emt.bookstore.service.UserService;
import com.finki.emt.bookstore.util.FileUtil;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
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
public class BookSeeder {

    private final Logger log = LoggerFactory.getLogger(BookSeeder.class);

    @Inject
    private BookService service;

    @Inject
    private UserService userService;

    @Inject
    private CategoryService categoryService;

    private final Lorem lorem = LoremIpsum.getInstance();

    private final Random rand = new Random();

    void seed() {
        log.debug("Request to seed Book entity");

        final double minPrice = 100;
        final double maxPrice = 10000;
        List<Category> categories = categoryService.findAll();
        List<User> admins = userService.findByAuthority(AuthoritiesConstants.ADMIN);

        IntStream.range(0 ,100)
                .forEach(i -> {
                    String name = lorem.getTitle(2, 4);
                    String shortDescription = lorem.getWords(10, 15);
                    String body = lorem.getParagraphs(3, 7);
                    double price = minPrice + (maxPrice - minPrice) * rand.nextDouble();
                    byte[] image = FileUtil.readBytes("img/book.png");
                    String publisher = lorem.getWords(2);
                    String author = lorem.getName();
                    int year = 1900 +  rand.nextInt(2017 - 1900);
                    int pages = rand.nextInt(1000 - 100) + 100;
                    User admin = rand(admins);
                    Collections.shuffle(categories);
                    Set<Category> bc = categories.subList(0, rand.nextInt(2) + 1).stream()
                            .collect(Collectors.toSet());
                    service.create(name, shortDescription, body, price, image, publisher,
                            author, year, pages, admin, bc);
                });
    }

    private <T> T rand(List<T> list) {
        int index = rand.nextInt(list.size());
        return list.get(index);
    }
}
