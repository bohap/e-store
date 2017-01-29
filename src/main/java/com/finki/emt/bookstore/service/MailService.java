package com.finki.emt.bookstore.service;

import com.finki.emt.bookstore.domain.Order;
import com.finki.emt.bookstore.domain.Promotion;
import com.finki.emt.bookstore.domain.User;

import java.util.List;

public interface MailService {

    void sendOrderCreatedMail(Order order);

    void sendLatestPromotionsMail(List<User> users, List<Promotion> promotions);
}
