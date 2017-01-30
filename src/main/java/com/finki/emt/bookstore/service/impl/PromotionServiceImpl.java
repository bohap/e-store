package com.finki.emt.bookstore.service.impl;

import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.domain.Promotion;
import com.finki.emt.bookstore.repository.PromotionRepository;
import com.finki.emt.bookstore.service.BookService;
import com.finki.emt.bookstore.service.PromotionService;
import com.finki.emt.bookstore.util.exceptions.ModelNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {

    private final Logger log = LoggerFactory.getLogger(PromotionServiceImpl.class);

    @Inject
    private PromotionRepository repository;

    @Inject
    private BookService bookService;

    @Override
    public List<Promotion> findAll() {
        log.debug("Request to get all Promotions");
        return repository.findAll();
    }

    @Override
    public Optional<Promotion> findById(long id) {
        log.debug("Request to get Promotion by id - {}", id);
        return repository.findByBookId(id);
    }

    @Override
    public void delete(long id) {
        log.debug("Request to delete promotion - {}", id);
        Book book = bookService.findById(id).orElseThrow(() ->
                new ModelNotFoundException("book with id " + id + " can't be find"));
        book.setPromotion(null);
        bookService.save(book);
    }

    @Override
    public void delete(String bookSlug) {
        log.debug("Request to delete promotion for book - {}", bookSlug);
        Book book = bookService.findBySlug(bookSlug).orElseThrow(() ->
                new ModelNotFoundException("book with slug " + bookSlug + " can't be find"));
        book.setPromotion(null);
        bookService.save(book);
    }

    @Override
    public void delete(Promotion promotion) {
        log.debug("Request to delete promotion, {}", promotion);
        Book book = promotion.getBook();
        book.setPromotion(null);
        bookService.save(book);
    }

    @Override
    public Promotion create(Book book, double newPrice, ZonedDateTime start, ZonedDateTime end) {
        log.debug("Request to create promotion for Book - {}", book);
        Promotion promotion = new Promotion(book, newPrice, start, end);
        book.setPromotion(promotion);
        bookService.save(book);
        return promotion;
    }

    @Override
    public Promotion save(String bookSlug, Promotion promotion) {
        log.debug("Request to save promotion - {}, {}", bookSlug, promotion);
        Book book = bookService.findBySlug(bookSlug).orElseThrow(() ->
                new ModelNotFoundException("book with slug " + bookSlug + " can't be find"));
        promotion.setBook(book);
        book.setPromotion(promotion);
        bookService.save(book);
        return promotion;
    }

    @Override
    public List<Promotion> findLatest() {
        log.debug("Request to get latest promotions");
        return repository.findTop10ByOrderByStart();
    }

    @Override
    public void deleteExpired() {
        log.debug("Request to delete expired promotions");
        ZonedDateTime now = ZonedDateTime.now();
        List<Promotion> promotions = repository.findAllByEndBefore(now);
        promotions.forEach(this::delete);
    }
}
