package com.finki.emt.bookstore.service;

import com.finki.emt.bookstore.domain.BillingAddress;
import com.finki.emt.bookstore.domain.BookOrder;
import com.finki.emt.bookstore.domain.CreditCard;
import com.finki.emt.bookstore.domain.Order;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import java.util.List;

public interface PayPalService {

    Payment createPayments(Order order) throws PayPalRESTException;
}
