package com.finki.emt.bookstore.web.rest;

import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.security.Principal;
import com.finki.emt.bookstore.service.BasketService;
import com.finki.emt.bookstore.web.rest.response.MinifiedBookResponse;
import com.finki.emt.bookstore.web.rest.vm.BookOrderVM;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/basket")
public class BasketController {

    @Inject
    private BasketService service;

    @GetMapping("/books")
    public List<MinifiedBookResponse> getBooks(@AuthenticationPrincipal Principal principal) {
        List<Book> books = service.getBooks(principal.getId());
        return books.stream()
                .map(MinifiedBookResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/books/{slug}/exists")
    public Map<String, Boolean> hasBook(@PathVariable String slug,
                                       @AuthenticationPrincipal Principal principal) {
        boolean exists = service.hasBook(principal.getId(), slug);
        return Collections.singletonMap("exists", exists);
    }

    @PostMapping("/books/{slug}")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> addBook(@PathVariable String slug,
                                       @AuthenticationPrincipal Principal principal) {
        service.addBook(principal.getId(), slug);

        return Collections.singletonMap("message", "book added to the basket");
    }

    @DeleteMapping("/books/{slug}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> removeBook(@PathVariable String slug,
                                        @AuthenticationPrincipal Principal principal) {
        service.removeBook(principal.getId(), slug);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> checkout(@AuthenticationPrincipal Principal principal,
                                        @Valid @RequestBody BookOrderVM bookOrderVM) {
        try {
            service.checkout(principal.getUser(), bookOrderVM);
        } catch (PayPalRESTException e) {
            throw new HttpServerErrorException(
                    HttpStatus.BAD_REQUEST,
                    "The provided PayPal information are invalid! Please enter " +
                            "valid information and try again");
        }

        return Collections.singletonMap("message", "order created");
    }
}
