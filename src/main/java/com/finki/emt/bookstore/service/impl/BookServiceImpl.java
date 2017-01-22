package com.finki.emt.bookstore.service.impl;

import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.domain.Category;
import com.finki.emt.bookstore.domain.User;
import com.finki.emt.bookstore.repository.BookRepository;
import com.finki.emt.bookstore.service.BookService;
import com.finki.emt.bookstore.service.CategoryService;
import com.finki.emt.bookstore.util.SlugUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    @Inject
    private BookRepository repository;

    @Inject
    private CategoryService categoryService;

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        log.debug("Request to get all Books");
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findById(long id) {
        log.debug("Request to get Book by id - {}", id);
        return Optional.ofNullable(repository.findOne(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findBySlug(String slug) {
        log.debug("Request to get Book by slug - {}", slug);
        return repository.findBySlug(slug);
    }

    @Override
    public Book save(Book book) {
        log.debug("Request to save Book - {}", book);
        return repository.save(book);
    }

    @Override
    public void delete(long id) {
        log.debug("Request to delete Book - {}", id);
        repository.delete(id);
    }

    @Override
    public Book create(String name, String shortDescription, String body, double price,
                       byte[] image, String published, String author, int year, int pages,
                       User admin, Set<Category> categories) {
        log.debug("Request for creating new Book - {}, {}", name, admin);
        ZonedDateTime now = ZonedDateTime.now();
        Book book = new Book();
        book.setName(name);
        book.setShortDescription(shortDescription);
        book.setBody(body);
        book.setCreatedAt(now);
        book.setUpdatedAt(now);
        book.setPrice(price);
        book.setImage(image);
        book.setPublisher(published);
        book.setAuthor(author);
        book.setYear(year);
        book.setPages(pages);

        book.setAdmin(admin);
        book.setCategories(categoryService.sync(categories)
                .stream().collect(Collectors.toSet()));

        // Set the slug
        final String slugged = SlugUtil.generate(name);
        String slug = repository.findBySlug(slugged)
                .map(b -> {
                    Book last = repository.findFirstByOrderById().orElse(new Book());
                    return slugged + "-" + (last.getId() + 1);
                })
                .orElse(slugged);
        book.setSlug(slug);

        return this.save(book);
    }
}
