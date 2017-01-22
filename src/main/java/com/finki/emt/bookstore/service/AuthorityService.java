package com.finki.emt.bookstore.service;

import com.finki.emt.bookstore.domain.Authority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public interface AuthorityService {

    List<Authority> findAll();

    Optional<Authority> findByName(String name);

    Authority save(Authority authority);

    void delete(String name);

    boolean exists(String name);
}
