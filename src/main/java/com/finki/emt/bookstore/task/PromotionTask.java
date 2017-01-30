package com.finki.emt.bookstore.task;

import com.finki.emt.bookstore.domain.Promotion;
import com.finki.emt.bookstore.domain.User;
import com.finki.emt.bookstore.service.MailService;
import com.finki.emt.bookstore.service.PromotionService;
import com.finki.emt.bookstore.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

@Component
public class PromotionTask {

    private static final Logger log = LoggerFactory.getLogger(PromotionTask.class);

    @Inject
    private PromotionService promotionService;

    @Inject
    private UserService userService;

    @Inject
    private MailService mailService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void removeExpiredPromotions() {
        log.debug("Executing task for removing expired promotions");
        promotionService.deleteExpired();
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void sendPromotionEmailToTheUsers() {
        log.debug("Executing task for sending promotion email to the users");
        List<User> users = userService.findAll();
        List<Promotion> promotions = promotionService.findLatest();
        mailService.sendLatestPromotionsMail(users, promotions);
    }
}
