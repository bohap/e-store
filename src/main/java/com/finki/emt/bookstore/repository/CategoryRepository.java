package com.finki.emt.bookstore.repository;

import com.finki.emt.bookstore.domain.Category;
import com.finki.emt.bookstore.domain.util.CategoryBooksCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>,
                                            JpaSpecificationExecutor<Category> {

    Optional<Category> findByName(String name);

    @Query("select c.id, c.name, count(c.id) from Category c inner join c.books cb group by c.id")
    List<CategoryBooksCount> groupByBooks();
}
