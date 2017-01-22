package com.finki.emt.bookstore.service;

import com.finki.emt.bookstore.domain.Category;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> findAll();

    Optional<Category> findById(long id);

    Optional<Category> findByName(String name);

    Category save(Category category);

    void delete(long id);

    boolean exists(String name);

    Collection<Category> sync(Collection<Category> categories);
}
