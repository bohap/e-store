package com.finki.emt.bookstore.repository;

import com.finki.emt.bookstore.domain.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PromotionRepository extends JpaRepository<Promotion, Long>,
                                             JpaSpecificationExecutor<Promotion> {

    Optional<Promotion> findByBookId(long bookId);
}
