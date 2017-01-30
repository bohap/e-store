package com.finki.emt.bookstore.service;

import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.domain.Promotion;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface PromotionService {

    List<Promotion> findAll();

    Optional<Promotion> findById(long id);

    void delete(long id);

    void delete(String bookSlug);

    void delete(Promotion promotion);

    Promotion create(Book book, double newPrice, ZonedDateTime start, ZonedDateTime end);

    Promotion save(String bookSlug, Promotion promotion);

    List<Promotion> findLatest();

    void deleteExpired();
}
