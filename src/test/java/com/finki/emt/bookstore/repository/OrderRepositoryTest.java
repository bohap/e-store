package com.finki.emt.bookstore.repository;

import com.finki.emt.bookstore.BookStoreApplication;
import com.finki.emt.bookstore.domain.BillingAddress;
import com.finki.emt.bookstore.domain.CreditCard;
import com.finki.emt.bookstore.domain.Order;
import com.finki.emt.bookstore.domain.User;
import com.finki.emt.bookstore.repository.util.ModelUtil;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BookStoreApplication.class)
@ActiveProfiles("test")
public class OrderRepositoryTest {

    @Inject
    private OrderRepository repository;

    @Inject
    private UserRepository userRepository;

    private final Random random = new Random();

    @Test
    public void test() {
        final Lorem lorem = LoremIpsum.getInstance();

        final String city = lorem.getCity();
        final String country = lorem.getCountry();
        final String zipCode = lorem.getZipCode();
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

        final User user = ModelUtil.mockUser(1, ZonedDateTime.now());
        userRepository.save(user);
        Order order = new Order(0, ZonedDateTime.now(), ZonedDateTime.now(), false);
        order.setUser(user);
        order.setBillingAddress(billingAddress);
        order.setCreditCard(creditCard);
        Order saved = repository.save(order);
        assertThat(saved, is(notNullValue()));
    }

    private String generateCreditCardNumber() {
        return IntStream.range(0, 16)
                .mapToObj(i -> String.valueOf(random.nextInt(10)))
                .collect(Collectors.joining());
    }
}
