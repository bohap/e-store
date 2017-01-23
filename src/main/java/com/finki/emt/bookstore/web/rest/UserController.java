package com.finki.emt.bookstore.web.rest;

import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.domain.Order;
import com.finki.emt.bookstore.domain.User;
import com.finki.emt.bookstore.service.OrderService;
import com.finki.emt.bookstore.service.UserService;
import com.finki.emt.bookstore.web.rest.errors.ModelNotFoundException;
import com.finki.emt.bookstore.web.rest.response.MinifiedUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<MinifiedUserResponse> index() {
        return this.convert(service.findAll());
    }

    @GetMapping(value = "/admins")
    public List<MinifiedUserResponse> admins() {
        return this.convert(service.findAllAdmins());
    }

    @GetMapping(value = "/regulars")
    public List<MinifiedUserResponse> regularUsers() {
        return this.convert(service.findAllRegularUsers());
    }

    @GetMapping(value = "/{slug}")
    public User show(@PathVariable String slug) {
        return service.findBySlug(slug).orElseThrow(() ->
                new HttpServerErrorException(HttpStatus.NOT_FOUND,
                        "User with slug " + slug + " can't be find"));
    }

    @GetMapping(value = "/{slug}/orders")
    public List<Order> orders(@PathVariable String slug) {
        try {
            return orderService.findAllByUserSlug(slug);
        } catch (ModelNotFoundException exp) {
            throw new HttpServerErrorException(HttpStatus.NOT_FOUND, exp.getMessage());
        }
    }

    @GetMapping(value = "/{slug}/favorites")
    public List<Book> favorites(@PathVariable String slug) {
        try {
            return service.findAllFavoritesBuSlug(slug);
        } catch (ModelNotFoundException exp) {
            throw new HttpServerErrorException(HttpStatus.NOT_FOUND, exp.getMessage());
        }
    }

    private List<MinifiedUserResponse> convert(List<User> users) {
        return users.stream()
                .map(MinifiedUserResponse::new)
                .collect(Collectors.toList());
    }
}
