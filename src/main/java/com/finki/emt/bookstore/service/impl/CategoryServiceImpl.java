package com.finki.emt.bookstore.service.impl;

import com.finki.emt.bookstore.domain.Category;
import com.finki.emt.bookstore.repository.CategoryRepository;
import com.finki.emt.bookstore.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Inject
    private CategoryRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        log.debug("Request to get all Categories");
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> findById(long id) {
        log.debug("Request to get Category by id - {}", id);
        return Optional.ofNullable(repository.findOne(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> findByName(String name) {
        log.debug("Request to get Category by name - {}", name);
        return repository.findByName(name);
    }

    @Override
    public Category save(Category category) {
        log.debug("Request to save Category - {}", category);
        return repository.save(category);

    }

    @Override
    public void delete(long id) {
        log.debug("Request to delete Category - ", id);
        repository.delete(id);
    }

    @Override
    public boolean exists(String name) {
        return this.findByName(name).isPresent();
    }

    @Override
    public Collection<Category> sync(Collection<Category> categories) {
        return categories.stream()
                .map(c -> findByName(c.getName()).orElseGet(() -> save(c)))
                .collect(Collectors.toList());
    }
}
