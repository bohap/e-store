package com.finki.emt.bookstore.web.rest;

import com.finki.emt.bookstore.domain.Promotion;
import com.finki.emt.bookstore.service.PromotionService;
import com.finki.emt.bookstore.web.rest.vm.PromotionVM;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class PromotionController {

    @Inject
    private PromotionService service;

    @PostMapping(value = "/{slug}/promotion")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> store(@PathVariable String slug,
                     @Valid @RequestBody PromotionVM promotionVM) {
        service.save(slug, promotionVM.createPromotion());

        return Collections.singletonMap("message", "Book added on promotion");
    }

    @DeleteMapping(value = "/{slug}/promotion")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity delete(@PathVariable String slug) {
        service.delete(slug);

        return ResponseEntity.noContent().build();
    }
}
