package com.finki.emt.bookstore.web.rest;

import com.finki.emt.bookstore.domain.Category;
import com.finki.emt.bookstore.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Inject
    private CategoryService service;

    @GetMapping
    public List<Category> index() {
        return service.findAll();
    }
}
