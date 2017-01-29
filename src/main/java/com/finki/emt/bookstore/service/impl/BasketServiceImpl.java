package com.finki.emt.bookstore.service.impl;

import com.finki.emt.bookstore.domain.Basket;
import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.domain.Order;
import com.finki.emt.bookstore.domain.User;
import com.finki.emt.bookstore.repository.BasketRepository;
import com.finki.emt.bookstore.service.BasketService;
import com.finki.emt.bookstore.service.BookService;
import com.finki.emt.bookstore.service.OrderService;
import com.finki.emt.bookstore.service.UserService;
import com.finki.emt.bookstore.util.exceptions.EmptyBasketException;
import com.finki.emt.bookstore.util.exceptions.ModelNotFoundException;
import com.finki.emt.bookstore.web.rest.vm.BookOrderVM;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Service("basketService")
@Transactional
public class BasketServiceImpl implements BasketService {

    private Logger log = LoggerFactory.getLogger(BasketServiceImpl.class);

    @Inject
    private BasketRepository repository;

    @Inject
    private BookService bookService;

    @Inject
    private UserService userService;

    @Inject
    private OrderService orderService;

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
    @Transactional(readOnly = true)
    public List<Book> getBooks(long id) {
        log.debug("Request to get basket books - {}", id);
        Basket basket = repository.findByUserId(id).orElseThrow(()
                -> new ModelNotFoundException("basket with id " + id + " can;t be find"));
        return basket.getBooks().stream().collect(Collectors.toList());
    }

    @Override
    public Basket addBook(long id, String bookSlug) {
        log.debug("Request to add book to a basket - {}, {}", id, bookSlug);
        Basket basket = repository.findByUserId(id).orElseThrow(() ->
                new ModelNotFoundException("basket with id " + id + " can't be find"));
        Book book = bookService.findBySlug(bookSlug).orElseThrow(() ->
                new ModelNotFoundException("book with slug " + bookSlug + " can't be find"));

        basket.getBooks().add(book);

        return repository.save(basket);
    }

    @Override
    public void removeBook(long id, String bookSlug) {
        log.debug("Request to remove book from the basket - {}, {}", id, bookSlug);
        Basket basket = repository.findByUserId(id).orElseThrow(() ->
                new ModelNotFoundException("basket with id " + id + " can't be find"));
        Book book = bookService.findBySlug(bookSlug).orElseThrow(() ->
                new ModelNotFoundException("book with slug " + bookSlug + " can't be find"));

        basket.getBooks().remove(book);
        repository.save(basket);
    }

    @Override
    public boolean hasBook(long id, String bookSlug) {
        log.debug("Request to check if the basket has a book- {}, {}", id, bookSlug);
        return repository.findByUserIdAndBooksSlug(id, bookSlug).isPresent();
    }

    @Override
    public Basket addBooks(long id, Collection<Book> books) {
        log.debug("Request to add books to a basket - {}, {}", id, books);
        Basket basket = repository.findByUserId(id).orElseThrow(() ->
                new ModelNotFoundException("Basket with id " + id + " don't exists"));

        basket.getBooks().addAll(books);

        return repository.save(basket);
    }

    @Override
    public Order checkout(User user, BookOrderVM bookOrderVM) throws PayPalRESTException {
        log.debug("Request to checkout basket - {}, {}", user, bookOrderVM);
        Basket basket = repository.findByUserId(user.getId()).orElseThrow(() ->
                new ModelNotFoundException("basket with id " + user.getId() + " can't be find"));
        Set<Book> basketBooks = basket.getBooks();

        if (basketBooks.isEmpty()) {
            throw new EmptyBasketException();
        }

        Order order = orderService.create(user, basketBooks, bookOrderVM, true);

        // TODO send a mail

        basket.setBooks(Collections.emptySet());
        repository.save(basket);

        return order;
    }
}
