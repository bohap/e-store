package com.finki.emt.bookstore.repository;

import com.finki.emt.bookstore.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>,
                                         JpaSpecificationExecutor<Order> {

    Optional<Order> findById(long id);

    List<Order> findByUserId(long userId);
}
