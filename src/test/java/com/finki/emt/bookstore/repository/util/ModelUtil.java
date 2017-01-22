package com.finki.emt.bookstore.repository.util;

import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.domain.User;
import com.finki.emt.bookstore.util.FileUtil;

import java.time.ZonedDateTime;
import java.util.Random;

import static org.hibernate.internal.util.StringHelper.repeat;

public class ModelUtil {

    private static final Random rand = new Random();

    public static User mockUser(long id, ZonedDateTime time) {
        String name = "User " + id;
        String slug = "user-" + id;
        String email = "user-" + id + "@test.com";
        String password = "password";
        return new User(id, name, slug, email, password, time, time);
    }

    public static Book mockBook(long id, ZonedDateTime time, User admin) {
        String name = "Book " + id;
        String slug = "book-" + id;
        String shortDescription = repeat("description " + id, 10);
        String body = repeat("body " + id, 20);
        double price = rand.nextDouble() * 10000;
        byte[] image = FileUtil.readBytes("img/book.png");
        int year = rand.nextInt(3000);
        int pages = rand.nextInt(300);
        String publisher = "Published " + id;
        String author = "Author " + id;
        Book book = new Book(id, name, slug, shortDescription, body, time, time, price, image,
                publisher, author, year, pages);
        book.setAdmin(admin);
        return book;
    }
}
