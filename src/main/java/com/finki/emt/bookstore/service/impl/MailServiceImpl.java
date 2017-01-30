package com.finki.emt.bookstore.service.impl;

import com.finki.emt.bookstore.config.BookStoreProperties;
import com.finki.emt.bookstore.domain.Order;
import com.finki.emt.bookstore.domain.Promotion;
import com.finki.emt.bookstore.domain.User;
import com.finki.emt.bookstore.service.MailService;
import com.paypal.base.codec.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Locale;

@Service
public class MailServiceImpl implements MailService {

    private Logger log = LoggerFactory.getLogger(MailServiceImpl.class);

    @Inject
    private BookStoreProperties properties;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Inject
    private JavaMailSender javaMailSender;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Inject
    private SpringTemplateEngine templateEngine;

    private void sendEmail(String to, String subject, String content,
                           boolean isMultipart, boolean isHtml) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(properties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Send email to User - {}", to);
        } catch (Exception exp) {
            log.warn("EMail could not be send to user - {}, {}", to, exp);
        }
    }

    @Override
    @Async
    public void sendOrderCreatedMail(Order order) {
        log.debug("Sending order crated email for order - {}", order);
        Context context = new Context(Locale.ENGLISH);

        double totalPrice = order.getBooks().stream()
                .mapToDouble(b -> b.getPrice() * b.getQuantity())
                .sum();
        context.setVariable("order", order);
        context.setVariable("totalPrice", totalPrice);
        String content = templateEngine.process("order-created", context);
        String subject = "Book Store - New order has been crated";
        User user = order.getUser();
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Override
    public void sendLatestPromotionsMail(List<User> users, List<Promotion> promotions) {
        log.debug("Sending promotions mail to the users");
        users.forEach(u -> {
            Context context = new Context(Locale.ENGLISH);
            context.setVariable("promotions", promotions);
            String content = templateEngine.process("promotions", context);
            String subject = "Book Store - New promotions";
            sendEmail(u.getEmail(), subject, content, false, true);
        });
    }
}
