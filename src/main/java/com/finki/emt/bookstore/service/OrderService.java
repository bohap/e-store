package com.finki.emt.bookstore.service;

import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.domain.BookOrder;
import com.finki.emt.bookstore.domain.Order;
import com.finki.emt.bookstore.domain.User;
import com.finki.emt.bookstore.security.Principal;
import com.finki.emt.bookstore.web.rest.vm.BookOrderVM;
import com.paypal.base.rest.PayPalRESTException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderService {

    List<Order> findAll();

    long count();

    Set<BookOrder> getBooks(long id);

    List<Order> findAllByUserSlug(String userSlug);

    Optional<Order> findById(long id);

    Order save(Order order);

    void delete(long id);

    Order create(User user, Collection<Book> books, BookOrderVM bookOrderVM,
                 boolean createPayPalPayment) throws PayPalRESTException;

    Order finish(long id);

    boolean canSee(long id);
}
