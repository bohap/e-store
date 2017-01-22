package com.finki.emt.bookstore.web.rest;

import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.service.BookService;
import com.finki.emt.bookstore.web.rest.response.MinifiedBookResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Inject
    private BookService service;

    @GetMapping
    public List<MinifiedBookResponse> index() {
        return service.findAll().stream()
                .map(MinifiedBookResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{slug}")
    public Book show(@PathVariable String slug) {
        return service.findBySlug(slug)
                .orElseThrow(() -> new HttpServerErrorException(
                        HttpStatus.NOT_FOUND, "Book with slug " + slug + " can't be find"));
    }

    @GetMapping("/{slug}/image")
    public void image(@PathVariable String slug, HttpServletResponse response) throws IOException {
        Book book = service.findBySlug(slug).orElseThrow(() ->
                new HttpServerErrorException(
                        HttpStatus.NOT_FOUND, "Book with slug " + slug + " can't be find"));
        byte[] image = book.getImage();
        if (image == null) {
            throw new IllegalStateException("Book with slug " + slug + " has a null image");
        }
        OutputStream out = response.getOutputStream();
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        out.write(image);
        out.flush();
        out.close();
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> store(@Valid @RequestBody Book book) {
        // TODO
        return null;
    }

    @PutMapping(value = "/{slug}")
    public ResponseEntity<?> update() {
        // TODO
        return null;
    }

    @DeleteMapping(value = "/{slug}")
    public ResponseEntity<?> delete() {
        // TODO
        return null;
    }
}
