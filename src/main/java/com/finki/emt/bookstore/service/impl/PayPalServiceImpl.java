package com.finki.emt.bookstore.service.impl;

import com.finki.emt.bookstore.config.BookStoreProperties;
import com.finki.emt.bookstore.domain.*;
import com.finki.emt.bookstore.domain.Order;
import com.finki.emt.bookstore.service.PayPalService;
import com.paypal.api.payments.*;
import com.paypal.api.payments.CreditCard;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PayPalServiceImpl implements PayPalService {

    private final Logger log = LoggerFactory.getLogger(PayPalServiceImpl.class);

    @Inject
    private BookStoreProperties properties;

    @Override
    public Payment createPayments(Order order) throws PayPalRESTException {
        log.debug("Creating payment - {}", order);
        BookStoreProperties.PayPal payPal = properties.getPayPal();
        APIContext context = new APIContext(payPal.getClientId(), payPal.getClientSecret(),
                payPal.getMode());

        // Create a new funding instrument
        Address address = createAddress(order.getBillingAddress());
        CreditCard creditCard = createCreditCard(order.getUser(), order.getCreditCard());
        creditCard.setBillingAddress(address);

        FundingInstrument fundingInstrument = new FundingInstrument();
        fundingInstrument.setCreditCard(creditCard);

        // Create the payer
        Payer payer = new Payer();
        payer.setFundingInstruments(Collections.singletonList(fundingInstrument));
        payer.setPaymentMethod("credit_card");

        // Set the sum of the items price, the tax and the shipping
        DecimalFormat df = new DecimalFormat("#.##");
        double sum = Double.valueOf(df.format(getPrice(order.getBooks())));
        double tax = Double.valueOf(df.format(sum * 0.18));
        double shipping = Double.valueOf(df.format(sum* 0.05));

        // Create the details and amount
        Details details = new Details();
        details.setSubtotal(String.format("%.2f", sum));
        details.setTax(String.format("%.2f", tax));
        details.setShipping(String.format("%.2f", shipping));

        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(String.format("%.2f", sum + tax + shipping));
        amount.setDetails(details);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(createDescription(order.getBooks()));

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(Collections.singletonList(transaction));

        Payment createdPayment;

        try {
            createdPayment = payment.create(context);
            log.debug("Payment created");
        } catch (PayPalRESTException exp) {
            log.debug("PayPal transaction error - {} {}", order.getUser(), exp.getMessage());
            throw exp;
        }

        return createdPayment;
    }

    private Address createAddress(BillingAddress address) {
        return (Address) new Address()
                .setType(address.getType())
                .setLine1(address.getStreet())
                .setCity(address.getCity())
                .setPostalCode(address.getPostalCode())
                .setState(address.getState())
                .setCountryCode(address.getCountryCode());
    }

    private CreditCard createCreditCard(User user, com.finki.emt.bookstore.domain.CreditCard card) {
        String fullName = user.getName();
        String[] parts = fullName.split("\\s+");
        String firstName = parts[0];
        String lastName = IntStream.range(1, parts.length)
                .mapToObj(i -> parts[i])
                .collect(Collectors.joining(" "));

        return new CreditCard()
                .setType(card.getType())
                .setNumber(card.getNumber())
                .setExpireMonth(card.getExpireMonth())
                .setExpireYear(card.getExpireYear())
                .setCvv2(card.getCvv2())
                .setFirstName(firstName)
                .setLastName(lastName);
    }

    private double getPrice(Set<BookOrder> orderBooks) {
        return orderBooks.stream()
                .mapToDouble(o -> o.getPrice() * o.getQuantity())
                .sum();
    }

    private String createDescription(Set<BookOrder> orderBooks) {
        return "Order with items: " + orderBooks.stream()
                .map(b -> String.valueOf(b.getPk().getBook().getId()))
                .collect(Collectors.joining(", "));
    }
}
