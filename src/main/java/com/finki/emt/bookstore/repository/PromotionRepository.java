package com.finki.emt.bookstore.repository;

import com.finki.emt.bookstore.domain.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface PromotionRepository extends JpaRepository<Promotion, Long>,
                                             JpaSpecificationExecutor<Promotion> {

    Optional<Promotion> findByBookId(long bookId);

    List<Promotion> findTop10ByOrderByStart();
}
