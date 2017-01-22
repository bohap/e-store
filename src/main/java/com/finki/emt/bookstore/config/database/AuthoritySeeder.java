package com.finki.emt.bookstore.config.database;

import com.finki.emt.bookstore.domain.Authority;
import com.finki.emt.bookstore.security.AuthoritiesConstants;
import com.finki.emt.bookstore.service.AuthorityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.stream.Stream;

/**
 * Save the admin and user authorities if they don;t exists already.
 */
@Component
//@Profile("dev")
public class AuthoritySeeder {

    private final Logger log = LoggerFactory.getLogger(AuthoritySeeder.class);

    @Inject
    private AuthorityService service;

    private final String[] authorities = {
            AuthoritiesConstants.ADMIN,
            AuthoritiesConstants.USER};

    void seed() {
        log.debug("Request for seeding Authority entity");
        Stream.of(authorities)
                .filter(a -> !service.exists(a))
                .map(Authority::new)
                .forEach(a -> service.save(a));
    }
}
