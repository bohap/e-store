package com.finki.emt.bookstore.service.impl;

import com.finki.emt.bookstore.domain.Authority;
import com.finki.emt.bookstore.repository.AuthorityRepository;
import com.finki.emt.bookstore.service.AuthorityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuthorityServiceImpl implements AuthorityService {

    private final Logger log = LoggerFactory.getLogger(AuthorityServiceImpl.class);

    @Inject
    private AuthorityRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Authority> findAll() {
        log.debug("Request to get all Authorities");
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Authority> findByName(String name) {
        log.debug("Request to get Authority - {}", name);
        return Optional.ofNullable(repository.findOne(name));
    }

    @Override
    public Authority save(Authority authority) {
        log.debug("Request to save Authority {}", authority);
        return repository.save(authority);
    }

    @Override
    public void delete(String name) {
        log.debug("Request to delete Authority - {}", name);
        repository.delete(name);
    }

    @Override
    public boolean exists(String name) {
        return this.findByName(name).isPresent();
    }
}
