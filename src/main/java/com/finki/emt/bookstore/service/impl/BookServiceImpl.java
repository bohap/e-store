package com.finki.emt.bookstore.service.impl;

import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.domain.Category;
import com.finki.emt.bookstore.domain.User;
import com.finki.emt.bookstore.repository.BookRepository;
import com.finki.emt.bookstore.service.BookService;
import com.finki.emt.bookstore.service.CategoryService;
import com.finki.emt.bookstore.util.PageRequestUtil;
import com.finki.emt.bookstore.util.SlugUtil;
import com.finki.emt.bookstore.web.rest.errors.ModelNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
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
    public List<Book> findAll(Optional<Integer> limit, Optional<Integer> offset,
                              Optional<Boolean> latest) {
        PageRequest pageRequest = PageRequestUtil.create(limit, offset, latest, "updatedAt");
        return repository.findAll(pageRequest).getContent();
    }

    @Override
    public List<Book> filterByCategory(Optional<Integer> limit, Optional<Integer> offset,
                                       Optional<Boolean> latest, String... categories) {
        PageRequest pageRequest = PageRequestUtil.create(limit, offset, latest, "updatedAt");
        return repository.findDistinctByCategoriesNameIn(pageRequest, categories);
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
        book.setSlug(createSlug(name));

        return this.save(book);
    }

    @Override
    public Book create(Book book, User admin) {
        log.debug("Request to create Book - {}, {} ", book, admin);
        ZonedDateTime now = ZonedDateTime.now();
        book.setCreatedAt(now);
        book.setUpdatedAt(now);
        book.setAdmin(admin);
        book.setSlug(createSlug(book.getName()));

        // Set the categories
        book.setCategories(categoryService.sync(book.getCategories()).stream()
                .collect(Collectors.toSet()));

        return this.save(book);
    }

    private String createSlug(String name) {
        final String slugged = SlugUtil.generate(name);
        return repository.findBySlug(slugged)
                .map(b -> {
                    Book last = repository.findFirstByOrderById().orElse(new Book());
                    return slugged + "-" + (last.getId() + 1);
                })
                .orElse(slugged);
    }

    @Override
    public Book update(String slug, Book book, User admin) {
        Book entity = repository.findBySlug(slug).orElseThrow(() ->
                new ModelNotFoundException("book with slug " + slug + " can't be find"));
        ZonedDateTime now = ZonedDateTime.now();
        entity.setUpdatedAt(now);
        entity.setName(book.getName());
        entity.setShortDescription(book.getShortDescription());
        entity.setBody(book.getBody());
        entity.setPrice(book.getPrice());
        entity.setPublisher(book.getPublisher());
        entity.setAuthor(book.getAuthor());
        entity.setYear(book.getYear());
        entity.setPages(book.getPages());
        book.setAdmin(admin);

        // Set the image
        if (book.getImage() != null) {
            entity.setImage(book.getImage());
        }

        // Set the categories
        entity.setCategories(categoryService.sync(book.getCategories()).stream()
                .collect(Collectors.toSet()));

        return this.save(entity);
    }

    @Override
    public void delete(String slug) {
        Book book = repository.findBySlug(slug).orElseThrow(() ->
                new ModelNotFoundException("book with slug " + slug + " can't be find"));
        repository.delete(book.getId());
    }
}
