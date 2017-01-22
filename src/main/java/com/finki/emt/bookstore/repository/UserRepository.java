package com.finki.emt.bookstore.repository;

import com.finki.emt.bookstore.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findById(long id);

    Optional<User> findBySlug(String slug);

    Optional<User> findByEmail(String email);

    List<User> findByAuthoritiesName(String name);

    long countByAuthoritiesName(String name);

    Optional<User> findByIdAndAuthoritiesName(long id, String name);

    Optional<User> findFirstByOrderByIdDesc();
}
