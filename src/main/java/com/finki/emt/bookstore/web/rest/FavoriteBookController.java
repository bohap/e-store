package com.finki.emt.bookstore.web.rest;

import com.finki.emt.bookstore.security.Principal;
import com.finki.emt.bookstore.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class FavoriteBookController {

    @Inject
    private BookService service;

    @GetMapping("/{slug}/favorites/exists")
    public Map<String, Boolean> exists(@PathVariable String slug,
                                       @AuthenticationPrincipal Principal principal) {
        boolean exists = service.isAddedToTheFavorites(slug, principal.getUser());

        return Collections.singletonMap("exists", exists);
    }

    @PostMapping("/{slug}/favorites")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> store(@PathVariable String slug,
                                   @AuthenticationPrincipal Principal principal) {
        service.addToFavorites(slug, principal.getUser());

        return Collections.singletonMap("message", "Book added to favorites");
    }

    @DeleteMapping("/{slug}/favorites")
    public ResponseEntity<?> remove(@PathVariable String slug,
                                    @AuthenticationPrincipal Principal principal) {
        service.removeFromFavorites(slug, principal.getUser());
        return ResponseEntity.noContent().build();
    }
}
