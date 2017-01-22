package com.finki.emt.bookstore.config.database;

import com.finki.emt.bookstore.domain.Authority;
import com.finki.emt.bookstore.domain.User;
import com.finki.emt.bookstore.security.AuthoritiesConstants;
import com.finki.emt.bookstore.service.AuthorityService;
import com.finki.emt.bookstore.service.UserService;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class UserSeeder {

    private final Logger log = LoggerFactory.getLogger(UserSeeder.class);

    private final Lorem lorem = LoremIpsum.getInstance();

    @Inject
    private UserService service;

    @Inject
    private AuthorityService authorityService;

    @Inject
    private PasswordEncoder passwordEncoder;

    private ZonedDateTime now = ZonedDateTime.now();
    private User[] admins = {
            new User(0, "admin", "admin", "admin@book-store.com", "password", now, now),
            new User(0, "Borche Petrovski", "borche-petrovski", "borche.petrovski@book-store.com",
                    "password", now, now),
            new User(0, "Marijan Gajdov", "marijan-gajdov", "marijan-gajdov@book-store.com",
                    "password", now, now)};

    void seed() {
        log.debug("Request to seed User entity");
        Authority adminAuthority = authorityService.findByName(AuthoritiesConstants.ADMIN)
                .orElseThrow(() -> new RuntimeException("Admin authority not saved in the database"));

        // Save the admins
        Stream.of(admins)
                .map(a -> {
                    a.setAuthorities(Stream.of(adminAuthority).collect(Collectors.toSet()));
                    a.setPassword(passwordEncoder.encode(a.getPassword()));
                    return a;
                })
                .forEach(a -> service.save(a));

        // Save the users
        IntStream.range(0, 50)
                .forEach(i -> {
                    String name = lorem.getName();
                    String email = createEmail(name);
                    service.create(name, email, "password");
                });
    }

    private String createEmail(String name) {
        return Stream.of(name.toLowerCase().split("\\s+"))
                .collect(Collectors.joining(".")) + "@user.com";
    }
}
