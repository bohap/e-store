package com.finki.emt.bookstore.service;

import com.finki.emt.bookstore.domain.Basket;
import com.finki.emt.bookstore.domain.Book;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BasketService {

    List<Basket> findAll();

    Optional<Basket> findById(long id);

    Basket addBook(long id, Book book);

    Basket addBooks(long id, Collection<Book> books);
}
