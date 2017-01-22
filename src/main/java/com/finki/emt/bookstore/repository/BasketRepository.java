package com.finki.emt.bookstore.repository;

import com.finki.emt.bookstore.domain.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Long>,
                                          JpaSpecificationExecutor<Basket> {

    Optional<Basket> findByUserId(long userId);
}
