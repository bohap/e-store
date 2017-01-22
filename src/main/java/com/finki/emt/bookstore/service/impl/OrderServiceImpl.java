package com.finki.emt.bookstore.service.impl;

import com.finki.emt.bookstore.domain.*;
import com.finki.emt.bookstore.repository.BookOrderRepository;
import com.finki.emt.bookstore.repository.OrderRepository;
import com.finki.emt.bookstore.service.OrderService;
import com.finki.emt.bookstore.service.PromotionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Inject
    private OrderRepository repository;

    @Inject
    private PromotionService promotionService;

    @Inject
    private BookOrderRepository bookOrderRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        log.debug("Request to get all Orders");
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findById(long id) {
        log.debug("Request to get Order by id - {}", id);
        return Optional.ofNullable(repository.findOne(id));
    }

    @Override
    public Order save(Order order) {
        log.debug("Request to save Order - {}", order);
        return repository.save(order);
    }

    @Override
    public void delete(long id) {
        log.debug("Request to delete Order - {}", id);
        repository.delete(id);
    }

    @Override
    public Order create(User user, Collection<Book> books) {
        ZonedDateTime now = ZonedDateTime.now();
        Order order = new Order();
        order.setCreatedAt(now);
        order.setUpdatedAt(now);
        order.setUser(user);

        Order saved = repository.save(order);
        this.storeBooks(saved, books);

        return saved;
    }

    private void storeBooks(Order order, Collection<Book> books) {
        books.forEach(b -> {
            double price = promotionService.findById(b.getId())
                    .map(Promotion::getNewPrice)
                    .orElse(b.getPrice());
            BookOrder bookOrder = new BookOrder(b, order, price);
            bookOrderRepository.save(bookOrder);
        });
    }
}
