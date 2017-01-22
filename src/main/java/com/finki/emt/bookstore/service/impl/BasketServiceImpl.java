package com.finki.emt.bookstore.service.impl;

import com.finki.emt.bookstore.domain.Basket;
import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.repository.BasketRepository;
import com.finki.emt.bookstore.service.BasketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BasketServiceImpl implements BasketService {

    private Logger log = LoggerFactory.getLogger(BasketServiceImpl.class);

    @Inject
    private BasketRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Basket> findAll() {
        log.debug("Request to get all Baskets");
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Basket> findById(long id) {
        log.debug("Request to get a Basket by id - {}", id);
        return repository.findByUserId(id);
    }

    @Override
    public Basket addBook(long id, Book book) {
        log.debug("Request to add book to a basket - {}, {}", id, book);
        Basket basket = repository.findByUserId(id).orElseThrow(() ->
                new IllegalArgumentException("basket with id " + id + " don't exists"));
        basket.getBooks().add(book);
        return repository.save(basket);
    }

    @Override
    public Basket addBooks(long id, Collection<Book> books) {
        log.debug("Request to add books to a basket - {}, {}", id, books);
        Basket basket = repository.findByUserId(id).orElseThrow(() ->
                new IllegalArgumentException("Basket with id " + id + " don't exists"));
        basket.getBooks().addAll(books);
        return repository.save(basket);
    }
}
