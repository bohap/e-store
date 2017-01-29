package com.finki.emt.bookstore.config.database;

import com.finki.emt.bookstore.domain.BillingAddress;
import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.domain.CreditCard;
import com.finki.emt.bookstore.domain.User;
import com.finki.emt.bookstore.security.AuthoritiesConstants;
import com.finki.emt.bookstore.service.BookService;
import com.finki.emt.bookstore.service.OrderService;
import com.finki.emt.bookstore.service.UserService;
import com.finki.emt.bookstore.web.rest.vm.BookOrderQuantityVM;
import com.finki.emt.bookstore.web.rest.vm.BookOrderVM;
import com.paypal.base.rest.PayPalRESTException;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class OrderSeeder {

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final Lorem lorem = LoremIpsum.getInstance();

    @Inject
    private OrderService service;

    @Inject
    private BookService bookService;

    @Inject
    private UserService userService;

    private final Random random = new Random();

    void seed() {
        log.debug("Request to seed Order entity");
        List<User> users = userService.findByAuthority(AuthoritiesConstants.USER);
        List<Book> books = bookService.findAll();

        IntStream.range(0, 300)
            .forEach(i -> {
                User user = users.get(random.nextInt(users.size()));

                Collections.shuffle(books);
                List<Book> orderBooks = books.subList(0, random.nextInt(14) + 1);

                List<BookOrderQuantityVM> bookOrderQuantityVMS = orderBooks.stream()
                        .map(o -> {
                            int quantity = random.nextInt(10) + 1;
                            return new BookOrderQuantityVM(o.getSlug(), quantity);
                        })
                        .collect(Collectors.toList());

                final String city = lorem.getCity().replaceAll("[^\\x20-\\x7e]", "");
                final String country = lorem.getCountry().replaceAll("[^\\x20-\\x7e]", "");
                final String zipCode = lorem.getZipCode().replaceAll("[^\\x20-\\x7e]", "");
                BillingAddress billingAddress = new BillingAddress()
                        .setType("HOME")
                        .setStreet(String.format("%s, %s, %s", city, country, zipCode))
                        .setCity(city)
                        .setState(country)
                        .setPostalCode(zipCode)
                        .setCountryCode(lorem.getStateAbbr());

                CreditCard creditCard = new CreditCard()
                        .setType("visa")
                        .setNumber(generateCreditCardNumber())
                        .setExpireMonth(random.nextInt(30) + 1)
                        .setExpireYear(random.nextInt(10) + 2017)
                        .setCvv2("012");

                BookOrderVM bookOrderVM = new BookOrderVM(billingAddress,
                        creditCard, bookOrderQuantityVMS);

                try {
                    service.create(user, orderBooks, bookOrderVM, false);
                } catch (PayPalRESTException e) {
                    e.printStackTrace();
                }
            });
    }

    private String generateCreditCardNumber() {
        return IntStream.range(0, 16)
                .mapToObj(i -> String.valueOf(random.nextInt(10)))
                .collect(Collectors.joining());
    }
}
