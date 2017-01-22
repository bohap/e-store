package com.finki.emt.bookstore.repository;

import com.finki.emt.bookstore.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>,
                                            JpaSpecificationExecutor<Category> {

    Optional<Category> findByName(String name);
}
