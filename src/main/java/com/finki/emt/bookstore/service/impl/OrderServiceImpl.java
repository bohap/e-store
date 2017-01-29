package com.finki.emt.bookstore.service.impl;

import com.finki.emt.bookstore.domain.*;
import com.finki.emt.bookstore.repository.BookOrderRepository;
import com.finki.emt.bookstore.repository.OrderRepository;
import com.finki.emt.bookstore.security.AuthoritiesConstants;
import com.finki.emt.bookstore.security.SecurityUtil;
import com.finki.emt.bookstore.service.*;
import com.finki.emt.bookstore.util.exceptions.ModelNotFoundException;
import com.finki.emt.bookstore.web.rest.vm.BookOrderQuantityVM;
import com.finki.emt.bookstore.web.rest.vm.BookOrderVM;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service("orderService")
@Transactional
public class OrderServiceImpl implements OrderService {

    private Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Inject
    private OrderRepository repository;

    @Inject
    private BookOrderRepository bookOrderRepository;

    @Inject
    private PromotionService promotionService;

    @Inject
    private UserService userService;

    @Inject
    private PayPalService payPalService;

    @Inject
    private SecurityUtil securityUtil;

    @Inject
    private MailService mailService;

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        log.debug("Request to get all Orders");
        return repository.findAll();
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<BookOrder> getBooks(long id) {
        log.debug("Request to get books for order - {}", id);
        Order order = repository.findById(id).orElseThrow(() ->
                new ModelNotFoundException("order with id " + id + " can't be find"));
        return order.getBooks();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAllByUserSlug(String userSlug) {
        log.debug("Request to get orders for user with slug - {}", userSlug);
        User user = userService.findBySlug(userSlug).orElseThrow(() ->
                new ModelNotFoundException("user with slug " + userSlug + " cant' be find"));
        return repository.findByUserId(user.getId());
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
    public Order create(User user, Collection<Book> books, BookOrderVM bookOrderVM,
                        boolean createPayPalPayment) throws PayPalRESTException {
        log.debug("Request to create order - {}, {}, {}, {}",
                user, books, bookOrderVM, createPayPalPayment);
        ZonedDateTime now = ZonedDateTime.now();
        Order order = new Order();
        order.setCreatedAt(now);
        order.setUpdatedAt(now);
        order.setUser(user);
        order.setBillingAddress(bookOrderVM.getBillingAddress());
        order.setCreditCard(bookOrderVM.getCreditCard());

        Set<BookOrder> orderBooks = this.getBookOrders(books, bookOrderVM.getBooks());
        order.setBooks(orderBooks);

        if (createPayPalPayment) {
            payPalService.createPayments(order);
        }

        Order saved = repository.save(order);
        orderBooks.forEach(b -> {
            b.getPk().setOrder(order);
            bookOrderRepository.save(b);
        });

        mailService.sendOrderCreatedMail(saved);

        return saved;
    }

    private Set<BookOrder> getBookOrders(Collection<Book> books,
                                          List<BookOrderQuantityVM> bookOrderQuantityVMS) {
        return books.stream()
            .map(b -> {
                double price = promotionService.findById(b.getId())
                        .filter(p -> p.getStart().isBefore(ZonedDateTime.now()))
                        .map(Promotion::getNewPrice)
                        .orElse(b.getPrice());

                int quantity = bookOrderQuantityVMS.stream()
                        .filter(bo -> b.getSlug().equals(bo.getSlug()))
                        .findFirst()
                        .map(BookOrderQuantityVM::getQuantity)
                        .orElse(1);

                return new BookOrder(b, null, price, quantity);
            })
            .collect(Collectors.toSet());
    }

    @Override
    public Order finish(long id) {
        log.debug("Marking order with id {} as finished", id);
        Order order = this.findById(id).orElseThrow(() ->
                new ModelNotFoundException("order with id " + id + " can't be find"));
        order.setFinished(true);
        order.setUpdatedAt(ZonedDateTime.now());

        return repository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSee(long id) {
        User user = securityUtil.getAuthentication().getUser();
        log.debug("Checking if the authenticated user can see the order - {}, {}", user, id);
        if (user.hasAuthority(AuthoritiesConstants.ADMIN)) {
            return true;
        }

        return this.findById(id)
                .map(o -> o.getUser().getId() == user.getId())
                .orElse(false);
    }
}
