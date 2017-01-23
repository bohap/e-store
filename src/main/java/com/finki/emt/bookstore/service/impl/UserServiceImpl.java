package com.finki.emt.bookstore.service.impl;

import com.finki.emt.bookstore.domain.Authority;
import com.finki.emt.bookstore.domain.Basket;
import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.domain.User;
import com.finki.emt.bookstore.repository.UserRepository;
import com.finki.emt.bookstore.security.AuthoritiesConstants;
import com.finki.emt.bookstore.service.AuthorityService;
import com.finki.emt.bookstore.service.UserService;
import com.finki.emt.bookstore.util.SlugUtil;
import com.finki.emt.bookstore.web.rest.errors.ModelNotFoundException;
import com.finki.emt.bookstore.web.rest.vm.RegisterVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Inject
    private UserRepository repository;

    @Inject
    private AuthorityService authorityService;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        log.debug("Request to get all Users");
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(long id) {
        log.debug("Request to get User by id - {}", id);
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findBySlug(String slug) {
        log.debug("Request to get User by slug - {}", slug);
        return repository.findBySlug(slug);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        log.debug("Request to get User by email - {}", email);
        return repository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        log.debug("Request to save User -{}", user);
        return repository.save(user);
    }

    @Override
    public void delete(long id) {
        log.debug("Request to delete User - ", id);
        repository.delete(id);
    }

    @Override
    public User create(String name, String email, String password) {
        log.debug("Request for creating new User - {}, {}, {}", name, email, password);
        ZonedDateTime now = ZonedDateTime.now();
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        // Set the slug
        user.setSlug(generateSlug(name));

        // Set the user authorities
        Optional<Authority> userAuthority = authorityService.findByName(AuthoritiesConstants.USER);
        userAuthority.ifPresent(a -> {
            Set<Authority> authorities = new HashSet<>();
            authorities.add(a);
            user.setAuthorities(authorities);
        });

        // Create tge basket
        Basket basket = new Basket(user, now);
        user.setBasket(basket);

        return this.save(user);
    }

    private String generateSlug(String name) {
        final String slug = SlugUtil.generate(name);
        return repository.findBySlug(slug)
                .map(u -> {
                    User last = repository.findFirstByOrderByIdDesc().orElse(new User());
                    return slug + "-" + (last.getId() + 1);
                })
                .orElse(slug);
    }

    @Override
    public User create(RegisterVM registerVM) {
        return this.create(registerVM.getName(), registerVM.getEmail(), registerVM.getPassword());
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findByAuthority(String authority) {
        return repository.findByAuthoritiesName(authority);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllAdmins() {
        return this.findByAuthority(AuthoritiesConstants.ADMIN);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllRegularUsers() {
        return this.findByAuthority(AuthoritiesConstants.USER);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAllFavoritesBuSlug(String slug) {
        User user = repository.findBySlug(slug).orElseThrow(() ->
                new ModelNotFoundException("User with slug " + slug + " can't be find"));
        return user.getFavorites().stream().collect(Collectors.toList());
    }
}
