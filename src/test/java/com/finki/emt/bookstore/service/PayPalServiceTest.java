package com.finki.emt.bookstore.service;

import com.finki.emt.bookstore.BookStoreApplication;
import com.finki.emt.bookstore.config.BookStoreProperties;
import com.finki.emt.bookstore.domain.*;
import com.finki.emt.bookstore.domain.Order;
import com.finki.emt.bookstore.repository.util.ModelUtil;
import com.paypal.api.payments.*;
import com.paypal.api.payments.CreditCard;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BookStoreApplication.class)
@ActiveProfiles("test")
public class PayPalServiceTest {

    @Inject
    private BookStoreProperties properties;

    @Inject
    private PayPalService payPalService;

    private ZonedDateTime now = ZonedDateTime.now();

    @Test
    public void testCall() {
        Book book = ModelUtil.mockBook(1, now, null);

        BookStoreProperties.PayPal payPal = properties.getPayPal();
        APIContext context = new APIContext(payPal.getClientId(),
                payPal.getClientSecret(), payPal.getMode());

        Address billiAddress = (Address) new Address()
                .setType("HOME")
                .setLine1("Some Address")
                .setCity("City")
                .setPostalCode("1400")
                .setState("Republic of Macedonia")
                .setCountryCode("MK");

        CreditCard card = new CreditCard()
                .setType("visa")
                .setNumber("4032030487906068")
                .setExpireMonth(2)
                .setExpireYear(2022)
                .setCvv2("012")
                .setFirstName("Test")
                .setLastName("User")
                .setBillingAddress(billiAddress);

        FundingInstrument fundingInstrument = new FundingInstrument();
        fundingInstrument.setCreditCard(card);

        Payer payer = new Payer();
        payer.setFundingInstruments(Collections.singletonList(fundingInstrument));
        payer.setPaymentMethod("credit_card");

        Details details = new Details();
        DecimalFormat df = new DecimalFormat("#.##");
        double sum = Double.valueOf(df.format(book.getPrice()));
        double tax = Double.valueOf(df.format(sum * 0.18));
        double shipping = Double.valueOf(df.format(sum * 0.05));
        details.setSubtotal(String.format("%.2f", sum));
        details.setTax(String.format("%.2f", tax));
        details.setShipping(String.format("%.2f", shipping));

        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(String.format("%.2f", sum + tax + shipping));
        amount.setDetails(details);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("Test transaction");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(Collections.singletonList(transaction));
        Payment createdPayment;

        try {
            createdPayment = payment.create(context);
            assertThat(createdPayment, is(notNullValue()));
        } catch (PayPalRESTException exp) {
            System.out.println(exp.getMessage());
        }
    }

    @Test
    public void testService() {
        Random rand = new Random();
        final double minPrice = 100;
        final double maxPrice = 5000;
        User user = ModelUtil.mockUser(1, now);
        Book book1 = ModelUtil.mockBook(1, now, null);
        Book book2 = ModelUtil.mockBook(2, now, null);
        Book book3 = ModelUtil.mockBook(3, now, null);
        Book book4 = ModelUtil.mockBook(4, now, null);
        Set<BookOrder> books = Stream.of(book1, book2, book3, book4)
                .map(b -> {
                    double price = minPrice + (maxPrice - minPrice) * rand.nextDouble();
                    int quantity = rand.nextInt(9) + 1;
                    return new BookOrder(b, null, price, quantity);
                })
                .collect(Collectors.toSet());

        BillingAddress address = new BillingAddress()
                .setType("HOME")
                .setStreet("Some Address")
                .setCity("City")
                .setPostalCode("1400")
                .setState("Republic of Macedonia")
                .setCountryCode("MK");

        com.finki.emt.bookstore.domain.CreditCard creditCard =
                new com.finki.emt.bookstore.domain.CreditCard()
                    .setType("visa")
                .setNumber("4032030487906068")
                .setExpireMonth(2)
                .setExpireYear(2022)
                .setCvv2("012");

        Order order = new Order(0, now, now, false);
        order.setUser(user);
        order.setBillingAddress(address);
        order.setCreditCard(creditCard);
        order.setBooks(books);

        try {
            payPalService.createPayments(order);
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
    }
}
