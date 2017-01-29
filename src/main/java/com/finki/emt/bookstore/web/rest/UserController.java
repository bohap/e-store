package com.finki.emt.bookstore.web.rest;

import com.finki.emt.bookstore.domain.Order;
import com.finki.emt.bookstore.domain.User;
import com.finki.emt.bookstore.security.Principal;
import com.finki.emt.bookstore.service.OrderService;
import com.finki.emt.bookstore.service.UserService;
import com.finki.emt.bookstore.util.exceptions.ModelNotFoundException;
import com.finki.emt.bookstore.web.rest.response.MinifiedBookResponse;
import com.finki.emt.bookstore.web.rest.response.MinifiedUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<? extends Serializable> index(Optional<Boolean> minified) {
        boolean minifiedResponse = minified.orElse(true);
        if (minifiedResponse) {
            return this.convert(service.findAll());
        }
        return service.findAll();
    }

    @GetMapping("/count")
    public Map<String, Long> count() {
        long count = service.count();
        return Collections.singletonMap("count", count);
    }

    @PreAuthorize("@userService.canSee(#principal, #slug)")
    @GetMapping("/{slug}")
    public User show(@PathVariable String slug,
                     @AuthenticationPrincipal Principal principal) {
        return service.findBySlug(slug).orElseThrow(() ->
                new HttpServerErrorException(HttpStatus.NOT_FOUND,
                        "User with slug " + slug + " can't be find"));
    }

    @PreAuthorize("@userService.canSee(#principal, #slug)")
    @GetMapping("/{slug}/orders")
    public List<Order> orders(@PathVariable String slug,
                              @AuthenticationPrincipal Principal principal) {
        return orderService.findAllByUserSlug(slug);
    }

    @PreAuthorize("@userService.canSee(#principal, #slug)")
    @GetMapping("/{slug}/favorites")
    public List<MinifiedBookResponse> favorites(@PathVariable String slug,
                                                @AuthenticationPrincipal Principal principal) {
        try {
            return service.findAllFavoritesBuSlug(slug).stream()
                    .map(MinifiedBookResponse::new)
                    .collect(Collectors.toList());
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
