package com.finki.emt.bookstore.repository;

import com.finki.emt.bookstore.domain.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>,
                                        JpaSpecificationExecutor<Book> {

    Optional<Book> findBySlug(String slug);

    Optional<Book> findFirstByOrderById();

    List<Book> findDistinctByCategoriesNameIn(Pageable pageable, String... categories);

    Optional<Book> findBySlugAndFavoritesId(String slug, long userId);
}