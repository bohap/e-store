package com.finki.emt.bookstore.service;

import com.finki.emt.bookstore.domain.*;
import com.finki.emt.bookstore.web.rest.vm.BookOrderQuantityVM;
import com.finki.emt.bookstore.web.rest.vm.BookOrderVM;
import com.paypal.base.rest.PayPalRESTException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BasketService {

    List<Basket> findAll();

    Optional<Basket> findById(long id);

    List<Book> getBooks(long id);

    Basket addBook(long id, String bookSlug);

    void removeBook(long id, String bookSlug);

    boolean hasBook(long id, String bookSlug);

    Basket addBooks(long id, Collection<Book> books);

    Order checkout(User user, BookOrderVM bookOrderVM) throws PayPalRESTException;
}
