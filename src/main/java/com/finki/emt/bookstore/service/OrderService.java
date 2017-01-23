package com.finki.emt.bookstore.service;

import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.domain.Order;
import com.finki.emt.bookstore.domain.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface OrderService {

    List<Order> findAll();

    List<Order> findAllByUserSlug(String userSlug);

    Optional<Order> findById(long id);

    Order save(Order order);

    void delete(long id);

    Order create(User user, Collection<Book> books);
}
