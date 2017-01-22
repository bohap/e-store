package com.finki.emt.bookstore.web.rest;

import com.finki.emt.bookstore.domain.User;
import com.finki.emt.bookstore.service.UserService;
import com.finki.emt.bookstore.web.rest.response.MinifiedUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

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

    private List<MinifiedUserResponse> convert(List<User> users) {
        return users.stream()
                .map(MinifiedUserResponse::new)
                .collect(Collectors.toList());
    }
}
