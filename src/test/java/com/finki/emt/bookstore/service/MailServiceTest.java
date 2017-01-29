package com.finki.emt.bookstore.service;

import com.finki.emt.bookstore.BookStoreApplication;
import com.finki.emt.bookstore.domain.BookOrder;
import com.finki.emt.bookstore.domain.Order;
import com.finki.emt.bookstore.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.finki.emt.bookstore.repository.util.ModelUtil.mockBook;
import static com.finki.emt.bookstore.repository.util.ModelUtil.mockUser;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BookStoreApplication.class)
@ActiveProfiles("test")
public class MailServiceTest {

    @Inject
    private MailService service;

    @Test
    public void testSendEmail() {
        User user = mockUser(1, ZonedDateTime.now());
        Order order = new Order(0, ZonedDateTime.now(), ZonedDateTime.now(), false);
        order.setUser(user);
        Set<BookOrder> bookOrders = Stream.of(mockBook(1, null, null), mockBook(2, null, null),
                mockBook(3, null, null), mockBook(4, null, null))
                .map(b -> new BookOrder(b, order, b.getPrice(), 1))
                .collect(Collectors.toSet());
        order.setBooks(bookOrders);

        double totalPrice = bookOrders.stream()
                .mapToDouble(b -> b.getPrice() * b.getQuantity())
                .sum();

        service.sendOrderCreatedMail(order, totalPrice);
    }
}
