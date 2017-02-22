package com.finki.emt.bookstore.repository;

import com.finki.emt.bookstore.domain.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>,
                                        JpaSpecificationExecutor<Book> {

    Optional<Book> findBySlug(String slug);

    Optional<Book> findFirstByOrderByIdDesc();

    List<Book> findDistinctByCategoriesNameIn(Pageable pageable, String... categories);

    Optional<Book> findBySlugAndFavoritesId(String slug, long userId);

    @Query("select b from Book b left join b.orders bo group by b.id order by count(b.id) DESC")
    List<Book> groupByOrdersCount(Pageable pageable);
}
