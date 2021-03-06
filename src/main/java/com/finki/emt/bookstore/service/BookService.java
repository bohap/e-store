package com.finki.emt.bookstore.service;

import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.domain.Category;
import com.finki.emt.bookstore.domain.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {

    List<Book> findAll();

    List<Book> findAll(Optional<Integer> offset, Optional<Integer> limit,
                       Optional<Boolean> latest, Optional<String> categories);

    long count();

    Optional<Book> findById(long id);

    Optional<Book> findBySlug(String slug);

    Book save(Book book);

    void delete(long id);

    Book create(String name, String shortDescription, String body, double price,
                byte[] image, String published, String author, int year, int pages,
                User admin, Set<Category> categories);

    Book create(Book book, User admin);

    Book update(String slug, Book book, User admin);

    void delete(String slug);

    boolean isAddedToTheFavorites(String slug, User user);

    Book addToFavorites(String bookSlug, User user);

    Book removeFromFavorites(String bookSlug, User user);

    List<Book> search(String phrase, Optional<Integer> offset, Optional<Integer> limit);

    List<Book> findPopular(Optional<Integer> offset, Optional<Integer> limit);
}
